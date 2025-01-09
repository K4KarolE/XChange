package karole;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Functions {

    static JsonNode jsonNodeApiResponse;

    static JsonNode jsonNodeLastUsedCurrencies;
    static ObjectNode objectNodeLastUsedCurrencies;
    static String lastUsedCurrFrom;
    static String lastUsedCurrTo;

    static File jsonFileLastUsedCurrencies = new File("./app/src/main/resources/lastUsedCurrencies.json");
    static ObjectMapper objectMapper = new ObjectMapper();

    static File[] jsonFiles = new File[5];
    static JsonNode[] jsonNodes = new JsonNode[5];
    static double[] ratesFrom = new double[5];
    static double[] ratesTo = new double[5];
    static int[] timeLastUpdateUnix = new int[5];
    static String[] timeLastUpdateUtc = new String[5];


    static void generateNodesFromJsons() {

        for (int i = 0; i < 5; i++) {

            String jsonPath = "./app/src/main/resources/apiResponse" + i  + ".json";    
            jsonFiles[i] = new File(jsonPath);
            
            try {
                jsonNodes[i] = objectMapper.readTree(jsonFiles[i]);
            } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        } 
    }


    static void generateRatesFromNodes() {

        for (int i = 0; i < 5; i++) {

            ratesFrom[i] = jsonNodes[i].get("rates").get(lastUsedCurrFrom).asDouble();
            ratesTo[i] = jsonNodes[i].get("rates").get(lastUsedCurrTo).asDouble();
        }
    }


    static void generateHistoricDatesFromNodes() {

        for (int i = 0; i < 5; i++) {

            String utc = jsonNodes[i].get("time_last_update_utc").asText();   
            String utcToAdd = utc.substring(4,7)+
                "/"+
                utc.substring(8,11)+
                "/"+
                utc.substring(14,16)+
                ": ";
            timeLastUpdateUtc[i] = utcToAdd;
        }
    }

   
    /*
     * LAST USED CURRENCIES
     */
    public static void generateLastUsedCurrencies() {
        try {
            jsonNodeLastUsedCurrencies = objectMapper.readTree(jsonFileLastUsedCurrencies);
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}

        lastUsedCurrFrom = jsonNodeLastUsedCurrencies.get("from").asText();
        lastUsedCurrTo = jsonNodeLastUsedCurrencies.get("to").asText();
    }


    static void writeToObjectNode(String currencyFrom, String currencyTo) {
        objectNodeLastUsedCurrencies = (ObjectNode) jsonNodeLastUsedCurrencies;
        objectNodeLastUsedCurrencies.put("from", currencyFrom);
        objectNodeLastUsedCurrencies.put("to", currencyTo);
    }


    static void writeLastUsedCurrJson() {
        try {
            objectMapper.writeValue(jsonFileLastUsedCurrencies, objectNodeLastUsedCurrencies);
        }
        catch (Exception e) {System.out.println("\nERROR: Could not write JSON file.\n");}
    }

    
    static void saveLastUsedCurrenciesGrouped(String currencyFrom, String currencyTo) {
        writeToObjectNode(currencyFrom, currencyTo);
        writeLastUsedCurrJson();
    }



     /*
     * TIME, RATES
     */
    public static Integer getNextUpdateTimeUnix() {
        return jsonNodes[0].get("time_next_update_unix").asInt();
    }



    public static boolean canGetNewApiRequest() {
        Integer currentTime = (int)Instant.now().getEpochSecond();
        Integer nextApiUpdate =  getNextUpdateTimeUnix();
        
        if (currentTime > nextApiUpdate) {
            return true;
        }
        else {
            return false;
        }
    }


    public static boolean isResponseValid() {
        String result = jsonNodeApiResponse.get("result").asText();
        if (result.equals("success")) {
            return true;
        }
        else {
            return false;
        }
    }


    static void reallocateAndWriteNodesToJson() {

        // Update nodes/jsons[1-4]
        for (int i=3; i > -1; i--) {
            jsonNodes[i+1] = jsonNodes[i];

            try {
                objectMapper.writeValue(jsonFiles[i+1], jsonNodes[i+1]);
            }
            catch (Exception e) {System.out.println(
                "ERROR: Could not reallocate previous Json files."
                );}
        }

        // Update node/json[0]
        try {
            jsonNodes[0] = jsonNodeApiResponse;
            objectMapper.writeValue(jsonFiles[0], jsonNodeApiResponse);
        }
        catch (Exception e) {System.out.println("ERROR: Could not write API response to file.");
        }

    }


    public static void appStartsGroupedActions() {
        /*
         * Checking if existing JSON is out of date
         * If yes, new API request
         * Saving the valid response as JSON  
         */
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        generateNodesFromJsons();
        generateRatesFromNodes();
        generateHistoricDatesFromNodes();

        if (canGetNewApiRequest()) {
            jsonNodeApiResponse = apiConnect.generateJsonNode();
        
            if (jsonNodeApiResponse != null && isResponseValid()) {
         
                reallocateAndWriteNodesToJson();
            }
            
            else {
                System.out.println("ERROR: No API response received.");
            }
        }
    }
}

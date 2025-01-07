package karole;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Functions {

    static apiConnect apiCall = new apiConnect();
    static JsonNode jsonNodeApiResponse;

    static JsonNode jsonNodeJSON;

    static JsonNode jsonNodeLastUsedCurrencies;
    static ObjectNode objectNodeLastUsedCurrencies;
    static String lastUsedCurrFrom;
    static String lastUsedCurrTo;

    public static Double rateFrom;
    public static Double rateTo; 
    
    static File jsonFileApi = new File("./app/src/main/resources/apiResponse.json");
    static File jsonFileLastUsedCurrencies = new File("./app/src/main/resources/lastUsedCurrencies.json");
    static ObjectMapper objectMapper = new ObjectMapper();



    static JsonNode generateJsonNode(File jsonFile, JsonNode jsonNode) {
        try {
            jsonNode = objectMapper.readTree(jsonFile);
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return jsonNode;
    }


    /*
     * LAST USED CURRENCIES
     */
    static void generateLastUsedCurrencies() {
        jsonNodeLastUsedCurrencies = generateJsonNode(jsonFileLastUsedCurrencies, jsonNodeLastUsedCurrencies);
        lastUsedCurrFrom = jsonNodeLastUsedCurrencies.get("from").asText();
        lastUsedCurrTo = jsonNodeLastUsedCurrencies.get("to").asText();
    }


    static void writeToObjectNode(String currencyFrom, String currencyTo) {
        objectNodeLastUsedCurrencies = (ObjectNode) jsonNodeLastUsedCurrencies;
        objectNodeLastUsedCurrencies.put("from", currencyFrom);
        objectNodeLastUsedCurrencies.put("to", currencyTo);
    }


    static void writeJson() {
        try {
            objectMapper.writeValue(jsonFileLastUsedCurrencies, objectNodeLastUsedCurrencies);
        }
        catch (Exception e) {System.out.println("\nERROR: Could not write JSON file.\n");}
    }

    
    static void saveLastUsedCurrenciesGrouped(String currencyFrom, String currencyTo) {
        writeToObjectNode(currencyFrom, currencyTo);
        writeJson();
    }



     /*
     * TIME, RATES
     */
    public static String getLastUpdateTimeUtc() {
        String fullTime = jsonNodeJSON.get("time_last_update_utc").toString();
        return fullTime.substring(0, 16);
    }


    public static Integer getNextUpdateTimeUnix() {
        return jsonNodeJSON.get("time_next_update_unix").asInt();
    }


    public static void generateRates() {
        rateFrom = jsonNodeJSON.get("rates").get(lastUsedCurrFrom).asDouble();
        rateTo = jsonNodeJSON.get("rates").get(lastUsedCurrTo).asDouble();
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
        String result = jsonNodeApiResponse.get("result").toString();
        if (result.equals("success")) {
            return true;
        }
        else {
            return false;
        }
    }


    public static void appStartsGroupedActions() {
        /*
         * Checking if existing JSON is out of date
         * If yes, new API request
         * Saving the valid response as JSON  
         */

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        jsonNodeJSON = generateJsonNode(jsonFileApi, jsonNodeJSON); // from the last used json 

        if (canGetNewApiRequest()) {
            jsonNodeApiResponse = apiCall.generateJsonNode();

            if (jsonNodeApiResponse != null) {
            
                if (isResponseValid()) {
                    try {
                        objectMapper.writeValue(jsonFileApi, jsonNodeApiResponse);
                        jsonNodeJSON = jsonNodeApiResponse;
                    }
                    catch (Exception e) {System.out.println("ERROR: Could not write API response to file.");}
                }
            }
            else {
                System.out.println("ERROR: No API response received.");
            }
        }
    }

}

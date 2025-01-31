package karole;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Functions {

    static JsonNode jsonNodeApiResponse;

    static JsonNode jsonNodeLastUsedCurrencies;
    static String lastUsedCurrFrom;
    static int lastUsedCurrFromIndex;
    static String lastUsedCurrTo;
    static int lastUsedCurrToIndex;

    static File jsonFileLastUsedCurrencies = new File("./app/src/main/resources/lastUsedCurrencies.json");
    static ObjectNode LastUsedCurrenciesObjectNode;
    static ObjectMapper objectMapper = new ObjectMapper();

    static File[] jsonFiles = new File[5];
    static JsonNode[] jsonNodes = new JsonNode[5];
    static double[] ratesFrom = new double[5];
    static double[] ratesTo = new double[5];
    static String[] timeLastUpdateUtc = new String[5];

    static int nextApiUpdateUnixJson;
    static int nextApiUpdateUnixApiResponse;


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


    static void generateLastUsedCurrencies() {
        try {
            jsonNodeLastUsedCurrencies = objectMapper.readTree(jsonFileLastUsedCurrencies);
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        lastUsedCurrFrom = jsonNodeLastUsedCurrencies.get("from").asText();
        lastUsedCurrTo = jsonNodeLastUsedCurrencies.get("to").asText();
        lastUsedCurrFromIndex = jsonNodeLastUsedCurrencies.get("from_index").asInt();
        lastUsedCurrToIndex = jsonNodeLastUsedCurrencies.get("to_index").asInt();
    }


    static void saveLastUsedCurrenciesJson() {
        LastUsedCurrenciesObjectNode = (ObjectNode) jsonNodeLastUsedCurrencies;
        LastUsedCurrenciesObjectNode.put("from", lastUsedCurrFrom);
        LastUsedCurrenciesObjectNode.put("to", lastUsedCurrTo);
        LastUsedCurrenciesObjectNode.put("from_index", lastUsedCurrFromIndex);
        LastUsedCurrenciesObjectNode.put("to_index", lastUsedCurrToIndex);

        try {
            objectMapper.writeValue(jsonFileLastUsedCurrencies, LastUsedCurrenciesObjectNode);
        }
        catch (Exception e) {System.out.println(
                "ERROR: Could not write selected currencies to JSON file.");
        }
    }


    public static boolean canGetNewApiRequest() {
        int currentTime = (int)Instant.now().getEpochSecond();
        nextApiUpdateUnixJson =  jsonNodes[0].get("time_next_update_unix").asInt();
        return currentTime > nextApiUpdateUnixJson;
    }


    public static boolean isResponseValid() {
        String result = jsonNodeApiResponse.get("result").asText();
        nextApiUpdateUnixApiResponse = jsonNodeApiResponse.get("time_next_update_unix").asInt();
        return result.equals("success") && nextApiUpdateUnixApiResponse > nextApiUpdateUnixJson;
    }


    static void reallocateAndWriteNodesToJson() {
        // Update (nodes/jsons)[1-4]
        for (int i=3; i > -1; i--) {
            jsonNodes[i+1] = jsonNodes[i];
            try {
                objectMapper.writeValue(jsonFiles[i+1], jsonNodes[i+1]);
            }
            catch (Exception e) {System.out.println(
                "ERROR: Could not reallocate previous Json files."
                );}
        }

        // Update (node/json)[0]
        try {
            jsonNodes[0] = jsonNodeApiResponse;
            objectMapper.writeValue(jsonFiles[0], jsonNodeApiResponse);
        }
        catch (Exception e) {System.out.println("ERROR: Could not write API response to file.");
        }
    }


    public static void appStartsGroupedActions() {
        /*
         * Checking if the already existing, latest
         * JSON(apiResponse0) is out of date
         * If yes:
         *  - Creating new API request
         *  - Saving the valid response as JSON
         *  - Reallocating the previous JSONs
         *  Generating rates, dates
         */
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        generateNodesFromJsons();

        if (canGetNewApiRequest()) {
            jsonNodeApiResponse = apiConnect.generateJsonNode();
            if (jsonNodeApiResponse != null && isResponseValid()) {
                reallocateAndWriteNodesToJson();
            }
            else {
                System.out.println("ERROR: No API response received.");
            }
        }

        generateRatesFromNodes();
        generateHistoricDatesFromNodes();
    }
}

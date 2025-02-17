package karole;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    static int historicJsonAmount = 7;
    static File[] jsonFiles = new File[historicJsonAmount];
    static JsonNode[] jsonNodes = new JsonNode[historicJsonAmount];
    static double[] ratesFrom = new double[historicJsonAmount];
    static double[] ratesTo = new double[historicJsonAmount];
    static String[] timeLastUpdateUtc = new String[historicJsonAmount];

    static int nextApiUpdateUnixJson;
    static int nextApiUpdateUnixApiResponse;

    static Logger logger = LogToFile.logger;


    static void generateNodesFromJsons() {
        for (int i = 0; i < historicJsonAmount; i++) {
            String jsonPath = "./app/src/main/resources/apiResponse" + i  + ".json";    
            jsonFiles[i] = new File(jsonPath);
            logger.info(jsonPath);
            try {
                jsonNodes[i] = objectMapper.readTree(jsonFiles[i]);
            } catch (IOException e) {logger.log(Level.WARNING,"Read tree-file");}
        } 
    }


    static void generateRatesFromNodes() {
        for (int i = 0; i < historicJsonAmount; i++) {
            ratesFrom[i] = jsonNodes[i].get("rates").get(lastUsedCurrFrom).asDouble();
            ratesTo[i] = jsonNodes[i].get("rates").get(lastUsedCurrTo).asDouble();
        }
    }


    static void generateHistoricDatesFromNodes() {
        for (int i = 0; i < historicJsonAmount; i++) {
            String utc = jsonNodes[i].get("time_last_update_utc").asText();   
            String utcToAdd = utc.substring(4,7)+
                "/"+
                utc.substring(8,11)+
                "/"+
                utc.substring(14,16)+
                ": ";
            timeLastUpdateUtc[i] = utcToAdd;
            logger.info(utc + " >> " + utcToAdd);
        }
    }


    static void generateLastUsedCurrencies() {
        try {
            jsonNodeLastUsedCurrencies = objectMapper.readTree(jsonFileLastUsedCurrencies);
        } catch (IOException e) {logger.log(Level.WARNING, "Read tree-file");}
        lastUsedCurrFrom = jsonNodeLastUsedCurrencies.get("from").asText();
        lastUsedCurrTo = jsonNodeLastUsedCurrencies.get("to").asText();
        lastUsedCurrFromIndex = jsonNodeLastUsedCurrencies.get("from_index").asInt();
        lastUsedCurrToIndex = jsonNodeLastUsedCurrencies.get("to_index").asInt();
        logger.info("Generated");
    }


    static void saveLastUsedCurrenciesJson() {
        LastUsedCurrenciesObjectNode = (ObjectNode) jsonNodeLastUsedCurrencies;
        LastUsedCurrenciesObjectNode.put("from", lastUsedCurrFrom);
        LastUsedCurrenciesObjectNode.put("to", lastUsedCurrTo);
        LastUsedCurrenciesObjectNode.put("from_index", lastUsedCurrFromIndex);
        LastUsedCurrenciesObjectNode.put("to_index", lastUsedCurrToIndex);

        try {
            objectMapper.writeValue(jsonFileLastUsedCurrencies, LastUsedCurrenciesObjectNode);
            logger.info("Saved");
        } catch (Exception e) {
            logger.log(Level.WARNING,"Could not write selected currencies to JSON file.");
        }
    }

    public static boolean canGetNewApiRequest() {
        // 3600: after passing the "time_next_update_unix" the latest
        // update/json not provided immediately hence +1hr delay
        int currentTime = (int)Instant.now().getEpochSecond();
        nextApiUpdateUnixJson =  jsonNodes[0].get("time_next_update_unix").asInt() + 3600;
        boolean result = currentTime > nextApiUpdateUnixJson;
        logger.info(Boolean.toString(result));
        return result;
    }


    public static boolean isResponseValid() {
        String jsonResult = jsonNodeApiResponse.get("result").asText();
        nextApiUpdateUnixApiResponse = jsonNodeApiResponse.get("time_next_update_unix").asInt();
        boolean result = jsonResult.equals("success") && nextApiUpdateUnixApiResponse > nextApiUpdateUnixJson;
        logger.info(Boolean.toString(result));
        return result;
    }



    static void reallocateAndWriteNodesToJson() {
        // Update (nodes/jsons)[1-6]
        for (int i=historicJsonAmount-2; i > -1; i--) {
            jsonNodes[i+1] = jsonNodes[i];
            try {
                objectMapper.writeValue(jsonFiles[i+1], jsonNodes[i+1]);
                logger.info("Json: " + Integer.toString(i) + " >> " + Integer.toString(i+1));
            }
            catch (Exception e) {
                logger.log(Level.WARNING, "Could not reallocate previous Json files.");
            }
        }

        // Update (node/json)[0]
        try {
            jsonNodes[0] = jsonNodeApiResponse;
            objectMapper.writeValue(jsonFiles[0], jsonNodeApiResponse);
            logger.info("Json: new >> 0");
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Could not write API response to file.");
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
        logger.info("appStartsGroupedActions(): START");
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        generateNodesFromJsons();

        if (canGetNewApiRequest()) {
            jsonNodeApiResponse = apiConnect.generateJsonNode();
            if (jsonNodeApiResponse != null && isResponseValid()) {
                reallocateAndWriteNodesToJson();
            }
            else {
                logger.log(Level.WARNING,"No API response received.");
            }
        }

        generateRatesFromNodes();
        generateHistoricDatesFromNodes();
        logger.info("appStartsGroupedActions(): FINISH");
    }
}

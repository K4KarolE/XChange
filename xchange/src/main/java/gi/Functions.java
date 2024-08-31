package gi;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class Functions {

    static apiConnect apiCall = new apiConnect();
    static JsonNode jsonNodeApiResponse;
    static HashMap<String,String> mapJsonApiResponsString;
    static HashMap<String,Integer> mapJsonApiResponsInteger;
    static HashMap<String,HashMap<String,Double>> mapRatesApiRespons;

    static JsonNode jsonNodeLastUsedCurrencies;
    static HashMap<String,String> mapJsonLastUsedCurrencies;
    static String lastUsedCurrFrom;
    static String lastUsedCurrTo;
    
    
    static JsonNode jsonNodeJSON;
    static HashMap<String,String> mapJsonDetailsString;
    static HashMap<String,Integer> mapJsonDetailsInteger;
    static HashMap<String,HashMap<String,Double>> mapRates;

    static File jsonFileApi = new File("./docs/apiResponse.json");
    static File jsonFileLastUsedCurrencies = new File("./docs/lastUsedCurrencies.json");
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
    static HashMap<String,String> getMapLastUsedCurrencies() {
        return objectMapper.convertValue(jsonNodeLastUsedCurrencies, HashMap.class);
    }


    static void getLastUsedCurrencies() {
        jsonNodeLastUsedCurrencies = generateJsonNode(jsonFileLastUsedCurrencies, jsonNodeLastUsedCurrencies);
        mapJsonLastUsedCurrencies = getMapLastUsedCurrencies();
        lastUsedCurrFrom = mapJsonLastUsedCurrencies.get("from");
        lastUsedCurrTo = mapJsonLastUsedCurrencies.get("to");
    }




    /*
     * MAPS CREATION FROM SAVED JSON
     */
    static HashMap<String,String> getMapJsonDetailsString(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, HashMap.class);
    }


    static HashMap<String,Integer> getMapJsonDetailsInteger(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, HashMap.class);
    }


    static HashMap<String,HashMap<String,Double>> getMapRates(JsonNode jsonNode) {
        return objectMapper.convertValue(jsonNode, HashMap.class);
    }


    public static void generateJsonMaps() {
        jsonNodeJSON = generateJsonNode(jsonFileApi, jsonNodeJSON);
        mapJsonDetailsString = getMapJsonDetailsString(jsonNodeJSON);
        mapJsonDetailsInteger = getMapJsonDetailsInteger(jsonNodeJSON);
        mapRates = getMapRates(jsonNodeJSON);
    }

    
    public static String lastUpdateTimeUtc() {
        String fullTime = mapJsonDetailsString.get("time_last_update_utc");
        return fullTime.substring(0, 25);
    }


    public static Integer nextUpdateTimeUnix() {
        return mapJsonDetailsInteger.get("time_next_update_unix");
    }


    public static Double getRate(String currencyCode) {
        return mapRates.get("rates").get(currencyCode);
    }


    public static boolean canGetNewApiRequest() {
        Integer currentTime = (int)Instant.now().getEpochSecond();
        Integer nextApiUpdate =  nextUpdateTimeUnix();
        
        if (currentTime > nextApiUpdate) {
            return true;
        }
        else {
            return false;
        }
    }


    /*
     *  API RESPONSE
     */
    public static void generateApiResponseMaps() {
        mapJsonApiResponsString = getMapJsonDetailsString(jsonNodeApiResponse);
        mapJsonApiResponsInteger = getMapJsonDetailsInteger(jsonNodeApiResponse);
        mapRatesApiRespons = getMapRates(jsonNodeApiResponse);
    }


    public static boolean isResponseValid() {
        String result = mapJsonApiResponsString.get("result");
        if (result.equals("success")) {
            return true;
        }
        else {
            return false;
        }
    }



    public static void main(String[] args) {

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        generateJsonMaps();


        if (canGetNewApiRequest()) {
            jsonNodeApiResponse = apiCall.generateJsonNode();
            
            if (jsonNodeApiResponse != null) {
                generateApiResponseMaps();
            }

            if (isResponseValid()) {
                try {
                    objectMapper.writeValue(jsonFileApi, jsonNodeApiResponse);
                    generateJsonMaps();
                }
                catch (Exception e) {System.out.println("ERROR: Could not write API response to file.");}
            }
        }
    }

}

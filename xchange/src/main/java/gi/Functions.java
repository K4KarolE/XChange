package gi;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Functions {

    static JsonNode jsonNodeLastUsedCurrencies;
    static HashMap<String,String> mapJsonLastUsedCurrencies;
    static String lastUsedCurrFrom;
    static String lastUsedCurrTo;
    
    
    static JsonNode jsonNodeApi;
    static HashMap<String,String> mapJsonDetailsString;
    static HashMap<String,Integer> mapJsonDetailsInteger;
    static HashMap<String,HashMap<String,Double>> mapRates;

    static File jsonFileApi = new File("./docs/learning/apiResponse.json");
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
     * MAPS CREATION FROM API/JSON
     */
    static HashMap<String,String> getMapJsonDetailsString() {
        return objectMapper.convertValue(jsonNodeApi, HashMap.class);
    }


    static HashMap<String,Integer> getMapJsonDetailsInteger() {
        return objectMapper.convertValue(jsonNodeApi, HashMap.class);
    }


    static HashMap<String,HashMap<String,Double>> getMapRates() {
        return objectMapper.convertValue(jsonNodeApi, HashMap.class);
    }


    public static void generateApiMaps() {
        jsonNodeApi = generateJsonNode(jsonFileApi, jsonNodeApi);
        mapJsonDetailsString = getMapJsonDetailsString();
        mapJsonDetailsInteger = getMapJsonDetailsInteger();
        mapRates = getMapRates();
    }


    public static boolean isResponseValid() {
        String result = mapJsonDetailsString.get("result");
        if (result.equals("success")) {
            return true;
        }
        else {
            return false;
        }
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



    public static void main(String[] args) {

        generateApiMaps();
        getLastUsedCurrencies();

        System.out.println(lastUsedCurrFrom);
        System.out.println(getRate("HUF"));
        System.out.println(lastUpdateTimeUtc());
        
    }

}

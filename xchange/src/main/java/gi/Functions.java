package gi;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Functions {

    static JsonNode jsonNode;
    static HashMap<String,String> mapJsonDetails;
    static HashMap<String,Integer> mapJsonDetailsInteger;
    static HashMap<String,HashMap<String,Double>> mapRates;

    static File jsonFileResponse = new File("./docs/learning/apiResponseExample.json");
    static File jsonFileTime = new File("./xchange/src/main/java/gi/lastApiRequestTime.json");
    static ObjectMapper objectMapper = new ObjectMapper();


    public static HashMap<String,String> getMapJsonDetails() {
        try {
            jsonNode = objectMapper.readTree(jsonFileResponse);
            mapJsonDetails = objectMapper.convertValue(jsonNode, HashMap.class);
            
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return mapJsonDetails;
    }


    public static HashMap<String,Integer> getMapJsonDetailsInteger() {
        try {
            jsonNode = objectMapper.readTree(jsonFileResponse);
            mapJsonDetailsInteger = objectMapper.convertValue(jsonNode, HashMap.class);
            
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return mapJsonDetailsInteger;
    }


    public static HashMap<String,HashMap<String,Double>> getMapRates() {
        try {
            jsonNode = objectMapper.readTree(jsonFileResponse);
            mapRates = objectMapper.convertValue(jsonNode, HashMap.class);
            
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return mapRates;
    }


    public static boolean isResponseValid() {
        String result = getMapJsonDetails().get("result");
        if (result.equals("success")) {
            return true;
        }
        else {
            return false;
        }
    }


    public static String lastUpdateTimeUtc() {
        String fullTime = getMapJsonDetails().get("time_last_update_utc");
        return fullTime.substring(0, 25);
    }


    public static Integer lastUpdateTimeUnix() {
        return getMapJsonDetailsInteger().get("time_last_update_unix");
    }


    public static Double getRate(String currencyCode) {
        return getMapRates().get("rates").get(currencyCode);
    }


    public static boolean canGetNewApiRequest() {
        // Check if 24hrs passed since last update of rates
        Integer currentTime = (int)Instant.now().getEpochSecond();
        Integer timeDiff = currentTime - lastUpdateTimeUnix();
        
        if (timeDiff > 86400) {
            return true;
        }
        else {
            return false;
        }
    }


    public static void main(String[] args) {

        // System.out.println(getMapJsonDetails());
        // getMapRates();
        // System.out.println(isResponseValid());
        // System.out.println(lastUpdateTimeUtc());
        // System.out.println(getRate("EUR"));
        System.out.println(canGetNewApiRequest());
        
    }

}

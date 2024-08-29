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

    static File jsonFile = new File("./docs/learning/apiResponse.json");
    static ObjectMapper objectMapper = new ObjectMapper();


    public static HashMap<String,String> getMapJsonDetailsString() {
        try {
            jsonNode = objectMapper.readTree(jsonFile);
            mapJsonDetails = objectMapper.convertValue(jsonNode, HashMap.class);
            
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return mapJsonDetails;
    }


    public static HashMap<String,Integer> getMapJsonDetailsInteger() {
        try {
            jsonNode = objectMapper.readTree(jsonFile);
            mapJsonDetailsInteger = objectMapper.convertValue(jsonNode, HashMap.class);
            
        } catch (IOException e) {System.out.println("ERROR: Read tree-file");}
        
        return mapJsonDetailsInteger;
    }


    public static HashMap<String,HashMap<String,Double>> getMapRates() {
        try {
            jsonNode = objectMapper.readTree(jsonFile);
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


    public static Integer nextUpdateTimeUnix() {
        return getMapJsonDetailsInteger().get("time_next_update_unix");
    }


    public static Double getRate(String currencyCode) {
        return getMapRates().get("rates").get(currencyCode);
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

        // System.out.println(getMapJsonDetails());
        // getMapRates();
        // System.out.println(isResponseValid());
        // System.out.println(lastUpdateTimeUtc());
        // System.out.println(getRate("EUR"));
        System.out.println(canGetNewApiRequest());
        
    }

}

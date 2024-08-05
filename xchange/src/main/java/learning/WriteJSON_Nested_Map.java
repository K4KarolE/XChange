/* 
    - for homogen, one deep json
    - if it is mixed (1, 2, .. deep) the mapping is not suitable
    - json file will be saved with an descending keys: 
        "key3":{},
        "key2":{},
        "key":{}
 */

package learning;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class WriteJSON_Nested_Map {

    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // to readable format

        File file = new File("./docs/learning/vehicle.json");
        
        
        try {
            // READ FILE - CONVERT JSONNODE TO MAP            
            JsonNode jsonNode = objectMapper.readTree(file);
            HashMap<String, HashMap<String, String>> resultMap = objectMapper.convertValue(jsonNode, HashMap.class);
            System.out.println("\n" + resultMap);


            // UPDATE THE MAP
            String car = resultMap.get("type").get("car");
            String newCarTitle = car + "+";

            System.out.println(car + "  >>  " +  newCarTitle);
            resultMap.get("type").put("car", newCarTitle);
    

            // CONVERT MAP BACK TO JSONNODE - SAVE TO FILE
            JsonNode tosave = objectMapper.convertValue(resultMap, JsonNode.class);
            objectMapper.writeValue(file, tosave);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/* 
    Cheers Jakob!
    https://jenkov.com/tutorials/java-json/jackson-objectmapper.html#jackson-json-tree-model

    READING UNMAPPED JSON AND STRING
 */

package karole;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReadJSON_Nested_JsonNode {

    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();
        
        File file = new File("./learning/src/main/resources/vehicles.json");
        
        String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
        "  \"nestedObject\" : { \"field\" : \"value\" } }";
        
        try {
            // FROM JSON FILE
            JsonNode nodeFromFile = objectMapper.readTree(file);
            System.out.println(nodeFromFile.get("type1"));
            System.out.println(nodeFromFile.get("type1").get("car"));

            System.out.println("\n\n");

            // FROM STRING
            JsonNode nodeFromString = objectMapper.readTree(carJson);
            System.out.println(nodeFromString.get("brand"));
            System.out.println(nodeFromString.get("doors"));
            System.out.println(nodeFromString.get("nestedObject"));
            System.out.println(nodeFromString.get("nestedObject").get("field"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
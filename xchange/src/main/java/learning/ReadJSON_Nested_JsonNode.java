/* 
    Cheers Jakob!
    https://jenkov.com/tutorials/java-json/jackson-objectmapper.html#jackson-json-tree-model
 */

package learning;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReadJSON_Nested_JsonNode {

    static String brand = "";

    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();
        
        File file = new File("./docs/learning/vehicle.json");
        
        String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
        "  \"nestedObject\" : { \"field\" : \"value\" } }";
        
        try {
            JsonNode nodeFromFile = objectMapper.readTree(file);
            System.out.println(nodeFromFile.get("type"));
            JsonNode types = nodeFromFile.get("type");
            System.out.println(types.get("car").asText() + "\n");


            JsonNode jsonNode = objectMapper.readTree(carJson);

            JsonNode brandNode = jsonNode.get("brand");
            String brand = brandNode.asText();
            System.out.println("brand = " + brand);
        
            JsonNode doorsNode = jsonNode.get("doors");
            int doors = doorsNode.asInt();
            System.out.println("doors = " + doors);
        
            JsonNode array = jsonNode.get("owners");
            JsonNode jsonNode_a = array.get(0);
            String john = jsonNode_a.asText();
            System.out.println("owner  = " + john);
        
            JsonNode child = jsonNode.get("nestedObject");
            JsonNode childField = child.get("field");
            String field = childField.asText();
            System.out.println("field = " + field);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(brand);
    }
}
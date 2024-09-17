/*
 * Using the Class_Vehicles to load the data from resources / vehicles.json
 */

package karole;

 import java.io.File;
 import java.io.IOException;
 
 import com.fasterxml.jackson.databind.DeserializationFeature;
 import com.fasterxml.jackson.databind.ObjectMapper; 
 
 public class ReadJSON_Nested_Annotation 
 {
    static Class_Vehicles vehicles = new Class_Vehicles();

    static int printCounter = 1;

    static void printCarProps () {
        System.out.println("# " + printCounter);
        System.out.println(vehicles.getCar());
        System.out.println(vehicles.getMoped() + "\n");
        printCounter++;
    }


    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();

        // Avoid unmatched JSON-Car class field(s): randomInfo
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File file = new File("./learning/src/main/resources/vehicles.json");
        
        try {
            printCarProps();
            vehicles = objectMapper.readValue(file, Class_Vehicles.class);
            printCarProps();
        }
        catch (IOException e) {     
            e.printStackTrace();
        }
    }
}
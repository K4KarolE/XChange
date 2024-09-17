/* 
    Cheers Jakob!
    https://jenkov.com/tutorials/java-json/jackson-objectmapper.html

    Using the Class_Car class to read and write resources / car.json
 */

package karole;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class WriteJSON 
{
    static Class_Car car = new Class_Car();

    static int printCounter = 1;

    static void printCarProps () {
        System.out.println("# " + printCounter);
        System.out.println(car.getBrand());
        System.out.println(car.getEngineSize() + "\n");
        printCounter++;
    }


    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();

        // Avoid unmatched JSON-Car class field(s): "randomInfo"
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // JSON INDENTION 
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println("Original Class_Car class values");
        printCarProps();
        
        File file = new File("./learning/src/main/resources/car.json");
        
        // READ
        try {
            car = objectMapper.readValue(file, Class_Car.class);
            System.out.println("Updated Class values from JSON");
            printCarProps();
        }
        catch (IOException e) {
            e.printStackTrace();     
        }
        
        // WRITE
        // Unmapped fields will be removed from JSON: "randomInfo"
        try {
            car.setBrand(car.getBrand() + "+");
            car.setEngineSize(car.getEngineSize() + 1);
            objectMapper.writeValue(file, car);
        }
        catch (IOException e) {
            e.printStackTrace();     
        }
        System.out.println("New Class values + JSON updated");
        printCarProps();
    }
}
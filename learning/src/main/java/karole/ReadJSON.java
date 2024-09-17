/* 
    Cheers Jakob!
    https://jenkov.com/tutorials/java-json/jackson-objectmapper.html

    Using the Car.class to load the data from resources / car.json
 */

package karole;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReadJSON 
{
    static Class_Car car = new Class_Car();
    static ObjectMapper objectMapper = new ObjectMapper();
    static int printCounter = 1;

    static void printCarProps () {
        System.out.println("# " + printCounter);
        System.out.println(car.getBrand());
        System.out.println(car.getEngineSize() + "\n");
        printCounter++;
    }


    public static void main( String[] args )
    {   

        // Avoid unmatched JSON-Car class field(s): randomInfo
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        File file = new File("./learning/src/main/resources/car.json");
        
        try {
            printCarProps();    // before reading json and load to the class
            car = objectMapper.readValue(file, Class_Car.class);
            printCarProps();    // after reading json and load to the class
        }
        catch (IOException e) {     
            e.printStackTrace();
        }
    }
}
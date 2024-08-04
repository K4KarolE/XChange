/* 
    Cheers Jakob!
    https://jenkov.com/tutorials/java-json/jackson-objectmapper.html
 */

package learning;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReadJSON 
{
    static Car car = new Car();

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

        // Avoid unmatched JSON-Car class field(s): randomInfo
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        printCarProps();

        File file = new File("./docs/learning/car.json");
        
        try {
            car = objectMapper.readValue(file, Car.class);
            printCarProps();
        }
        catch (IOException e) {     
            e.printStackTrace();
        }

        printCarProps();
    }
}
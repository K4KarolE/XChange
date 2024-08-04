package gi;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import learning.Car;


public class App 
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
        
        try {
            File file = new File("D:\\_DEV\\Java\\XChange\\xchange\\src\\main\\java\\learning\\cars.json");
            car = objectMapper.readValue(file, Car.class);
            printCarProps();
        }
        catch (IOException e) {     
            e.printStackTrace();     
        }

        printCarProps();
    }
}
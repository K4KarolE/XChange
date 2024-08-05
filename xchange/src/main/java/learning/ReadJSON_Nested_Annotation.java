 package learning;

 import java.io.File;
 import java.io.IOException;
 
 import com.fasterxml.jackson.databind.DeserializationFeature;
 import com.fasterxml.jackson.databind.ObjectMapper; 
 
 public class ReadJSON_Nested_Annotation 
 {
    static Vehicle car = new Vehicle();

    static int printCounter = 1;

    static void printCarProps () {
        System.out.println("# " + printCounter);
        System.out.println(car.getCar());
        System.out.println(car.getMoped() + "\n");
        printCounter++;
    }


    public static void main( String[] args )
    {   
        ObjectMapper objectMapper = new ObjectMapper();

        // Avoid unmatched JSON-Car class field(s): randomInfo
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        printCarProps();

        File file = new File("./docs/learning/vehicle.json");
        
        try {
            car = objectMapper.readValue(file, Vehicle.class);
            printCarProps();
        }
        catch (IOException e) {     
            e.printStackTrace();
        }

        printCarProps();
    }
}
/*
 * Cheers lads!
 * https://stackoverflow.com/questions/37010891/how-to-map-a-nested-value-to-a-property-using-jackson-annotations
 *
 * Class used in the ReadJSON_Nested_Annotation to load data from resources / vehicles.json
 */

package karole;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Class_Vehicles {
    private String car = null;
    private String moped = null;


    @JsonProperty("type2")  // type(1-3) keys in vehicles.json
    public void getValues(HashMap<String, String> type) {
        car = type.get("car");
        moped = type.get("moped");
    }


    public String getCar() {return this.car;}

    public String getMoped() {return this.moped;}
}

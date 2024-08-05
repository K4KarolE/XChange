/*
 * Cheers lads!
 * https://stackoverflow.com/questions/37010891/how-to-map-a-nested-value-to-a-property-using-jackson-annotations
 */

package learning;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vehicle {
    private String car = null;
    private String moped = null;

    @JsonProperty("type")
    public void getValues(HashMap<String, String> type) {
        car = type.get("car");
        moped = type.get("moped");
    }

    public String getCar() {return this.car;}

    public String getMoped() {return this.moped;}
}

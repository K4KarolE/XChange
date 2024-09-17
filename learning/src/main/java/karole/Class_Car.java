/*
    Class used in the ReadJSON to parse resources / car.json
 */

package karole;

public class Class_Car {
    private String brand = null;
    private float engineSize = 0;

    public String getBrand() { return this.brand; }
    public float getEngineSize() { return this.engineSize; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setEngineSize(float engineSize) { this.engineSize = engineSize; }
}


/**
 * The daisy in this world.
 */
public class Daisy {

    private DaisyType type;
    //the albedo of daisy
    private double albedo;
    //the age of daisy
    private int age;

    public Daisy(DaisyType type, double albedo, int age){
        this.type = type;
        this.albedo = albedo;
        this.age = age;
    }

    public DaisyType getType() {
        return type;
    }

    public void setType(DaisyType type) {
        this.type = type;
    }

    //get the age of daisy
    public int getAge() {
        return age;
    }

    public void growOld() {
        age ++;
    }

    //get the albedo of daisy
    public double getAlbedo() {
        return albedo;
    }

    //set the albedo of daisy
    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public enum DaisyType {BLACK, WHITE}
}

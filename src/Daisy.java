/**
 * The daisy in this world.
 */
public class Daisy {

    private daisyType type;
    //the albedo of daisy
    private double albedo;
    //the age of daisy
    private int age;

    public Daisy( daisyType type, double albedo, int age){
        this.type = type;
        this.albedo = albedo;
        this.age = age;
    }

    public daisyType getType() {
        return type;
    }

    public void setType(daisyType type) {
        this.type = type;
    }

    //get the age of daisy
    public int getAge() {
        return age;
    }

    public void growOld() {
        age --;
    }

    //get the albedo of daisy
    public double getAlbedo() {
        return albedo;
    }

    //set the albedo of daisy
    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public enum daisyType {BLACK, WHITE}
}

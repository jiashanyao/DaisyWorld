/**
 * Daisies in the world. They are of two types: black and white.
 * They differ in albedo. White daisies have a high surface albedo and thus reflect light and heat,
 * thus cooling the area around them. Black daisies have a low surface albedo and thus absorb light and heat,
 * thus heating the area around them. Daisies with maximum age die.
 */
public class Daisy {

    // Type of daisy
    private DaisyType type;
    // Albedo of daisy
    private double albedo;
    // Age of daisy
    private int age;

    public Daisy(DaisyType type, double albedo, int age){
        this.type = type;
        this.albedo = albedo;
        this.age = age;
    }

    public DaisyType getType() {
        return type;
    }

    public int getAge() {
        return age;
    }

    /**
     * Daisy ages one year
     */
    public void growOld() {
        age ++;
    }

    public double getAlbedo() {
        return albedo;
    }

    /**
     * Enumerate DaisyType as BLACK and WHITE
     */
    public enum DaisyType {BLACK, WHITE}
}

/**
 * The simulation for the daisy world
 */
public class Simulation {
    public static void main(String[] args) {
        String scenario = args[0];

        double initialPercent = Params.START_BLACK + Params.START_WHITE;

        // it is invalid that the initial percent of all daisies
        // is over 100%
        if (initialPercent > 100) {
            System.out.println("Invalid!");
        }
        else {
            DaisyWorld earth = new DaisyWorld();
            earth.run();
        }
    }
}

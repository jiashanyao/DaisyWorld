/**
 * The simulation for the daisy world
 */
public class Simulation {
    public static void main(String args[]) {
        String scenario = args[0];
        double solarLuminosity = 0;

        switch (scenario) {
            case "ramp": {
                solarLuminosity = Params.RAMP;
                break;
            }
            case "low": {
                solarLuminosity = Params.LOW;
                break;
            }
            case "our": {
                solarLuminosity = Params.OUR;
                break;
            }
            case "high": {
                solarLuminosity = Params.HIGH;
                break;
            }
        }

        double initialPercent = Params.START_BLACK + Params.START_WHITE;

        // it is invalid that the initial percent of all daisies
        // is over 100%
        if (initialPercent > 100) {
            System.out.println("Invalid!");
        }
        else {
            DaisyWorld earth = new DaisyWorld(Params.START_BLACK, Params.START_WHITE,
                    Params.ALBEDO_OF_BLACk, Params.ALBEDO_OF_WHITE, solarLuminosity,
                    scenario);
            earth.run();
        }
    }
}

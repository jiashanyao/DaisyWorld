/**
 * DaisyWorld is a world filled with two different types of daisies:
 * black daisies and white daisies. They differ in albedo, which is
 * how much energy they absorb as heat from sunlight.
 */
public class DaisyWorld {
    // start percentage of daisies
    private double startBlacks;
    private double startWhites;

    // the albedo of daisies
    private double albedoOfBlack;
    private double albedoOfWhite;

    private double solarLuminosity;

    private String scenario;

    private double [][] grid;

    // initialize the daisy world
    public DaisyWorld(double startBlacks, double startWhites,
            double albedoOfBlack, double albedoOfWhite,
            double solarLuminosity, String scenario) {
        this.startBlacks = startBlacks;
        this.startWhites = startWhites;
        this.albedoOfBlack = albedoOfBlack;
        this.albedoOfWhite = albedoOfWhite;
        this.solarLuminosity = solarLuminosity;
        this.scenario = scenario;
        this.grid = new double[Params.EDGE][Params.EDGE];
    }

    public void run() {
        while (true);
    }
}

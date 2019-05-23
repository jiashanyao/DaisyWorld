import java.util.LinkedList;
import java.util.Random;

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

    private Patch [][] grid;

    // initialize the daisy world
    DaisyWorld() {
        this.startBlacks = Params.START_BLACK;
        this.startWhites = Params.START_WHITE;
        this.albedoOfBlack = Params.ALBEDO_OF_BLACk;
        this.albedoOfWhite = Params.ALBEDO_OF_WHITE;
        this.solarLuminosity = Params.SOLAR_LUMINOSITY;
        this.grid = new Patch[Params.EDGE][Params.EDGE];
        // Initialize grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] = new Patch();
            }
        }
    }

    private void seedDaisies(Patch[][] grid, double percentOfBlack, double percentOfWhite) {
        LinkedList<int[]> remainingSpace = new LinkedList<>();
        //initialize the remainingSpace as a list of 2-int-arrays, each of which represents a patch coordinate in the grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                remainingSpace.add(new int[]{i, j});
            }
        }

        int numberOfSpaces = Params.EDGE * Params.EDGE;
        int numOfBlack = (int) (percentOfBlack * 0.01 * numberOfSpaces);
        int numOfWhite = (int) (percentOfWhite * 0.01 * numberOfSpaces);
        randomlySeed(grid, remainingSpace, numOfBlack, Daisy.daisyType.BLACK, albedoOfBlack);
        randomlySeed(grid, remainingSpace, numOfWhite, Daisy.daisyType.WHITE, albedoOfWhite);
    }

    private void randomlySeed(Patch[][] grid, LinkedList<int[]> remainingSpace,  int numberOfDaisies, Daisy.daisyType type, double albedo) {
        Random random = new Random();
        while (numberOfDaisies > 0) {
            int maxRandom = remainingSpace.size();
            int chosenIndex = random.nextInt(maxRandom);
            int[] addingIndex = remainingSpace.remove(chosenIndex);
            int age = random.nextInt(Params.MAX_AGE) + 1;
            grid[addingIndex[0]][addingIndex[1]].setDaisy(new Daisy(type, albedo, age));
            numberOfDaisies--;
        }
    }

    private void tick() {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j].calTemp(solarLuminosity);
            }
        }
        Diffusion.diffuse(grid, Params.DIFFUSION_RATIO);
        //TODO: daisies die and regenerate
    }

    public void run() {
        seedDaisies(grid, Params.START_BLACK, Params.START_WHITE);
        for (int t = 0; t < Params.TICKS; t++) {
            tick();
        }
    }
    public static void main(String[] args) {
        DaisyWorld earth = new DaisyWorld();
        earth.run();
        Patch[][] grid = earth.grid;
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                System.out.printf("%.2f ", grid[i][j].getTemperature());
            }
            System.out.println();
        }
    }
}

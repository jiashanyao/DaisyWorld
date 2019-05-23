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

    private String scenario;

    private Patch [][] grid;

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
        this.grid = new Patch[Params.EDGE][Params.EDGE];
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
        //type 1 = black flower, type 2 = white flower
        randomlySeed(grid, remainingSpace, numOfBlack, Daisy.daisyType.BLACK, Params.ALBEDO_OF_BLACk);
        randomlySeed(grid, remainingSpace, numOfWhite, Daisy.daisyType.WHITE, Params.ALBEDO_OF_WHITE);
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

    public static void main(String[] args) {
        DaisyWorld earth = new DaisyWorld(Params.START_BLACK, Params.START_WHITE, Params.ALBEDO_OF_BLACk, Params.ALBEDO_OF_WHITE, Params.OUR, "");
        earth.seedDaisies(earth.grid, Params.START_BLACK, Params.START_WHITE);
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                if (earth.grid[i][j].getDaisy() != null)
                    System.out.print(earth.grid[i][j].getDaisy().getType() + " " + earth.grid[i][j].getDaisy().getAge() + " ");
                else
                    System.out.print("   Null   ");
            }
            System.out.println();
        }
    }
}

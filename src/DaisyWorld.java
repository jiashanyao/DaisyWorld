import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is the main class. Run the main method of this class to run the model and get results
 * generated.
 *
 * DaisyWorld is a world filled with two different types of daisies: black and white.
 * The basis of the world is a grid of patches. Each of the patches may or may not have a daisy
 * grown. The world runs as a discrete fashion tick by tick. At each tick, daisies absorb light
 * and heat, patches diffuse temperature, daisies die and reproduce, metrics are recorded.
 *
 * Results are printed and written to a csv file after the model run finishes.
 */
public class DaisyWorld {

    // A grid of patches
    private Patch[][] grid;

    // A list of global temperature for every tick
    private ArrayList<Double> globalTempRecord;

    // A list of black population for every tick
    private ArrayList<Integer> blackPopulation;

    // A list of white population for every tick
    private ArrayList<Integer> whitePopulation;

    // A list of global soil quality for every tick
    private ArrayList<Double> globalSoilQualityRecord;

    // Initialize the daisy world
    public DaisyWorld() {
        grid = new Patch[Params.EDGE][Params.EDGE];
        // Initialize grid
        if (Params.QUALITY_SWITCH == 0) {
            for (int i = 0; i < Params.EDGE; i++) {
                for (int j = 0; j < Params.EDGE; j++) {
                    grid[i][j] = new Patch();
                }
            }
        } else {
            // If the switch of extension is on, also initialize the quality attribute
            Random rand = new Random();
            for (int i = 0; i < Params.EDGE; i++) {
                for (int j = 0; j < Params.EDGE; j++) {
                    grid[i][j] = new Patch();
                    grid[i][j].setQuality(rand.nextDouble() * Params.INITIAL_MAX_QUALITY);
                }
            }
            // Diffuse for 20 rounds after randomly initializing soil quality to maintain
            // the gradual change in soil quality
            for (int i = 0; i < 20; i++) Util.diffuseSoilQuality(grid, Params.DIFFUSION_RATIO);
        }

        // Initialize global temperature and black and white population record
        globalTempRecord = new ArrayList<>();
        blackPopulation = new ArrayList<>();
        whitePopulation = new ArrayList<>();
        globalSoilQualityRecord = new ArrayList<>();
        // Seed daisies
        seedDaisies(grid, Params.START_BLACK, Params.START_WHITE);
        // First round absorb and recording results
        absorb();
        recordGlobalTemp();
        recordPopulation();
        recordGlobalSoilQuality();
    }

    public ArrayList<Double> getGlobalTempRecord() {
        return globalTempRecord;
    }

    public ArrayList<Integer> getBlackPopulation() {
        return blackPopulation;
    }

    public ArrayList<Integer> getWhitePopulation() {
        return whitePopulation;
    }

    public ArrayList<Double> getGlobalSoilQualityRecord() {
        return globalSoilQualityRecord;
    }

    /**
     * Randomly initialize daisies in the grid of patches with the given black and white
     * percentages.
     * @param grid A grid of patches represented by a 2-dimension array
     * @param percentOfBlack Percentage of start black daisies
     * @param percentOfWhite Percentage of start white daisies
     */
    private void seedDaisies(Patch[][] grid, double percentOfBlack, double percentOfWhite) {
        LinkedList<int[]> remainingSpace = new LinkedList<>();
        // Initialize the remainingSpace as a list of 2-int-arrays,
        // each of which represents a patch coordinate in the grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                remainingSpace.add(new int[]{i, j});
            }
        }

        int numberOfSpaces = Params.EDGE * Params.EDGE;
        int numOfBlack = (int) (percentOfBlack * numberOfSpaces);
        int numOfWhite = (int) (percentOfWhite * numberOfSpaces);
        randomlySeed(grid, remainingSpace, numOfBlack,
                Daisy.DaisyType.BLACK, Params.ALBEDO_OF_BLACk);
        randomlySeed(grid, remainingSpace, numOfWhite,
                Daisy.DaisyType.WHITE, Params.ALBEDO_OF_WHITE);
    }

    /**
     * Randomly seed one type of daisies with the remainingSpace and numberOfDaisies of that type
     * @param grid A grid of patches represented by a 2-dimension array
     * @param remainingSpace List of patches that are not occupied yet
     * @param numberOfDaisies Number of daisies to be planted
     * @param type Type of daisies to be planted
     * @param albedo Albedo of daisies to be planted
     */
    private void randomlySeed(Patch[][] grid, LinkedList<int[]> remainingSpace,
                              int numberOfDaisies, Daisy.DaisyType type, double albedo) {
        Random random = new Random();
        while (numberOfDaisies > 0) {
            int maxRandom = remainingSpace.size();
            int chosenIndex = random.nextInt(maxRandom);
            int[] addingIndex = remainingSpace.remove(chosenIndex);
            int age = random.nextInt(Params.MAX_AGE);
            grid[addingIndex[0]][addingIndex[1]].setDaisy(new Daisy(type, albedo, age));
            numberOfDaisies--;
        }
    }

    /**
     * Each patch absorbs light and heat from the sun
     */
    private void absorb() {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j].calTemp(Params.SOLAR_LUMINOSITY, Params.ALBEDO_Of_SURFACE);
            }
        }
    }

    /**
     * Record the global temperature of the current tick
     */
    private void recordGlobalTemp() {
        double totalTemperature = 0;
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                totalTemperature += grid[i][j].getTemperature();
            }
        }
        Double currentGlobalTemp = totalTemperature / (Params.EDGE * Params.EDGE);
        globalTempRecord.add(currentGlobalTemp);
    }

    /**
     * Record the global soil quality of the current tick
     */
    private void recordGlobalSoilQuality() {
        double totalSoilQuality = 0;
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                totalSoilQuality += grid[i][j].getQuality();
            }
        }
        Double currentGlobalQuality = totalSoilQuality / (Params.EDGE * Params.EDGE);
        globalSoilQualityRecord.add(currentGlobalQuality);
    }

    /**
     * Record black and white population of the current tick
     */
    private void recordPopulation() {
        int black = 0, white = 0;
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                Daisy daisy = grid[i][j].getDaisy();
                if (daisy != null) {
                    if (daisy.getType() == Daisy.DaisyType.BLACK) {
                        black++;
                    } else {
                        white++;
                    }
                }
            }
        }
        blackPopulation.add(black);
        whitePopulation.add(white);
    }

    /**
     * Each daisy ages and dies if reaches the maximum age
     */
    private void age() {
        // Age and die
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                Patch patch = grid[i][j];
                if (patch.getDaisy() != null) {
                    if (patch.getDaisy().getAge() < Params.MAX_AGE) {
                        patch.getDaisy().growOld(); // age
                    } else {
                        patch.setDaisy(null);       // die
                    }
                }
            }
        }
    }

    /**
     * At each tick, all patches absorb energy from the sun, diffuse temperature synchronously;
     * all daisies age and die if necessary, and reproduce synchronously. mode is used to check
     * if the option of soil quality extension is on.
     * @param mode whether the option of soil quality extension is on
     */
    public void tick(int mode) {
        // Absorb luminosity
        absorb();
        // Diffuse
        Util.diffuseTemperature(grid, Params.DIFFUSION_RATIO);
        //If extension is enable also change the quality of patch (Mutual affect)
        if (mode > 0) {
            Util.diffuseSoilQuality(grid, Params.DIFFUSION_RATIO);
            Util.changeQuality(grid);
        }
        // Age and check die
        age();
        // Reproduce
        Util.reproduce(grid);
        // Record global temperature and black and white population
        recordGlobalTemp();
        recordPopulation();
        recordGlobalSoilQuality();
    }

    /**
     * Print the DaisyWorld to the standard output. Black circles means black daisies,
     * white circles means white daisies, empty means no daisies. Numeric number of each patch
     * represents the temperature of that patch.
     */
    public void printGrid() {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                Patch patch = grid[i][j];
                Daisy daisy = patch.getDaisy();
                if (daisy != null) {
                    if (daisy.getType() == Daisy.DaisyType.BLACK) {
                        System.out.print("\u25CF");
                    } else {
                        System.out.print("\u25CB");
                    }
                    System.out.printf("%3.0f|", patch.getTemperature());
                } else {
                    System.out.printf(" %3.0f|", patch.getTemperature());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Main method, entry point of the program.
     * @param args Command-line input, not needed in this program.
     */
    public static void main(String[] args) {
        DaisyWorld earth = new DaisyWorld();
//        earth.printGrid();        // Uncomment to print the initial grid
        for (int t = 0; t < Params.TICKS; t++) {
            earth.tick(Params.QUALITY_SWITCH);
//            earth.printGrid();    // Uncomment to print the grid each tick
        }
        earth.printGrid();          // Print the grid of final tick
        ArrayList<Double> temp = earth.getGlobalTempRecord();
        ArrayList<Integer> black = earth.getBlackPopulation();
        ArrayList<Integer> white = earth.getWhitePopulation();
        ArrayList<Double> quality = earth.getGlobalSoilQualityRecord();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data.csv"));
            for (Integer i = 0; i <= Params.TICKS; i++) {
                if (i < Params.TICKS) {
                    writer.write(i.toString() + ',');
                } else {
                    writer.write(i.toString());
                }
            }
            writer.newLine();
            for (int i = 0; i <= Params.TICKS; i++) {
                if (i < Params.TICKS) {
                    writer.write(temp.get(i).toString() + ',');
                } else {
                    writer.write(temp.get(i).toString());
                }
            }
            writer.newLine();
            for (int i = 0; i <= Params.TICKS; i++) {
                if (i < Params.TICKS) {
                    writer.write(black.get(i).toString() + ',');
                } else {
                    writer.write(black.get(i).toString());
                }
            }
            writer.newLine();
            for (int i = 0; i <= Params.TICKS; i++) {
                if (i < Params.TICKS) {
                    writer.write(white.get(i).toString() + ',');
                } else {
                    writer.write(white.get(i).toString());
                }
            }
            writer.newLine();
            for (int i = 0; i <= Params.TICKS; i++) {
                if (i < Params.TICKS) {
                    writer.write(quality.get(i).toString() + ',');
                } else {
                    writer.write(quality.get(i).toString());
                }
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Global Temperature " + temp);
        System.out.println("Black Population " + black);
        System.out.println("White Population " + white);
        System.out.println("Global Soil Quality " + quality);
    }
}

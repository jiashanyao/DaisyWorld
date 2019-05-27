import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;

/**
 * DaisyWorld is a world filled with two different types of daisies:
 * black daisies and white daisies. They differ in albedo, which is
 * how much energy they absorb as heat from sunlight.
 */
public class DaisyWorld {

    private double startBlack;
    private double startWhite;

    private double albedoOfBlack;
    private double albedoOfWhite;
    private double albedoOfSurface;
    private double solarLuminosity;

    private Patch[][] grid;
    private ArrayList<Double> globalTempRecord;

    private ArrayList<Integer> blackPopulation;

    private ArrayList<Integer> whitePopulation;


    // initialize the daisy world
    public DaisyWorld(double startBlack, double startWhite, double albedoOfBlack,
                      double albedoOfWhite, double albedoOfSurface, double solarLuminosity) {
        this.startBlack = startBlack;
        this.startWhite = startWhite;
        this.albedoOfBlack = albedoOfBlack;
        this.albedoOfWhite = albedoOfWhite;
        this.albedoOfSurface = albedoOfSurface;
        this.solarLuminosity = solarLuminosity;
        grid = new Patch[Params.EDGE][Params.EDGE];
        // Initialize grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] = new Patch();
            }
        }
        // Initialize global temperature and black and white population record
        globalTempRecord = new ArrayList<>();
        blackPopulation = new ArrayList<>();
        whitePopulation = new ArrayList<>();
        // Seed daisies
        seedDaisies(grid, this.startBlack, this.startWhite);
        absorb();
        recordGlobalTemp();
        recordPopulation();
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

    private void seedDaisies(Patch[][] grid, double percentOfBlack, double percentOfWhite) {
        LinkedList<int[]> remainingSpace = new LinkedList<>();
        //initialize the remainingSpace as a list of 2-int-arrays, each of which represents a patch coordinate in the grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                remainingSpace.add(new int[]{i, j});
            }
        }

        int numberOfSpaces = Params.EDGE * Params.EDGE;
        int numOfBlack = (int) (percentOfBlack * numberOfSpaces);
        int numOfWhite = (int) (percentOfWhite * numberOfSpaces);
        randomlySeed(grid, remainingSpace, numOfBlack, Daisy.DaisyType.BLACK, albedoOfBlack);
        randomlySeed(grid, remainingSpace, numOfWhite, Daisy.DaisyType.WHITE, albedoOfWhite);
    }

    private void randomlySeed(Patch[][] grid, LinkedList<int[]> remainingSpace, int numberOfDaisies, Daisy.DaisyType type, double albedo) {
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

    private void absorb() {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j].calTemp(solarLuminosity, albedoOfSurface);
            }
        }
    }

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

    public void tick() {
        // Absorb luminosity
        absorb();
        // Diffuse
        Util.diffuseTemperature(grid, Params.DIFFUSION_RATIO);
        // Age and check die
        age();
        // Regenerate
        Util.reProduct(grid);
        // Record global temperature and black and white population
        recordGlobalTemp();
        recordPopulation();
    }

    public void printGrid() {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                Patch patch = grid[i][j];
                Daisy daisy = patch.getDaisy();
                if (daisy != null) {
                    if (daisy.getType() == Daisy.DaisyType.BLACK) {
                        System.out.print("\u25CF ");
                    } else {
                        System.out.print("\u25CB ");
                    }
                    System.out.printf("%6.2f |", patch.getTemperature());
                } else {
                    System.out.printf("  %6.2f |", patch.getTemperature());
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        DaisyWorld earth = new DaisyWorld(
                Params.START_BLACK, Params.START_WHITE,
                Params.ALBEDO_OF_BLACk, Params.ALBEDO_OF_WHITE,
                Params.ALBEDO_Of_SURFACE, Params.SOLAR_LUMINOSITY);
//        earth.printGrid();
        for (int t = 0; t < Params.TICKS; t++) {
            earth.tick();
//            System.out.println();
//            earth.printGrid();
        }
        ArrayList<Double> temp = earth.getGlobalTempRecord();
        ArrayList<Integer> black = earth.getBlackPopulation();
        ArrayList<Integer> white = earth.getWhitePopulation();
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Global Temperature " + temp);
        System.out.println("Black Population " + black);
        System.out.println("White Population " + white);
    }
}

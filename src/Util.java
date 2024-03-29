import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Utility class for systematic methods like diffuse() and reproduce() in accordance to
 * the NetLogo code to separate complexity of logic from the DaisyWorld class.
 */
public class Util {

    /**
     * Diffuses a ratio of the temperature of a patch to all 8 neighbours equally.
     * The patch keeps what is left. Diffusion is synchronous for all patches in a grid.
     *
     * @param grid           A grid of patches represented by a 2-dimension array
     * @param diffusionRatio Diffusion ratio
     */
    public static void diffuseTemperature(Patch[][] grid, double diffusionRatio) {
        // Initialize a grid recording the temperature change for each patch after diffusion
        double[][] gridDelta = new double[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) gridDelta[i][j] = 0;
        }
        // Calculate the temperature change for each patch
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++)
                calculateShares(grid[i][j].getTemperature(), gridDelta, i, j, diffusionRatio);
        }
        // Add the temperature change to the left temperature after diffusion
        applyTemperatureShares(gridDelta, grid, diffusionRatio);
    }

    /**
     * Diffuse a ratio of the soil quality of a patch to all 8 neighbours equally.
     * The patch keeps what is left. This is synchronous for all patches in a grid.
     * Sand patch will no longer receive soil quality from its neighbours.
     *
     * @param grid           A grid of patches represented by a 2-dimension array
     * @param diffusionRatio Diffusion ratio
     */
    public static void diffuseSoilQuality(Patch[][] grid, double diffusionRatio) {
        // Initialize a grid recording the temperature change for each patch after diffusion
        double[][] gridDelta = new double[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) gridDelta[i][j] = 0;
        }
        // Calculate the soil quality change for each patch
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++)
                calculateShares(grid[i][j].getSoilQuality(), gridDelta, i, j, diffusionRatio);
        }
        // Add the soil quality change to the left soil quality after diffusion
        applyQualityShares(gridDelta, grid, diffusionRatio);
    }

    /**
     * Calculate shares of a diffused value of a patch (x, y) for the patch's neighbours,
     * and accumulate the shares to the delta grid.
     *
     * @param patchValue     The patch value to be diffused
     * @param gridDelta      A 2-d array recording the value change for each patch after diffusion
     * @param x              x coordinate
     * @param y              y coordinate
     * @param diffusionRatio Diffusion ratio
     */
    private static void calculateShares(double patchValue, double[][] gridDelta,
                                        int x, int y, double diffusionRatio) {
        double deltaTemp = diffusionRatio / 8 * patchValue;
        gridDelta[wrap(x - 1)][wrap(y - 1)] += deltaTemp;
        gridDelta[wrap(x - 1)][wrap(y)] += deltaTemp;
        gridDelta[wrap(x - 1)][wrap(y + 1)] += deltaTemp;
        gridDelta[wrap(x)][wrap(y - 1)] += deltaTemp;
        gridDelta[wrap(x)][wrap(y + 1)] += deltaTemp;
        gridDelta[wrap(x + 1)][wrap(y - 1)] += deltaTemp;
        gridDelta[wrap(x + 1)][wrap(y)] += deltaTemp;
        gridDelta[wrap(x + 1)][wrap(y + 1)] += deltaTemp;
    }

    /**
     * Add the temperature change from delta grid to the remaining temperature in grid.
     *
     * @param gridDelta      A 2-d array recording the temperature change for each patch after diffusion
     * @param grid           A grid of patches represented by a 2-dimension array
     * @param diffusionRatio Diffusion ratio
     */
    private static void applyTemperatureShares(
            double[][] gridDelta, Patch[][] grid, double diffusionRatio) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                // New temperature is the left temperature after losing plus the temperature gained
                double newTemperature = grid[i][j].getTemperature() * (1 - diffusionRatio)
                        + gridDelta[i][j];
                grid[i][j].setTemperature(newTemperature);
            }
        }
    }

    /**
     * Add the soil quality change from delta grid to the remaining soil quality in grid.
     * Sand patch will no longer receive soil quality delta from its neighbours.
     *
     * @param gridDelta      A 2-d array recording the soil quality change for each patch after diffusion
     * @param grid           A grid of patches represented by a 2-dimension array
     * @param diffusionRatio Diffusion ratio
     */
    private static void applyQualityShares(double[][] gridDelta, Patch[][] grid,
                                           double diffusionRatio) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                double currentQuality = grid[i][j].getSoilQuality();
                //If it becomes sand, it will never go back
                double newQuality = 0;
                if (currentQuality > Params.DEATH_LINE)
                    newQuality = currentQuality * (1 - diffusionRatio) + gridDelta[i][j];
                grid[i][j].setSoilQuality(newQuality);
            }
        }
    }

    /**
     * Each daisy in the grid reproduces its next generation.
     * Reproduction happens synchronously for each daisy.
     *
     * @param grid A grid of patches represented by a 2-dimension array
     */
    @SuppressWarnings("unchecked")
    public static void reproduce(Patch[][] grid) {
        // A grid that records sprout candidates of each open patch (a list consisting black
        // and/or white daisies)
        ArrayList<Daisy>[][] sproutGrid = new ArrayList[Params.EDGE][Params.EDGE];
        // Check each patch for its regeneration
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                sprout(grid, sproutGrid, i, j, grid[i][j].getSoilQuality());
            }
        }
        // Apply the sprouted daisies to the original grid.
        // Randomly choose a baby daisy if there are more than one candidate.
        Random random = new Random();
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                if (grid[i][j].getDaisy() == null && sproutGrid[i][j] != null && grid[i][j].getSoilQuality() > 0) {
                    // Randomly chooses a baby candidate
                    Daisy babyDaisy = sproutGrid[i][j].get(random.nextInt(sproutGrid[i][j].size()));
                    grid[i][j].setDaisy(babyDaisy);
                }
            }
        }
    }

    /**
     * A daisy at patch(i, j) has a probability of reproducing a child at one of its open
     * neighbour patches. The probability is based on a parabolic function that depends on
     * the local temperature. If an open patch has more than one candidate daisy baby,
     * a random candidate will be chosen to sprout here. For example, if an open patch is
     * surrounded by 7 black daisies and 1 white daisies, it has a probability of 7/8
     * to sprout a black daisy and a probability of 1/8 to sprout a white daisy.
     *
     * @param grid       A grid of patches represented by a 2-dimension array
     * @param sproutGrid A grid of sprout candidates for each patch, an 2-d array of lists
     * @param i          i coordinate
     * @param j          j coordinate
     * @param quality    Soil quality of the patch
     */
    private static void sprout(Patch[][] grid, ArrayList<Daisy>[][] sproutGrid, int i, int j,
                               double quality) {
        double temperature = grid[i][j].getTemperature();
        double seedThreshold = (0.1457 * temperature)
                - (0.0032 * (temperature * temperature)) - 0.6443;
        Random random = new Random();
        double survivability = random.nextDouble();
        double soilAvailability = random.nextDouble();
        Daisy parent = grid[i][j].getDaisy();
        if (parent != null && survivability < seedThreshold && soilAvailability < quality) {
            LinkedList<int[]> neighbors = new LinkedList<>();
            addIfNoDaisy(neighbors, wrap(i - 1), wrap(j - 1), grid);
            addIfNoDaisy(neighbors, wrap(i - 1), wrap(j), grid);
            addIfNoDaisy(neighbors, wrap(i - 1), wrap(j + 1), grid);
            addIfNoDaisy(neighbors, wrap(i), wrap(j + 1), grid);
            addIfNoDaisy(neighbors, wrap(i + 1), wrap(j + 1), grid);
            addIfNoDaisy(neighbors, wrap(i + 1), wrap(j), grid);
            addIfNoDaisy(neighbors, wrap(i + 1), wrap(j - 1), grid);
            addIfNoDaisy(neighbors, wrap(i), wrap(j - 1), grid);
            if (neighbors.size() > 0) {
                int[] sproutCoor = neighbors.get(random.nextInt(neighbors.size()));
                if (sproutGrid[sproutCoor[0]][sproutCoor[1]] == null) {
                    sproutGrid[sproutCoor[0]][sproutCoor[1]] = new ArrayList<>();
                }
                // Baby daisy inherits from parent's type, albedo. Age is 0.
                Daisy babyDaisy = new Daisy(parent.getType(), parent.getAlbedo(), 0);
                // Adds the baby to a list of baby candidates for that patch.
                sproutGrid[sproutCoor[0]][sproutCoor[1]].add(babyDaisy);
            }
        }
    }

    /**
     * In this function the quality of soil will change according to current quality and whether
     * it is planted. The basic law is if something is good, it is easy to become better and
     * hard to be worse. But if something is bad, it is easy to become worse and hard to become better.
     * (The balance of echo system)
     *
     * @param grid The patch World represented by 2-dimensional array.
     */
    public static void changeQuality(Patch[][] grid) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                //Linear model, if the quality is good, it changed very slowly,
                // if it is bad, it changed very fast.
                double currentQuality = grid[i][j].getSoilQuality();
                double nonPrefectRate = 1 - currentQuality;
                double decreasePossible = Params.CHANGE_BASE * nonPrefectRate;
                double increasePossible = Params.CHANGE_BASE * currentQuality;
                double newQuality;
                if (grid[i][j].getDaisy() == null)
                    newQuality = Math.max(0, currentQuality - decreasePossible);
                else newQuality = Math.min(1, currentQuality + increasePossible);
                grid[i][j].setSoilQuality(newQuality);
            }
        }
    }

    /**
     * Wrap a coordinate to make the world a torus.
     *
     * @param coordinate Coordinate to be wrapped
     * @return Wrapped coordinate
     */
    private static int wrap(int coordinate) {
        if (coordinate < 0) {
            return Params.EDGE - 1;
        } else if (coordinate >= Params.EDGE) {
            return 0;
        } else {
            return coordinate;
        }
    }

    /**
     * Add {x, y} to neighbors list if patch at grid(x,y) has no daisy.
     *
     * @param neighbors List of neighbors that has no daisy
     * @param x         x coordinate
     * @param y         y coordinate
     * @param grid      A grid of patches represented by a 2-dimension array
     */
    private static void addIfNoDaisy(LinkedList<int[]> neighbors, int x, int y, Patch[][] grid) {
        if (grid[x][y].getDaisy() == null) {
            neighbors.add(new int[]{x, y});
        }
    }

}

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Util {

    public static void diffuse(Patch[][] grid, double diffusionRatio) {
        double[][] gridDelta = new double[Params.EDGE][Params.EDGE];

        //In default the temp will not change, so the change condition will be 0.
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) gridDelta[i][j] = 0;
        }

        //For each patch would be changed, implement its modification
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++)
                calculateShares(grid[i][j].getTemperature(), gridDelta, i, j, diffusionRatio);
        }
        applyShares(gridDelta, grid, diffusionRatio);
    }

    private static void calculateShares(double patchValue, double[][] gridDelta, int x, int y, double diffusionRatio) {
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

    //Firstly calculate the remaining temperature, then append modification to its remaining temperature
    private static void applyShares(double[][] gridDelta, Patch[][] grid, double diffusionRatio) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                double newTemperature = grid[i][j].getTemperature() * (1 - diffusionRatio) + gridDelta[i][j];
                grid[i][j].setTemperature(newTemperature);
            }
        }
    }

    public static void regenerate(Patch[][] grid) {
        // Records sprout candidates of an open patch (a list consisting black and/or white daisies)
        ArrayList<Daisy>[][] sproutGrid = new ArrayList[Params.EDGE][Params.EDGE];
        // Checks each patch for its regeneration
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                sprout(grid, sproutGrid, i, j);
            }
        }
        // Applies the sprouted daisies to the original grid.
        // Randomly chose a baby daisy if there are more than one candidate.
        Random random = new Random();
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                if (grid[i][j].getDaisy() == null && sproutGrid[i][j] != null) {
                    // Randomly chooses a baby candidate
                    Daisy babyDaisy = sproutGrid[i][j].get(random.nextInt(sproutGrid[i][j].size()));
                    grid[i][j].setDaisy(babyDaisy);
                }
            }
        }
    }

    private static void sprout(Patch[][] grid, ArrayList<Daisy>[][] sproutGrid, int i, int j) {
        double temperature = grid[i][j].getTemperature();
        double seedThreshold = (0.1457 * temperature) - (0.0032 * (temperature * temperature)) - 0.6443;
        Random random = new Random();
        double survivability = random.nextDouble();
        Daisy parent = grid[i][j].getDaisy();
        if (parent != null && survivability < seedThreshold) {
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

    private static int wrap(int coordinate) {
        if (coordinate < 0) {
            return Params.EDGE - 1;
        } else if (coordinate >= Params.EDGE) {
            return  0;
        } else {
            return coordinate;
        }
    }

    private static void addIfNoDaisy(LinkedList<int[]> neighbors, int x, int y, Patch[][] grid) {
        if (grid[x][y].getDaisy() == null) {
            neighbors.add(new int[] {x, y});
        }
    }

    public static void main(String[] args) {
        Patch [][] grid = new Patch[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] = new Patch();
                grid[i][j].setTemperature(25);
            }
        }
        grid[4][4].setDaisy(new Daisy(Daisy.daisyType.WHITE, 0, 0));
        grid[4][5].setDaisy(new Daisy(Daisy.daisyType.WHITE, 0, 0));
        grid[4][6].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        grid[5][4].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        grid[5][6].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        grid[6][4].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        grid[6][5].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        grid[6][6].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));

        for (int k = 0; k < 1; k++) {
            regenerate(grid);
            for (int i = 0; i < Params.EDGE; i++) {
                for (int j = 0; j < Params.EDGE; j++) {
                    Daisy daisy = grid[i][j].getDaisy();
                    if (daisy != null) {
                        System.out.print(grid[i][j].getDaisy().getType() + " ");
                    } else {
                        System.out.print("null  ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}

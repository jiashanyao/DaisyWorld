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
        // Records if a patch sprouts a daisy in the grid
        Daisy[][] sproutGrid = new Daisy[Params.EDGE][Params.EDGE];
        Random random = new Random();
        // Calculates which patch will sprout a daisy
        // For patches that are not located on edges
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                sprout(grid, sproutGrid, i, j);
            }
        }
        // Applies the sprouted daisies to the original grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                if (grid[i][j].getDaisy() == null && sproutGrid[i][j] != null) {
                    grid[i][j].setDaisy(sproutGrid[i][j]);
                }
            }
        }
    }

    private static void sprout(Patch[][] grid, Daisy[][] sproutGrid, int i, int j) {
        Daisy parent = grid[i][j].getDaisy();
        if (parent != null) {
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
                Random random = new Random();
                int[] sproutCoor = neighbors.get(random.nextInt(neighbors.size()));
                int age = random.nextInt(Params.MAX_AGE);
                // Inherits from parent's type, albedo. Age is randomized.
                sproutGrid[sproutCoor[0]][sproutCoor[1]] = new Daisy(parent.getType(), parent.getAlbedo(), age);
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
            }
        }
        grid[0][0].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        for (int k = 0; k < 5; k++) {
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

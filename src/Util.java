import java.util.LinkedList;
import java.util.Random;

public class Util {

    static void diffuse(Patch[][] grid, double diffusionRatio) {
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

    static void calculateShares(double patchValue, double[][] gridDelta, int x, int y, double diffusionRatio) {
        double deltaTemp = diffusionRatio / 8 * patchValue;
        if (x > 0 && x < Params.EDGE - 1) {
            if (y > 0 && y < Params.EDGE - 1) {
                gridDelta[x - 1][y - 1] += deltaTemp;
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x - 1][y + 1] += deltaTemp;
                gridDelta[x][y - 1] += deltaTemp;
                gridDelta[x][y + 1] += deltaTemp;
                gridDelta[x + 1][y - 1] += deltaTemp;
                gridDelta[x + 1][y] += deltaTemp;
                gridDelta[x + 1][y + 1] += deltaTemp;
            }
            if (y == 0) {
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x - 1][y + 1] += deltaTemp;
                gridDelta[x][y + 1] += deltaTemp;
                gridDelta[x + 1][y] += deltaTemp;
                gridDelta[x + 1][y + 1] += deltaTemp;
                gridDelta[x][y] += 3 * deltaTemp;
            }
            if (y == Params.EDGE - 1) {
                gridDelta[x - 1][y - 1] += deltaTemp;
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x][y - 1] += deltaTemp;
                gridDelta[x + 1][y - 1] += deltaTemp;
                gridDelta[x + 1][y] += deltaTemp;
                gridDelta[x][y] += 3 * deltaTemp;
            }
        }
        if (x == 0) {
            if (y > 0 && y < Params.EDGE - 1) {
                gridDelta[x][y - 1] += deltaTemp;
                gridDelta[x][y + 1] += deltaTemp;
                gridDelta[x + 1][y - 1] += deltaTemp;
                gridDelta[x + 1][y] += deltaTemp;
                gridDelta[x + 1][y + 1] += deltaTemp;
                gridDelta[x][y] += 3 * deltaTemp;
            }
            if (y == 0) {
                gridDelta[0][1] += deltaTemp;
                gridDelta[1][0] += deltaTemp;
                gridDelta[1][1] += deltaTemp;
                gridDelta[x][y] += 5 * deltaTemp;
            }
            if (y == Params.EDGE - 1) {
                gridDelta[0][y - 1] += deltaTemp;
                gridDelta[1][y - 1] += deltaTemp;
                gridDelta[1][y] += deltaTemp;
                gridDelta[x][y] += 5 * deltaTemp;
            }
        }
        if (x == Params.EDGE - 1) {
            if (y > 0 && y < Params.EDGE - 1) {
                gridDelta[x - 1][y - 1] += deltaTemp;
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x - 1][y + 1] += deltaTemp;
                gridDelta[x][y - 1] += deltaTemp;
                gridDelta[x][y + 1] += deltaTemp;
                gridDelta[x][y] += 3 * deltaTemp;
            }
            if (y == 0) {
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x - 1][y + 1] += deltaTemp;
                gridDelta[x][y + 1] += deltaTemp;
                gridDelta[x][y] += 5 * deltaTemp;
            }
            if (y == Params.EDGE - 1) {
                gridDelta[x - 1][y - 1] += deltaTemp;
                gridDelta[x - 1][y] += deltaTemp;
                gridDelta[x][y - 1] += deltaTemp;
                gridDelta[x][y] += 5 * deltaTemp;
            }
        }
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

    static void regenerate(Patch[][] grid) {
        // Records if a patch sprouts a daisy in the grid
        Daisy[][] sproutGrid = new Daisy[Params.EDGE][Params.EDGE];
        Random random = new Random();
        // Calculates which patch will sprout a daisy
        // For patches that are not located on edges
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                Daisy parent = grid[i][j].getDaisy();
                if (parent != null) {
                    LinkedList<int[]> neighbors = new LinkedList<>();
                    addIfNoDaisy(neighbors, wrap(i - 1, j - 1), grid);
                    addIfNoDaisy(neighbors, wrap(i - 1, j), grid);
                    addIfNoDaisy(neighbors, wrap(i - 1, j + 1), grid);
                    addIfNoDaisy(neighbors, wrap(i, j + 1), grid);
                    addIfNoDaisy(neighbors, wrap(i + 1, j + 1), grid);
                    addIfNoDaisy(neighbors, wrap(i + 1, j), grid);
                    addIfNoDaisy(neighbors, wrap(i + 1, j - 1), grid);
                    addIfNoDaisy(neighbors, wrap(i, j - 1), grid);
                    if (neighbors.size() > 0) {
                        int[] sproutCoor = neighbors.get(random.nextInt(neighbors.size()));
                        int age = random.nextInt(Params.MAX_AGE);
                        // Inherits from parent's type, albedo. Age is randomized.
                        sproutGrid[sproutCoor[0]][sproutCoor[1]] = new Daisy(parent.getType(), parent.getAlbedo(), age);
                    }
                }
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

    private static int[] wrap(int i, int j) {
        if (i < 0) {
            i = Params.EDGE - 1;
        } else if (i >= Params.EDGE) {
            i = 0;
        }
        if (j < 0) {
            j = Params.EDGE - 1;
        } else if (j >= Params.EDGE) {
            j = 0;
        }
        return new int[] {i, j};
    }

    private static void addIfNoDaisy(LinkedList<int[]> neighbors, int[] coordinates, Patch[][] grid) {
        if (grid[coordinates[0]][coordinates[1]].getDaisy() == null) {
            neighbors.add(coordinates);
        }
    }

    public static void main(String[] args) {
        Patch [][] grid = new Patch[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] = new Patch();
            }
        }
        grid[9][5].setDaisy(new Daisy(Daisy.daisyType.BLACK, 0, 0));
        for (int k = 0; k < 5; k++) {
            regenerate(grid);
            for (int i = 0; i < Params.EDGE; i++) {
                for (int j = 0; j < Params.EDGE; j++) {
                    Daisy daisy = grid[i][j].getDaisy();
                    if (daisy != null) {
                        System.out.print(daisy.getType() + " ");
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

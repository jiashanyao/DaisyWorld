public class Diffusion {

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
    static void applyShares(double[][] gridDelta, Patch[][] grid, double diffusionRatio) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                double newTemperature = grid[i][j].getTemperature() * (1 - diffusionRatio) + gridDelta[i][j];
                grid[i][j].setTemperature(newTemperature);
            }
        }
    }

    public static void main(String[] args) {
        Patch [][] grid = new Patch[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] = new Patch();
                grid[i][j].setTemperature(-10);
            }
        }
        grid[5][5].setTemperature(0);
        for (int k = 0; k < 3; k++)
            diffuse(grid, 0.5);
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                System.out.print(grid[i][j].getTemperature() + " ");
            }
            System.out.println();
        }
    }
}

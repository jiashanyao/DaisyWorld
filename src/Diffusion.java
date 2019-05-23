public class Diffusion {

    static void diffuse(double[][] grid, double diffusionRatio) {
        double[][] gridDelta = new double[Params.EDGE][Params.EDGE];

        //In default the temp will not change, so the change condition will be 0.
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) gridDelta[i][j] = 0;
        }

        //For each patch would be changed, implement its modification
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++)
                calculateShares(grid[i][j], gridDelta, i, j, diffusionRatio);
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
    static void applyShares(double[][] gridDelta, double[][] grid, double diffusionRatio) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                grid[i][j] *= (1 - diffusionRatio);
                grid[i][j] += gridDelta[i][j];
            }
        }
    }

    public static void main(String[] args) {
        double [][] temperatureArray = new double[Params.EDGE][Params.EDGE];
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                temperatureArray[i][j] = 0;
            }
        }
        temperatureArray[15][15] = 200;
        for (int k = 0; k < 2; k++)
            diffuse(temperatureArray, 0.5);
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                System.out.print(temperatureArray[i][j] + " ");
            }
            System.out.println();
        }
    }
}

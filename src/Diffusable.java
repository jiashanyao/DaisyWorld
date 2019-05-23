public class Diffusable {
    static double[][] temperatureArray;

    static void diffuseTemp(double[][] temperatureArray, double transferCoefficient) {
        double[][] deltaTemp = new double[Params.EDGE][Params.EDGE];

        //In default the temp will not change, so the change condition will be 0.
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) deltaTemp[i][j] = 0;
        }

        //For each patch would be changed, implement its modification
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++)
                affectSurrouding(temperatureArray[i][j], deltaTemp, i, j, transferCoefficient);
        }
        allocateChangeToOrigin(deltaTemp, temperatureArray, transferCoefficient);
    }

    static void affectSurrouding(double patchTemp, double[][] deltaTempArray, int xIndex, int yIndex, double transferCoefficient) {
        double deltaTemp = transferCoefficient / 8 * patchTemp;
        if (xIndex > 0 && xIndex < Params.EDGE - 1) {
            if (yIndex > 0 && yIndex < Params.EDGE - 1) {
                deltaTempArray[xIndex - 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex + 1] += deltaTemp;
            }
            if (yIndex == 0) {
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 3 * deltaTemp;
            }
            if (yIndex == Params.EDGE - 1) {
                deltaTempArray[xIndex - 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 3 * deltaTemp;
            }
        }
        if (xIndex == 0) {
            if (yIndex > 0 && yIndex < Params.EDGE - 1) {
                deltaTempArray[xIndex][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex + 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 3 * deltaTemp;
            }
            if (yIndex == 0) {
                deltaTempArray[0][1] += deltaTemp;
                deltaTempArray[1][0] += deltaTemp;
                deltaTempArray[1][1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 5 * deltaTemp;
            }
            if (yIndex == Params.EDGE - 1) {
                deltaTempArray[0][yIndex - 1] += deltaTemp;
                deltaTempArray[1][yIndex - 1] += deltaTemp;
                deltaTempArray[1][yIndex] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 5 * deltaTemp;
            }
        }
        if (xIndex == Params.EDGE - 1) {
            if (yIndex > 0 && yIndex < Params.EDGE - 1) {
                deltaTempArray[xIndex - 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 3 * deltaTemp;
            }
            if (yIndex == 0) {
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex + 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 5 * deltaTemp;
            }
            if (yIndex == Params.EDGE - 1) {
                deltaTempArray[xIndex - 1][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex - 1][yIndex] += deltaTemp;
                deltaTempArray[xIndex][yIndex - 1] += deltaTemp;
                deltaTempArray[xIndex][yIndex] += 5 * deltaTemp;
            }
        }
    }

    //Firstly calculate the remaining temperature, then append modification to its remaining temperature
    static void allocateChangeToOrigin(double[][] modification, double[][] originArray, double transferCoefficient) {
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                originArray[i][j] *= (1 - transferCoefficient);
                originArray[i][j] += modification[i][j];
            }
        }
    }

    public static void main(String[] args) {
        temperatureArray = new double[Params.EDGE][Params.EDGE];
    }
}

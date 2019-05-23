public class Diffusable {
    static double[][] temperatureArray;

    static void diffuseTemp(double[][] temperatureArray, double transferCoefficient) {
        double[][] deltaTemp = new double[30][30];

        //In default the temp will not change, so the change condition will be 0.
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) deltaTemp[i][j] = 0;
        }

        //For each patch would be changed, implement its modification
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) affectSurrouding(temperatureArray[i][j], deltaTemp, i, j);
        }
        allocateChangeToOrigin(deltaTemp, temperatureArray, transferCoefficient);
    }

    static void affectSurrouding(double patchTemp, double[][] deltaTempArray, int xIndex, int yIndex) {
        if (xIndex

    }

    //Firstly calculate the remaining temperature, then append modification to its remaining tempature
    static void allocateChangeToOrigin(double[][] modification, double[][] originArray, double transferCoefficient) {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                originArray[i][j] *= (1 - transferCoefficient);
                originArray[i][j] += modification[i][j];
            }
        }
    }

    public static void main(String[] args) {
        temperatureArray = new double[30][30];
    }
}

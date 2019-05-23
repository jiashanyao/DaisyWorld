import java.util.LinkedList;
import java.util.Random;

public class Createflower {

    static void createFlowers(int[][] flowerArray, double perCentOfBlack, double perCentOfWhite) {
        LinkedList<int[]> remainingSpace = new LinkedList<>();
        //initialize the space as empty (0)
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                flowerArray[i][j] = 0;
                remainingSpace.add(new int[]{i, j});
            }
        }

        int numberOfSpaces = Params.EDGE * Params.EDGE;
        int numOfBlack = (int) (perCentOfBlack * 0.01 * numberOfSpaces);
        int numOfWhite = (int) (perCentOfWhite * 0.01 * numberOfSpaces);
        //type 1 = black flower, type 2 = white flower
        generateFlowersInWorld(flowerArray, remainingSpace, 1, numOfBlack);
        generateFlowersInWorld(flowerArray, remainingSpace, 2, numOfWhite);
    }

    static void generateFlowersInWorld(int[][] flowerArray, LinkedList<int[]> remainingSpace, int typeOfFlower, int numberOfFlowers) {
        while (numberOfFlowers > 0) {
            int maxRandom = remainingSpace.size();
            Random random = new Random();
            int chosenOne = random.nextInt(maxRandom);
            int[] addingIndex = remainingSpace.remove(chosenOne);
            flowerArray[addingIndex[0]][addingIndex[1]] = typeOfFlower;
            numberOfFlowers--;
        }
    }

    public static void main(String[] args) {
        int[][] flowerArray = new int[Params.EDGE][Params.EDGE];
        createFlowers(flowerArray, Params.START_BLACK, Params.START_WHITE);
        for (int i = 0; i < Params.EDGE; i++) {
            System.out.println();
            for (int j = 0; j < Params.EDGE; j++) {
                System.out.print(flowerArray[i][j] + " ");
            }
        }

    }
}

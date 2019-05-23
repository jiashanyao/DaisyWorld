import java.util.LinkedList;
import java.util.Random;

public class Setup {

    static void seedDaisies(int[][] grid, double percentOfBlack, double percentOfWhite) {
        LinkedList<int[]> remainingSpace = new LinkedList<>();
        //initialize the remainingSpace as a list of 2-int-arrays, each of which represents a patch coordinate in the grid
        for (int i = 0; i < Params.EDGE; i++) {
            for (int j = 0; j < Params.EDGE; j++) {
                remainingSpace.add(new int[]{i, j});
            }
        }

        int numberOfSpaces = Params.EDGE * Params.EDGE;
        int numOfBlack = (int) (percentOfBlack * 0.01 * numberOfSpaces);
        int numOfWhite = (int) (percentOfWhite * 0.01 * numberOfSpaces);
        //type 1 = black flower, type 2 = white flower
        generateFlowersInWorld(grid, remainingSpace, 1, numOfBlack);
        generateFlowersInWorld(grid, remainingSpace, 2, numOfWhite);
    }

    static void generateFlowersInWorld(int[][] grid, LinkedList<int[]> remainingSpace, int typeOfFlower, int numberOfFlowers) {
        Random random = new Random();
        while (numberOfFlowers > 0) {
            int maxRandom = remainingSpace.size();
            int chosenOne = random.nextInt(maxRandom);
            int[] addingIndex = remainingSpace.remove(chosenOne);
            grid[addingIndex[0]][addingIndex[1]] = typeOfFlower;
            numberOfFlowers--;
        }
    }

    public static void main(String[] args) {
        int[][] flowerArray = new int[Params.EDGE][Params.EDGE];
        seedDaisies(flowerArray, Params.START_BLACK, Params.START_WHITE);
        for (int i = 0; i < Params.EDGE; i++) {
            System.out.println();
            for (int j = 0; j < Params.EDGE; j++) {
                System.out.print(flowerArray[i][j] + " ");
            }
        }

    }
}

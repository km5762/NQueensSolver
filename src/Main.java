import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Type \"exit\" to exit. Type 0 to check if a position is legal. Type 1 to find the next legal position. Type 2 to solve an n queens problem.");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                ArrayList<Integer> array = new ArrayList<>();
                System.out.println("To fill the positional array, enter a value and then press enter. When you are done filling the array, enter nothing.");
                while (true) {
                    String num = scanner.nextLine();
                    if (num.isEmpty()) {
                        int[] intArray = array.stream().mapToInt(i -> i).toArray();
                        if (isLegalPosition(intArray))
                            System.out.println("Position " + Arrays.toString(intArray) + " is legal.");
                        else
                            System.out.println("Position " + Arrays.toString(intArray) + " is not legal.");
                        break;
                    } else {
                        try {
                            array.add(Integer.valueOf(num));
                        } catch (Exception e) {
                            System.out.println("Please enter only valid integers.");
                        }
                    }
                }
            } else if (input.equals("exit")) {
                break;
            } else if (input.equals("1")) {
                ArrayList<Integer> array = new ArrayList<>();
                System.out.println("To fill the positional array, enter a value and then press enter. When you are done filling the array, enter nothing.");
                while (true) {
                    String num = scanner.nextLine();
                    if (num.isEmpty()) {
                        int[] intArray = array.stream().mapToInt(i -> i).toArray();
                        System.out.println("Next legal position is " + Arrays.toString(nextLegalPosition(intArray.length, intArray)));
                        break;
                    } else {
                        try {
                            array.add(Integer.valueOf(num));
                        } catch (Exception e) {
                            System.out.println("Please enter only valid integers.");
                        }
                    }
                }
            } else if (input.equals("2")) {
                System.out.println(String.format("%-32s %-32s %-32s", "n", "Number of solutions", "First solution"));
                for (int i = 4; i <= 14; i++) {
                    System.out.println(String.format("%-32d %-32d %-32s", i, nQueensCountSolutions(i), Arrays.toString(nQueensFirstSolution(i))));
                }
            }
        }
    }

    public static boolean isLegalPosition(int... positions) {
        for (int i = 0; i < positions.length; i++) {        /// for each position in positions
            for (int k = 0; k < positions.length; k++) {    /// compare to all other positions
                if (k != i) {
                    int positionRow = i;
                    int positionColumn = positions[i];
                    if (positionColumn == 0)        /// if we have reached 0 positions, we have already confirmed the partial solution
                        return true;

                    int compareToRow = k;
                    int compareToColumn = positions[k];
                    if (compareToColumn == 0) {         /// skip over 0 positions in comparisons
                        break;
                    }

                    if (positionRow == compareToRow)        /// same row
                        return false;
                    else if (positionColumn == compareToColumn)     /// same column
                        return false;
                    else if (((positionColumn - positionRow) == (compareToColumn - compareToRow)) || ((positionColumn + positionRow) == (compareToColumn + compareToRow)))      /// same diagonal
                        return false;
                }
            }
        }
        return true;
    }

    public static int lastPosition(int... positions) {
        for (int i = positions.length - 1; i >= 0; i--) {
            if (positions[i] != 0) {
                return i + 1;
            }
        }
        return -1;
    }

    public static boolean isSolution(int... positions) {
        return positions[positions.length - 1] != 0;
    }

    public static int legalMoveInRowFromLegal(int n, int row, int... positions) {
        int[] nextPosition = positions.clone();
        for (int i = positions[row - 1] + 1; i <= n; i++) {
            nextPosition[row - 1] = i;
            if (isLegalFromLegal(nextPosition) && nextPosition[row - 1] != 0)
                return i;
        }
        return -1;
    }

    public static int legalMoveInRow(int n, int row, int... positions) {
        int[] nextPosition = positions.clone();
        for (int i = positions[row - 1] + 1; i <= n; i++) {
            nextPosition[row - 1] = i;
            if (isLegalPosition(nextPosition) && nextPosition[row - 1] != 0)
                return i;
        }
        return -1;
    }

    public static boolean isLegalFromLegal(int... positions) {
        int lastQueenRow = lastPosition(positions);
        int lastQueenColumn = positions[lastQueenRow - 1];
        for (int i = lastQueenRow - 2; i >= 0; i--) {
            int compareToRow = i + 1;
            int compareToColumn = positions[i];
            if (lastQueenRow == compareToRow || lastQueenColumn == compareToColumn || (((lastQueenColumn - lastQueenRow) == (compareToColumn - compareToRow)) || ((lastQueenColumn + lastQueenRow) == (compareToColumn + compareToRow))))
                return false;
        }
        return true;
    }

    public static int[] nextLegalPositionFromLegal(int n, int... positions) {
        int lastRow = lastPosition(positions);
        int[] nextPosition = positions.clone();

        for (int i = lastRow + 1; i >= 1; i--) {
            if (!(i > positions.length)) {
                int move;
                if ((move = legalMoveInRowFromLegal(n, i, nextPosition)) != -1) {
                    nextPosition[i - 1] = move;
                    break;
                } else
                    nextPosition[i - 1] = 0;
            }
        }
        return nextPosition;
    }

    public static int[] nextLegalPosition(int n, int... positions) {
        int lastRow = lastPosition(positions);
        int[] nextPosition = positions.clone();

        for (int i = lastRow + 1; i >= 1; i--) {
            if (!(i > positions.length)) {
                int move;
                if ((move = legalMoveInRow(n, i, nextPosition)) != -1) {
                    nextPosition[i - 1] = move;
                    break;
                } else
                    nextPosition[i - 1] = 0;
            }
        }
        return nextPosition;
    }

    public static int[] nQueensFirstSolution(int n) {
        int[] nextPosition = new int[n];
        nextPosition[0] = 1;
        while (!isSolution(nextPosition)) {
            nextPosition = nextLegalPositionFromLegal(n, nextPosition);
        }

        return nextPosition;
    }

    public static int nQueensCountSolutions(int n) {
        int[] nextPosition = new int[n];
        nextPosition[0] = 1;
        int count = 0;
        while (!allSolutionsFound(nextPosition)) {
            nextPosition = nextLegalPositionFromLegal(n, nextPosition);
            if (isSolution(nextPosition))
                count++;
        }

        return count;
    }

    public static boolean allSolutionsFound(int... positions) {
        return positions[0] == 0;
    }
}

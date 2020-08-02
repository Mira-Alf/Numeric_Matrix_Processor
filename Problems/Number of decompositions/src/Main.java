import java.util.Scanner;

public class Main {

    public static int[][][] SUBARRAY_MATRIX;

    private static int getNumberOfRows(int[][] tmp, int index) {
        int count = 0;
        for(int i = 0; i < tmp.length; i++) {
            if(tmp[i][0] <= index)
                count++;
        }
        return count;
    }

    private static int getNumberOfRows(String tmp, int index) {
        int count = 0;
        String[] tmpStrings = tmp.split("\n");
        for(int i = 0; i < tmpStrings.length; i++) {
            int firstElement = Integer.parseInt(tmpStrings[i].split("\\s+")[0]);
            if(firstElement <= index)
                count++;
        }
        return count;
    }

    private static void display(int[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static int[][] getSubArray(int index) {
        if(index == 1) {
            if(SUBARRAY_MATRIX[index-1] == null ) {
                SUBARRAY_MATRIX[index-1] = new int[][]{{1}};
            }
            return SUBARRAY_MATRIX[index-1];
        } else {
            int totalRows = 0;
            int[][][] subArrays = new int[index-1][][];
            int[] numberOfRowsForIndex = new int[index];
            for(int i = 1; i < index; i++) {
                int[][] tmp = null;
                if(SUBARRAY_MATRIX[index-i-1] == null) {
                    tmp = getSubArray(index-i);
                    SUBARRAY_MATRIX[index-1-i] = tmp;
                };//
                subArrays[i-1] = SUBARRAY_MATRIX[index-1-i];
                tmp = SUBARRAY_MATRIX[index-1-i];
                int numRowsStartingWithI = getNumberOfRows(tmp, i);
                numberOfRowsForIndex[i] = numRowsStartingWithI;
                totalRows += numRowsStartingWithI;
            }
            int[][] result = new int[totalRows+1][];
            int rowIndex = 0;
            for(int i = 1; i <index; i++ ) {
                int[][] tmp = subArrays[i-1];
                int numRowsStartingWithI = numberOfRowsForIndex[i];
                for(int j = 0; j < numRowsStartingWithI; j++) {
                    result[rowIndex] = new int[tmp[j].length+1];
                    result[rowIndex][0] = i;
                    for(int k = 1; k <= tmp[j].length; k++) {
                        result[rowIndex][k] = tmp[j][k-1];
                    }
                    rowIndex++;
                }
            }
            result[totalRows] = new int[] {index};
            if(SUBARRAY_MATRIX[index-1] == null) {
                SUBARRAY_MATRIX[index-1] = result;
            }
            return result;
        }
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int stopValue = scanner.nextInt();
        SUBARRAY_MATRIX = new int[stopValue][][];
        int[][] result = getSubArray(stopValue);
        display(result);
    }
}
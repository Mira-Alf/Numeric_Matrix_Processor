package processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //try {
            ProgramTemplate template = new ProgramTemplate();
            template.execute();
        //} catch(Exception e) {
//            System.out.println("ERROR: "+e.getMessage());
        //}
    }
}

enum MatrixOperation {
    ADD(1), MULT_WITH_A_CONSTANT(2), MATRIX_MULT(3), TRANSPOSE(4),
    DETERMINANT(5), INVERSE(6), EXIT(0);
    private int opValue;

    MatrixOperation(int opValue) {
        this.opValue = opValue;
    }

    public static MatrixOperation getOperation(int opValue) {
        switch (opValue) {
            case 0:
                return EXIT;
            case 1:
                return ADD;
            case 2:
                return MULT_WITH_A_CONSTANT;
            case 3:
                return MATRIX_MULT;
            case 4:
                return TRANSPOSE;
            case 5:
                return DETERMINANT;
            case 6:
                return INVERSE;
        }
        return null;
    }

    public String getStatement() {
        switch(this) {
            case ADD:
                return "1. Add matrices";
            case MULT_WITH_A_CONSTANT:
                return "2. Multiply matrix to a constant";
            case MATRIX_MULT:
                return "3. Multiply matrices";
            case TRANSPOSE:
                return "4. Transpose matrix";
            case DETERMINANT:
                return "5. Calculate a determinant";
            case INVERSE:
                return "6. Inverse matrix";
            case EXIT:
                return "0. Exit";
        }
        return "";
    }

}

enum TransposeOperation {
    MAIN_DIAGONAL(1), SIDE_DIAGONAL(2), VERTICAL_LINE(3), HORIZONTAL_LINE(4);
    private int transposeType;

    TransposeOperation(int transposeType) {
        this.transposeType = transposeType;
    }

    public static TransposeOperation getTransposeOperation(int transposeType) {
        switch(transposeType) {
            case 1:
                return MAIN_DIAGONAL;
            case 2:
                return SIDE_DIAGONAL;
            case 3:
                return VERTICAL_LINE;
            case 4:
                return HORIZONTAL_LINE;
        }
        return null;
    }

    public String getStatement() {
        switch(this) {
            case MAIN_DIAGONAL:
                return "1. Main diagonal";
            case SIDE_DIAGONAL:
                return "2. Side diagonal";
            case VERTICAL_LINE:
                return "3. Vertical line";
            case HORIZONTAL_LINE:
                return "4. Horizontal line";
        }
        return "";
    }
}

class ProgramTemplate {

    private MatrixOperation operation;
    private MatrixOperator operator;
    private final Scanner scanner;

    public ProgramTemplate() {
        scanner = new Scanner(System.in);
    }

    public void setOperation() {
        Arrays.stream(MatrixOperation.values())
                .map(MatrixOperation::getStatement)
                .forEach(System.out::println);

        System.out.print("Your choice: ");
        int opValue = Integer.parseInt(scanner.nextLine().split("\\s+")[0]);
        operation = MatrixOperation.getOperation(opValue);
    }

    public void setOperator() {
        switch(operation) {
            case ADD:
                operator = new MatrixAdditionOperator(scanner);
                break;
            case MULT_WITH_A_CONSTANT:
                operator = new MatrixScalarMultiplicationOperator(scanner);
                break;
            case MATRIX_MULT:
                operator = new MatrixMultiplicationOperator(scanner);
                break;
            case TRANSPOSE:
                operator = new MatrixTransposeOperator(scanner);
                break;
            case DETERMINANT:
                operator = new MatrixDeterminantOperator(scanner);
                break;
            case INVERSE:
                operator = new MatrixInverseOperator(scanner);
                break;
        }
    }

    public void execute() {
        setOperation();
        while(operation != MatrixOperation.EXIT) {
            setOperator();
            performOperation();
            displayResults();
            setOperation();
        }
    }

    public void performOperation() {
        operator.execute(false);
    }

    public void displayResults() {
        operator.displayResults();
    }

}

class MatrixUtil {
    public static double[][] makeMatrix(List<String> inputLines) {
        String[] rowColValue = inputLines.get(0).split("\\s+");
        double[][] matrix = new double[Integer.parseInt(rowColValue[0])]
                [Integer.parseInt(rowColValue[1])];
        int rowIndex = 0, colIndex = 0;

        for(int index=1; index < inputLines.size(); index++) {
            String[] rowLine = inputLines.get(index).split("\\s+");
            for (String s : rowLine) matrix[rowIndex][colIndex++] = Double.parseDouble(s);
            rowIndex++;
            colIndex = 0;
        }
        return matrix;
    }

    public static void validateMatricesBeforeMultiplying(double[][] matrixA, double[][] matrixB ) {
        int matrixARows = matrixA.length;
        int matrixACols = getColumns(matrixA);
        int matrixBRows = matrixB.length;
        int matrixBCols = getColumns(matrixB);

        if(matrixACols != matrixBRows)
            throw new RuntimeException("Matrices cannot be multiplied as dimensions are not correct");

    }

    public static int getColumns(double[][] matrix) {
        int columns = 0;
        for(int i=0; i < matrix.length; i++) {
            if(columns == 0)
                columns = matrix[i].length;
            else {
                if(columns != matrix[i].length)
                    throw new RuntimeException("Not a valid matrix as different number of columns are present");
            }
        }
        return columns;
    }



    private static boolean isTriangleMatrix(double[][] matrix, boolean checkLowerHalf) {
        if (checkLowerHalf) {
            for (int i = 1; i < matrix.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (matrix[i][j] != 0)
                        return false;
                }
            }
            return true;
        } else {
            for (int i = 0; i < matrix.length - 1; i++) {
                for (int j = i + 1; j < matrix[i].length; j++) {
                    if (matrix[i][j] != 0)
                        return false;
                }
            }
            return true;
        }
    }

    private static int getMaximumIndex(int[] numberOfZeros) {
        int maxIndex = 0;
        for(int i = 1; i < numberOfZeros.length; i++) {
            if(numberOfZeros[i] > numberOfZeros[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    static int mult(int num, int numTimes) {
        int prod = 1;
        for(int i = 0; i < numTimes; i++) {
            prod *= num;
        }
        return prod;
    }

    private static double calculateProductOfMainDiagonalElements(double[][] matrix) {
        double prod = 1.0;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(i == j) {
                    prod *= matrix[i][j];
                }
            }
        }
        return prod;
    }

    public static double[][] makeSubMatrix(double[][]matrix, int rowIndexToSkip, int colIndexToSkip) {
        double[][] result = new double[matrix.length-1][];
        int rowIndex = 0, colIndex = 0;
        for(int i = 0; i < matrix.length; i++) {
            if(i != rowIndexToSkip) {
                result[rowIndex] = new double[matrix[i].length-1];
                for(int j = 0; j < matrix[i].length; j++) {
                    if(j != colIndexToSkip) {
                        result[rowIndex][colIndex++] = matrix[i][j];
                    }
                }
                rowIndex++;
                colIndex = 0;
            }
        }
        return result;
    }

    public static double calculateDeterminant(double[][] matrix) {
        //displayMatrix(matrix);
        //System.out.println("__________________________");
        boolean isTriangleMatrix = isTriangleMatrix(matrix, true);
        isTriangleMatrix = isTriangleMatrix || isTriangleMatrix(matrix, false);
        if(isTriangleMatrix) {
            return calculateProductOfMainDiagonalElements(matrix);
        }

        int[] numberOfZerosInRows = getNumberOfZeros(matrix, true);
        int[] numberOfZerosInCols = getNumberOfZeros(matrix, false);
        int selectedRow = getMaximumIndex(numberOfZerosInRows);
        int selectedColumn = getMaximumIndex(numberOfZerosInCols);
        //System.out.println(selectedRow+", "+selectedColumn);
        boolean isRow = true;
        int selectedIndex = selectedRow;
        if(numberOfZerosInRows[selectedRow] < numberOfZerosInCols[selectedColumn]) {
            isRow = false;
            selectedIndex = selectedColumn;
        }
        double determinant = 0.0;

        if(isRow) {

            for(int j = 0; j < matrix[selectedIndex].length; j++) {
                determinant += matrix[selectedIndex][j] * mult(-1, selectedIndex+j) *
                        calculateDeterminant(makeSubMatrix(matrix, selectedIndex, j));
            }
        } else {
            for(int i = 0; i < matrix.length; i++) {
                determinant += matrix[i][selectedIndex] * mult(-1, selectedIndex+i) *
                        calculateDeterminant(makeSubMatrix(matrix, i, selectedIndex));
            }
        }
        //System.out.println(determinant);
        return determinant;
    }

    private static int[] getNumberOfZeros(double[][] matrix, boolean checkRows) {
        int[] zerosInRowsOrCols = null;
        if(checkRows == true) {
            zerosInRowsOrCols = new int[matrix.length];
            for(int i = 0; i < matrix.length; i++) {
                int count = 0;
                for(int j = 0; j < matrix[i].length; j++) {
                    if(matrix[i][j] == 0)
                        count++;
                }
                zerosInRowsOrCols[i] = count;
            }
        } else {
            zerosInRowsOrCols = new int[getColumns(matrix)];
            for(int j = 0; j < zerosInRowsOrCols.length; j++) {
                int count = 0;
                for(int i = 0; i < matrix.length; i++) {
                    if(matrix[i][j] == 0)
                        count++;
                }
                zerosInRowsOrCols[j] = count;
            }
        }
        return zerosInRowsOrCols;
    }


    public static void displayMatrix(double[][] matrix) {
        System.out.println();
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j< matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

abstract class MatrixOperator {
    protected MatrixOperation operation;
    protected double[][] resultMatrix;
    protected Scanner scanner;

    public MatrixOperator() {}

    public MatrixOperator(Scanner scanner) {
        this.scanner = scanner;
    }

    abstract void setInputs();
    void execute(boolean isInputSetAlready) {
        if(!isInputSetAlready)
            setInputs();
    }
    abstract void displayResults();
    public double[][] getResultMatrix() {
        return this.resultMatrix;
    }
}

class MatrixAdditionOperator extends MatrixOperator {

    private double[][] matrixA;
    private double[][] matrixB;

    public MatrixAdditionOperator(Scanner scanner) {
        super(scanner);
        this.operation = MatrixOperation.ADD;
    }

    @Override
    public void setInputs() {

        for(int i = 0; i < 2; i++) {
            List<String> inputLines = new ArrayList<>();
            System.out.printf("Enter the size of the %s matrix:%n", i==0 ? "first" : "second");
            String inputLine = scanner.nextLine();
            inputLines.add(inputLine);
            int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
            System.out.printf("Enter the %s matrix:%n", i==0 ? "first" : "second");
            for(int j = 0; j < rows; j++ ) {
                inputLines.add(scanner.nextLine());
            }
            setMatrix(i, MatrixUtil.makeMatrix(inputLines));
        }
    }

    private void setMatrix(int index, double[][] matrix) {
        if(index == 0)
            matrixA = matrix;
        else
            matrixB = matrix;
    }

    @Override
    void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        addMatrices();
    }

    @Override
    void displayResults() {
        System.out.println("The addition result is:");
        MatrixUtil.displayMatrix(resultMatrix);
    }

    public void addMatrices() {
        if(matrixA.length != matrixB.length)
            throw new RuntimeException("Matrices cannot be added");
        resultMatrix = new double[matrixA.length][];
        for(int i = 0; i < matrixA.length; i++) {
            if(matrixA[i].length != matrixB[i].length)
                throw new RuntimeException("Matrices cannot be added");

            resultMatrix[i] = new double[matrixA[i].length];
            for(int j = 0; j < matrixA[i].length; j++) {
                resultMatrix[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
    }

}

class MatrixScalarMultiplicationOperator extends MatrixOperator {

    private double[][] matrix;
    private double scalingFactor;
    public MatrixScalarMultiplicationOperator(Scanner scanner) {
        super(scanner);
        operation = MatrixOperation.MATRIX_MULT;
    }

    @Override
    void setInputs() {
        List<String> inputLines = new ArrayList<>();
        System.out.printf("Enter the size of the matrix: ");
        String inputLine = scanner.nextLine();
        inputLines.add(inputLine);
        int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
        System.out.printf("Enter the matrix:%n");
        for(int j = 0; j < rows; j++ ) {
            inputLines.add(scanner.nextLine());
        }
        setMatrix(MatrixUtil.makeMatrix(inputLines));

        System.out.print("Enter the scaling factor: ");
        scalingFactor = Double.parseDouble(scanner.nextLine().split("\\s+")[0]);
    }

    private void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        scalarProduct();
    }

    @Override
    void displayResults() {
        System.out.println("The multiplication of matrix by a constant is: ");
        MatrixUtil.displayMatrix(resultMatrix);
    }

    public void scalarProduct() {
        resultMatrix = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            resultMatrix[i] = new double[matrix[i].length];
            for(int j = 0; j < matrix[i].length; j++) {
                resultMatrix[i][j] = scalingFactor*matrix[i][j];
            }
        }
    }

}

class MatrixMultiplicationOperator extends MatrixOperator {

    private double[][] matrixA;
    private double[][] matrixB;

    public MatrixMultiplicationOperator(Scanner scanner) {
        super(scanner);
        operation = MatrixOperation.MATRIX_MULT;
    }
    @Override
    void setInputs() {
        for(int i = 0; i < 2; i++) {
            List<String> inputLines = new ArrayList<>();
            System.out.printf("Enter the size of the %s matrix: ", i==0 ? "first" : "second");
            String inputLine = scanner.nextLine();
            inputLines.add(inputLine);
            int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
            System.out.printf("Enter the %s matrix:%n", i==0 ? "first" : "second");
            for(int j = 0; j < rows; j++ ) {
                inputLines.add(scanner.nextLine());
            }
            setMatrix(i, MatrixUtil.makeMatrix(inputLines));
        }
    }

    private void setMatrix(int index, double[][] matrix) {
        if(index == 0)
            matrixA = matrix;
        else
            matrixB = matrix;
    }

    @Override
    public void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        matrixMultiply();
    }

    @Override
    void displayResults() {
        MatrixUtil.displayMatrix(resultMatrix);
    }

    public void matrixMultiply() {
        MatrixUtil.validateMatricesBeforeMultiplying(matrixA, matrixB);
        resultMatrix = new double[matrixA.length][];

        int m = matrixA.length, n = matrixB.length, k = MatrixUtil.getColumns(matrixB);
        for(int i = 0; i < m; i++) {
            resultMatrix[i] = new double[matrixB[i].length];
            for(int j = 0; j < k; j++) {
                double sum = 0;
                for(int f = 0; f < n; f++) {
                    sum = sum + matrixA[i][f]*matrixB[f][j];
                }
                resultMatrix[i][j] = sum;
            }
        }
    }

}

class MatrixTransposeOperator extends MatrixOperator {
    private double[][] matrix;
    private TransposeOperation transposeType;

    public MatrixTransposeOperator(Scanner scanner) {
        super(scanner);
        operation = MatrixOperation.TRANSPOSE;
    }

    public MatrixTransposeOperator() { }

    @Override
    void setInputs() {
        Arrays.stream(TransposeOperation.values())
                .map(TransposeOperation::getStatement)
                .forEach(System.out::println);
        System.out.print("Your choice: ");
        setTransposeType(Integer.parseInt(scanner.nextLine().split("\\s+")[0]));
        List<String> inputLines = new ArrayList<>();
        System.out.printf("Enter the size of the matrix: ");
        String inputLine = scanner.nextLine();
        inputLines.add(inputLine);
        int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
        System.out.printf("Enter the matrix:%n");
        for(int j = 0; j < rows; j++ ) {
            inputLines.add(scanner.nextLine());
        }
        setMatrix(MatrixUtil.makeMatrix(inputLines));
    }

    public void setTransposeType(int tType) {
        transposeType = TransposeOperation.getTransposeOperation(tType);
    }
    public void setTransposeType(TransposeOperation transposeType) {
        this.transposeType = transposeType;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        transpose();
    }

    @Override
    void displayResults() {
        MatrixUtil.displayMatrix(resultMatrix);
    }

    public void transpose() {
        int rows = matrix.length;
        int cols = MatrixUtil.getColumns(matrix);
        if(rows != cols)
            throw new RuntimeException("Transpose cannot be created");
        switch(transposeType) {
            case MAIN_DIAGONAL:
                transposeOnMainDiagonal();
                break;
            case SIDE_DIAGONAL:
                transposeOnSideDiagonal();
                break;
            case VERTICAL_LINE:
                transposeOnVerticalLine();
                break;
            case HORIZONTAL_LINE:
                transposeOnHorizontalLine();
                break;
        }
    }

    private void transposeOnMainDiagonal() {
        resultMatrix = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            resultMatrix[i] = new double[matrix[i].length];
            for(int j = 0; j < matrix[i].length; j++) {
                resultMatrix[i][j] = matrix[j][i];
            }
        }
    }

    private void transposeOnSideDiagonal() {
        int rows = matrix.length;
        int cols = MatrixUtil.getColumns(matrix);

        resultMatrix = new double[rows][];
        for(int i = 0; i < rows; i++) {
            resultMatrix[i] = new double[cols];
            for(int j = 0; j < cols; j++) {
                if(i != cols-1-j) {
                    resultMatrix[i][j] = matrix[rows-1-j][cols-1-i];
                } else {
                    resultMatrix[i][j] = matrix[i][j];
                }
            }
        }
    }

    private void transposeOnVerticalLine() {
        int rows = matrix.length;
        int cols = MatrixUtil.getColumns(matrix);
        resultMatrix = new double[rows][];
        for (int i = 0; i < rows; i++) {
            resultMatrix[i] = new double[cols];
            for (int j = 0; j < cols; j++) {
                if(cols%2 == 1 && j == cols/2) {
                    resultMatrix[i][j] = matrix[i][j];
                } else
                    resultMatrix[i][j] = matrix[i][cols-1-j];
            }
        }
    }

    private void transposeOnHorizontalLine() {
        int rows = matrix.length;
        int cols = MatrixUtil.getColumns(matrix);
        resultMatrix = new double[rows][];
        for (int i = 0; i < rows; i++) {
            resultMatrix[i] = new double[cols];
            for (int j = 0; j < cols; j++) {
                if(rows%2 == 1 && i == rows/2) {
                    resultMatrix[i][j] = matrix[i][j];
                } else
                    resultMatrix[i][j] = matrix[rows-1-i][j];
            }
        }
    }



}

class MatrixDeterminantOperator extends MatrixOperator {

    private double[][] matrix;
    private double determinant;

    public MatrixDeterminantOperator() {
    }

    public MatrixDeterminantOperator(Scanner scanner) {
        super(scanner);
        operation = MatrixOperation.DETERMINANT;
    }

    @Override
    public void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        determinant = MatrixUtil.calculateDeterminant(matrix);
        resultMatrix = new double[][]{{determinant}};
    }



    @Override
    public void setInputs() {
        List<String> inputLines = new ArrayList<>();
        System.out.printf("Enter the size of the matrix: ");
        String inputLine = scanner.nextLine();
        inputLines.add(inputLine);
        int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
        System.out.printf("Enter the matrix:%n");
        for(int j = 0; j < rows; j++ ) {
            inputLines.add(scanner.nextLine());
        }
        setMatrix(MatrixUtil.makeMatrix(inputLines));
    }
    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public void displayResults() {
        System.out.println(determinant);
    }
}

class MatrixInverseOperator extends MatrixOperator {
    private double[][] matrix;
    private MatrixTransposeOperator transposeOperator;
    private MatrixDeterminantOperator determinantOperator;

    public MatrixInverseOperator(Scanner scanner) {
        super(scanner);
        this.operation = MatrixOperation.INVERSE;
    }

    @Override
    void setInputs() {
        List<String> inputLines = new ArrayList<>();
        System.out.printf("Enter the size of the matrix: ");
        String inputLine = scanner.nextLine();
        inputLines.add(inputLine);
        int rows = Integer.parseInt(inputLine.split("\\s+")[0]);
        System.out.printf("Enter the matrix:%n");
        for(int j = 0; j < rows; j++ ) {
            inputLines.add(scanner.nextLine());
        }
        setMatrix(MatrixUtil.makeMatrix(inputLines));
        transposeOperator = new MatrixTransposeOperator();
        transposeOperator.setMatrix(matrix);
        transposeOperator.setTransposeType(TransposeOperation.MAIN_DIAGONAL);

        determinantOperator = new MatrixDeterminantOperator();
        determinantOperator.setMatrix(matrix);
    }

    private void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public void execute(boolean isInputSetAlready) {
        super.execute(isInputSetAlready);
        invertMatrix();
    }

    private void invertMatrix() {
        determinantOperator.execute(true);
        double determinant = determinantOperator.getResultMatrix()[0][0];
        transposeOperator.execute(true);
        double[][] result = transposeOperator.getResultMatrix();

        double[][] adjMatrix = new double[result.length][];
        resultMatrix = new double[result.length][];
        for(int i = 0; i < result.length; i++) {
            adjMatrix[i] = new double[result[i].length];
            resultMatrix[i] = new double[result[i].length];
            for(int j = 0; j < result[i].length; j++) {
                determinantOperator.setMatrix(MatrixUtil.makeSubMatrix(result, i, j));
                determinantOperator.execute(true);
                adjMatrix[i][j] = determinantOperator.getResultMatrix()[0][0];
                resultMatrix[i][j] = adjMatrix[i][j]/determinant * MatrixUtil.mult(-1, i+j);
                if(resultMatrix[i][j] == 0.0 || resultMatrix[i][j] == -0.0)
                    resultMatrix[i][j] = 0.0;
            }
        }
    }

    @Override
    void displayResults() {
        MatrixUtil.displayMatrix(resultMatrix);
    }
}
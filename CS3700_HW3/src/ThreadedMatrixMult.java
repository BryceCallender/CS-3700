import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ThreadedMatrixMult {
    public static void main(String[] args) {
        float[][] first = { {1,2,3} , {4,5,6}, {7,8,9}, {1,2,3}};
        float[][] second = { {1,2,3,4}, {3,4,5,6}, {5,6,7,8}};

        int m,n,p;

        m = first.length;
        n = first[0].length;
        p = second[0].length;

        float[][] result = new float[m][p];

        matMult(first,second,result, m,n,p);

        first = generateMatrix(25,25);
        second = generateMatrix(25,25);
        result = generateMatrix(25,25);

        matMult(first,second,result,25,25,25);

        first = generateMatrix(50,50);
        second = generateMatrix(50,50);
        result = generateMatrix(50,50);

        matMult(first,second,result,50,50,50);

        first = generateMatrix(100,100);
        second = generateMatrix(100,100);
        result = generateMatrix(100,100);

        matMult(first,second,result,100,100,100);

        first = generateMatrix(200,200);
        second = generateMatrix(200,200);
        result = generateMatrix(200,200);

        matMult(first,second,result,200,200,200);

        first = generateMatrix(1000,1000);
        second = generateMatrix(1000,1000);
        result = generateMatrix(1000,1000);

        matMult(first,second,result,1000,1000,1000);
    }

    public static void matMult(float[][] A, float [][] B, float[][] C, int m, int n, int p) {
        int[] numThreads = {1,2,4,8};

        List<MatrixMultThread> matrixMultThreads = new ArrayList<>();

        int endRow = 0;
        int startRow = 0;
        int threadIndex = 0;

        for (int threadCount: numThreads) {
            System.out.format("%n%nPerforming Matrix Multiplication with %d thread(s) with a final matrix size of %dx%d%n", threadCount, m,p);

            long begin = System.nanoTime();

            for (int i = 0; i < threadCount; i++) {
                endRow += m / threadCount;

                if(endRow > m) {
                    endRow = m + 1;
                }

                matrixMultThreads.add(new MatrixMultThread(A, B, C, startRow, endRow, n, p));
                matrixMultThreads.get(threadIndex).start();
                startRow = endRow;
                threadIndex++;
            }

            matrixMultThreads.forEach(matrixMultThread -> {
                try {
                    matrixMultThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            startRow = 0;
            endRow = 0;

            long end = System.nanoTime();

            System.out.println("It took " + (end-begin) + "ns.");

            clearMatrix(C);
        }

    }

    public static void clearMatrix(float[][] matrix) {
        for (float[] floats : matrix) {
            Arrays.fill(floats, 0.0f);
        }
    }

    public static float[][] generateMatrix(int row, int col) {
        float[][] newMatrix = new float[row][col];
        Random random = new Random();

        for (float[] rows: newMatrix) {
            Arrays.fill(rows, random.nextInt(20) + 1);
        }

        return newMatrix;
    }

    public static void displayProduct(float[][] product) {
        System.out.println("Product of two matrices is: ");
        for(float[] row : product) {
            for (float column : row) {
                System.out.print(column + "  ");
            }
            System.out.println();
        }
    }
}

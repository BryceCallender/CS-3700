public class ThreadedMatrixMult {
    public static void main(String[] args) {
        float[][] first = { {1,2,3} , {4,5,6}};
        float[][] second = { {1,2}, {3,4}, {5,6}};

        int m,n,p;

        m = first.length;
        n = first[0].length;
        p = second[0].length;

        float[][] result = new float[m][p];

        matMult(first,second,result, m,n,p);

        displayProduct(result);
    }

    public static void matMult(float[][] A, float [][]B, float[][]C, int m, int n, int p) {
        int numThreads[] = {1,2,4,8};

        long begin = System.currentTimeMillis();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("It took " + (end-begin) + "ms.");
    }

    public static void displayProduct(float[][] product) {
        System.out.println("Product of two matrices is: ");
        for(float[] row : product) {
            for (float column : row) {
                System.out.print(column + "    ");
            }
            System.out.println();
        }
    }
}

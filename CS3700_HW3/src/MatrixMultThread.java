public class MatrixMultThread extends Thread {
    private float[][] A;
    private float[][] B;
    private float[][] C;

    private int start;
    private int end;
    private int numRows;
    private int numCols;

    MatrixMultThread(float[][] A, float [][]B, float[][]C, int startRow, int endRow, int numRows, int numCols) {
        this.A = A;
        this.B = B;
        this.C = C;

        this.start = startRow;
        this.end = endRow;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    @Override
    public void run() {
        if(start > numRows) {
            return;
        }

        for (int i = start; i < end; i++) {
            for (int j = 0; j < numCols; j++) {
                for (int k = 0; k < numRows; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }
}

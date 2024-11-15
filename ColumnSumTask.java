import java.util.concurrent.RecursiveTask;

class ColumnSumTask extends RecursiveTask<int[]> {
    private final int start;
    private final int end;
    private final int [][] matrix;

    ColumnSumTask(int [][]matrix, int start, int end) {
        this.start = start;
        this.end = end;
        this.matrix = matrix;
    }

    @Override
    protected int[] compute() {
        if (end - start <= 2) {
            int[] result = new int[matrix[0].length];
            for (int j = start; j < end; j++) {
                for (int[] ints : matrix) {
                    result[j] += ints[j];
                }
            }
            return result;
        } else {
            int mid = (start + end) / 2;
            ColumnSumTask leftTask = new ColumnSumTask(matrix, start, mid);
            ColumnSumTask rightTask = new ColumnSumTask(matrix, mid, end);
            leftTask.fork();
            int[] rightResult = rightTask.compute();
            int[] leftResult = leftTask.join();

            for (int i = 0; i < matrix[0].length; i++) {
                rightResult[i] += leftResult[i];
            }
            return rightResult;
        }
    }
}
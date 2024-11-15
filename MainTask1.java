import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class MainTask1 {
    private static int[][] matrix;
    private static int[] columnSums;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Введіть мінімальне значення діапазону: ");
        int minRange = sc.nextInt();

        System.out.print("Введіть максимальне значення діапазону: ");
        int maxRange = sc.nextInt();

        if(minRange > maxRange){
            System.out.println("Некоректно введені дані.");
            return;
        }

        System.out.print("Введіть розмір матриці:");
        int sizeOfMatrix = sc.nextInt();

        generateMatrix(sizeOfMatrix, minRange, maxRange);
        System.out.println("Утворена матриця:");
        printMatrix();

        System.out.println("\nWork Stealing:");
        long startTime = System.currentTimeMillis();
        columnSums = computeColumnSumForkJoin();
        long endTime = System.currentTimeMillis();
        System.out.println("Суми колонок: ");
        printColumnSums();
        System.out.println("Час виконання: " + (endTime - startTime) + "ms");

        System.out.println("\nWork Dealing:");
        startTime = System.currentTimeMillis();
        columnSums = computeColumnSumExecutorService();
        endTime = System.currentTimeMillis();
        System.out.println("Суми колонок: ");
        printColumnSums();
        System.out.println("Час виконання: " + (endTime - startTime) + "ms");
    }

    private static void generateMatrix(int size, int min, int max) {
        matrix = new int[size][size];
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] = random.nextInt(min, max);
    }

    private static void printMatrix() {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private static void printColumnSums() {
        for (int sum : columnSums) {
            System.out.print(sum + " ");
        }
        System.out.println();
    }

    private static int[] computeColumnSumForkJoin() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ColumnSumTask mainTask = new ColumnSumTask(matrix, 0, matrix[0].length);
        return forkJoinPool.invoke(mainTask);
    }

    private static int[] computeColumnSumExecutorService() {
        int cols = matrix[0].length;
        int[] result = new int[cols];
        ExecutorService executorService = Executors.newFixedThreadPool(cols);

        for (int j = 0; j < cols; j++) {
            int col = j;
            executorService.submit(() -> {
                int sum = 0;
                for (int[] ints : matrix)
                    sum += ints[col];

                result[col] = sum;
            });
        }
        executorService.shutdown();
        return result;
    }
}



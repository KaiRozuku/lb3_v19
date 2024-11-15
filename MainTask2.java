import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class MainTask2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть шлях до директорії: ");
        String directoryPath = scanner.nextLine();
        //D:\II курс
        System.out.print("Введіть мінімальний розмір файлу (в MB): ");
        long size = scanner.nextLong();
        if (size <= 0) {
            System.out.println("Некоректні дані");
            return;
        }
        ForkJoinPool pool = new ForkJoinPool();

        FileSearchTask task = new FileSearchTask(new File(directoryPath), size);
        int result = pool.invoke(task);

        System.out.println("Кількість файлів, більших за " + size + " мегабайт: " + result);
    }
}

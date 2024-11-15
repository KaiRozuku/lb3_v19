import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class FileSearchTask extends RecursiveTask<Integer> {
    private final File directory;
    private final long minSize;

    public FileSearchTask(File directory, long minSize) {
        this.directory = directory;
        this.minSize = minSize * (1024 * 1024);
    }

    @Override
    protected Integer compute() {
        if (!directory.exists()) {
            System.out.println("❌ Шлях " + directory.getPath() + " не знайдено. Пошук неможливий.");
            return 0;
        }
        if (!directory.isDirectory()) {
            System.out.println("🚫 " + directory.getPath() + " - це не директорія!");
            return 0;
        }
        System.out.println("📂 Перевіряємо папку: " + directory.getName());
        int count = 0;
        List<FileSearchTask> subTasks = new ArrayList<>();

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        FileSearchTask subTask = new FileSearchTask(file, minSize);
                        subTask.fork();
                        subTasks.add(subTask);
                    } else if (file.isFile() && file.length() > minSize)
                        count++;
                }
            }

            for (FileSearchTask subTask : subTasks)
                count += subTask.join();
        }
        return count;
    }
}
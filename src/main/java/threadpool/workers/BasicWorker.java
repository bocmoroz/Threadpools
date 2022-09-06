package threadpool.workers;

import java.util.concurrent.BlockingQueue;

public class BasicWorker implements Runnable {

    private static int basicCountTask;

    private int countTask;

    private final BlockingQueue<Runnable> tasks;

    public BasicWorker(BlockingQueue<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();

        try {
            while (!thread.isInterrupted()) {
                Runnable runnable = tasks.take();
                runnable.run();
                countTask++;
                basicCountTask++;
            }

            printReport(thread);

        } catch (InterruptedException e) {
            printReport(thread);
        }

    }

    private void printReport(Thread thread) {
        System.out.println("Основной поток " + thread.getName() + " остановлен! " +
                "Выполнил заданий: " + countTask + " Всего заданий выполнено основными потоками: " + basicCountTask);
    }
}

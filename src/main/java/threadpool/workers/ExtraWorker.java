package threadpool.workers;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ExtraWorker implements Runnable {

    private static int extraCountTask;

    private int countTask;

    private final int minThreads;
    private final BlockingQueue<Runnable> tasks;
    private final List<Thread> extraThreadList;


    public ExtraWorker(int minThreads, BlockingQueue<Runnable> tasks, List<Thread> extraThreadList) {
        this.minThreads = minThreads;
        this.tasks = tasks;
        this.extraThreadList = extraThreadList;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();

        try {
            while (!thread.isInterrupted()) {

                if (tasks.size() <= minThreads) {
                    extraThreadList.remove(thread);
                    thread.interrupt();
                }

                Runnable runnable = tasks.take();
                runnable.run();
                countTask++;
                extraCountTask++;
            }

            printReport(thread);

        } catch (InterruptedException e) {
            printReport(thread);
        }

    }

    private void printReport(Thread thread) {
        System.out.println("Вспомогательный поток " + thread.getName() + " остановлен! " +
                "Выполнил заданий: " + countTask + " Всего заданий выполнено вспомогательными потоками: " + extraCountTask);
    }

}

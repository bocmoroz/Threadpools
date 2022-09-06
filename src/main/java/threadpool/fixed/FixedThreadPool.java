package threadpool.fixed;

import threadpool.workers.BasicWorker;
import threadpool.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPool implements ThreadPool {

    private int basicThreadCount;

    private boolean isStartedPool;
    private boolean isStoppedPool;

    private final BlockingQueue<Runnable> tasks;
    private final List<Thread> threadList;

    public FixedThreadPool(int countOfThreads) throws IllegalArgumentException {
        if (countOfThreads < 1) {
            throw new IllegalArgumentException("Количество потоков - положительное число!");
        }

        tasks = new LinkedBlockingQueue<>();
        threadList = new ArrayList<>(countOfThreads);

        for (int i = 0; i < countOfThreads; i++) {
            BasicWorker basicWorker = new BasicWorker(tasks);
            threadList.add(new Thread(basicWorker));
            basicThreadCount++;
        }
    }

    @Override
    public void start() throws IllegalThreadStateException {
        if (isStartedPool) {
            throw new IllegalThreadStateException("Нельзя запустить ThreadPool больше одного раза");
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        isStartedPool = true;
    }

    @Override
    public void execute(Runnable task) throws IllegalAccessException {
        if (isStoppedPool) {
            throw new IllegalAccessException("Невозможно добавить задание после остановки ThreadPool!");
        }

        tasks.add(task);
    }

    @Override
    public void stop() {
        isStoppedPool = true;

        if (tasks.size() > 0) {
            System.out.println(getClass().getSimpleName() + " остановлен. Ожидание завершения всех заданий из очереди.");
            while (tasks.size() > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("\nВсе задания из очереди завершены.");

        }

        System.out.println("Ожидание остановки всех потоков.");

        for (Thread thread : threadList) {
            thread.interrupt();
        }

        System.out.println("Всего было создано потоков: " + basicThreadCount);

    }

}

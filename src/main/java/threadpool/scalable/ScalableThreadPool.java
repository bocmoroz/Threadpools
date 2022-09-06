package threadpool.scalable;

import threadpool.ThreadPool;
import threadpool.workers.BasicWorker;
import threadpool.workers.ExtraWorker;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScalableThreadPool implements ThreadPool {

    private int basicThreadCount;
    private int extraThreadCount;

    private boolean isStartedPool;
    private boolean isStoppedPool;

    private final int minThreads;
    private final int maxThreads;

    private final BlockingQueue<Runnable> tasks;
    private final List<Thread> basicThreadList;
    private final List<Thread> extraThreadList;

    public ScalableThreadPool(int minThreads, int maxThreads) throws IllegalArgumentException {
        if (minThreads < 1 || maxThreads < minThreads) {
            throw new IllegalArgumentException("Введено некорректное количество потоков!");
        }

        this.minThreads = minThreads;
        this.maxThreads = maxThreads;

        tasks = new LinkedBlockingQueue<>();
        basicThreadList = new ArrayList<>(minThreads);
        extraThreadList = new ArrayList<>(maxThreads - minThreads);

        for (int i = 0; i < minThreads; i++) {
            BasicWorker basicWorker = new BasicWorker(tasks);
            basicThreadList.add(new Thread(basicWorker));
            basicThreadCount++;
        }

    }

    @Override
    public void start() {
        if (isStartedPool) {
            throw new IllegalThreadStateException("Нельзя запустить ThreadPool больше одного раза");
        }

        for (Thread thread : basicThreadList) {
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

        if (tasks.size() > minThreads && extraThreadList.size() < (maxThreads - minThreads)) {
            ExtraWorker extraWorker = new ExtraWorker(minThreads, tasks, extraThreadList);
            Thread thread = new Thread(extraWorker);
            thread.start();
            extraThreadList.add(thread);
            extraThreadCount++;
        }

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

        for (Thread thread : basicThreadList) {
            thread.interrupt();
        }

        System.out.println("Всего было создано потоков " + (basicThreadCount + extraThreadCount) +
                ".(С учётом создания/удаления вспомогательных) " +
                " Из них: основных - " + basicThreadCount + ", вспомогательных - " + extraThreadCount);
    }
}

package threadpool;

public interface ThreadPool {

    void start() throws IllegalThreadStateException;

    void execute(Runnable runnable) throws IllegalAccessException;

    void stop();
}

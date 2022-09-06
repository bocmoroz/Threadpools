import fortest.TestRunnable;
import threadpool.fixed.FixedThreadPool;
import threadpool.ThreadPool;

public class FixedMain {

    public static void main(String[] args) {

        System.out.println("*** FIXED THREAD POOL ***");

        ThreadPool pool1 = new FixedThreadPool(10);

        try {
            pool1.start();

            for (int i = 0; i < 200; i++) {
                pool1.execute(new TestRunnable(String.valueOf(i)));
            }

            pool1.execute(new TestRunnable("Extra task"));

            pool1.stop();

        } catch (IllegalThreadStateException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}

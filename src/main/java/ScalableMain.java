import fortest.TestRunnable;
import threadpool.ThreadPool;
import threadpool.scalable.ScalableThreadPool;

public class ScalableMain {

    public static void main(String[] args) {

        System.out.println("*** SCALABLE THREAD POOL ***");

        ThreadPool pool2 = new ScalableThreadPool(2, 10);

        try {
            pool2.start();

            for (int i = 0; i < 1000000; i++) {
                pool2.execute(new TestRunnable(String.valueOf(i)));
            }

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            pool2.execute(new TestRunnable("Extra task"));

            pool2.stop();

        } catch (IllegalThreadStateException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

    }
}

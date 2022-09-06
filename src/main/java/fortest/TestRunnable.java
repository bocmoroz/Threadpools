package fortest;

public class TestRunnable implements Runnable {

    private final String name;

    public TestRunnable(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Номер исполняющего треда: " + Thread.currentThread().getName() + ", имя задания: " + name);
    }
}

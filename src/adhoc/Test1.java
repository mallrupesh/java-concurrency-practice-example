package adhoc;

public class Test1 {

    public static void main(String[] args) {
        // Passing Runnable
        Runnable myRunnable = () -> System.out.println("Runnable 1: " + Thread.currentThread().getName());
        new Thread(myRunnable).start();

        // Creating Runnable inside
        new Thread(() -> System.out.println("Runnable 2: " + Thread.currentThread().getName())).start();

        // Creating Runnable inside that run multiple Runnable inside
        new Thread(() -> {
            for(int i = 0; i < 10; i++) {
                myRunnable.run();
            }
        }).start();
    }
}
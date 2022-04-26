package threadblocking;

import java.util.concurrent.atomic.AtomicBoolean;

public class Ex2UsingLocks {

    private static final AtomicBoolean LOCK = new AtomicBoolean(false);
    private static String name;
    private static int counter = 0;
    private static String result;

    public static void main(String[] args) {

        doSomeTask();

        synchronized (LOCK) {
            while(!LOCK.get()) {   // wait for InitThread notify
                try {              // if main waits after notify has already been called
                    LOCK.wait();   // signal is lost and main waits forever
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        System.out.println("Finished in main: " + Thread.currentThread().getName());
    }

    /**
     * This e.g. is to demo that the statements/ functions inside Runnable block
     * are executed sequentially but if new Threads created in the functions
     * inside Runnable, then we have to apply strategy to block the outer Thread
     * until all the Worker threads complete and return
     *
     * In this e.g., computeCon() creates new Threads
     */
    public static void doSomeTask() {
        Thread initThread = new Thread(() -> {      // Init Thread
            initCon();
            computeCon();
            blockCon();
            finishCon();
        });
        initThread.start();
    }

    public static void initCon() {
        name = "Start";
        counter = 0;
        System.out.println(name);
    }

    public static void computeCon() {
        for(int i = 0; i < 10; i++) {   // start Worker Threads
            new Thread(() -> {
                synchronized (LOCK) {
                    counter++;
                    System.out.println(counter);
                    LOCK.notifyAll();   // worker threads notify the InitThread
                }
            }).start();
        }
    }

    // This will become the blocker function
    public static void blockCon() {
        synchronized (LOCK) {
            while(counter < 10) {
                try{
                    LOCK.wait();        // InitThread waits until the counter reaches 10
                }catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public static void finishCon() {
        name = "Finished";
        result = name + ":" + counter;
        System.out.println(result);

        synchronized (LOCK) {
            LOCK.set(true);
            LOCK.notifyAll();   // InitThread sets LOCK true and notify main
        }
    }
}

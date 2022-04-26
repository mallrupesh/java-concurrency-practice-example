package threadblocking;

import java.util.concurrent.atomic.AtomicInteger;

public class Ex1UsingBusyWaiting {
    static String name;
    static int num = 0;
    static volatile AtomicInteger counter = new AtomicInteger(0);
    static String result;

    public static void main(String[] args) throws InterruptedException {


        //Thread.sleep(1000);

        //singleThreadSequential();
        //nestedThreadsSequential();


        System.out.println("Finished in main: " + Thread.currentThread().getName());
    }


    /**
     * This e.g. is to demo that the statements/ functions inside Runnable block
     * are executed sequentially unless new Threads are created in the function
     * inside the Runnable block
     *
     * In this e.g., no new Threads created by the functions inside Runnable
     */
    public static void singleThreadSequential() {
        new Thread(() -> {
            init();
            compute();
            block();
            finish();
        }).start();
    }

    public static void init() {
        name = "Start";
        num = 0;
        System.out.println(name);
    }

    public static void compute() {
        for(int i = 0; i < 10; i++) {
            num++;
            System.out.println(num);
        }
    }

    public static void block() {
        while(true) {
            if(num >= 10){
                System.out.println("Counter done");
                break;
            }else {
                System.out.println("waiting");
            }
        }
    }

    public static void finish() {
        name = "Finished in worker thread: " + Thread.currentThread().getName();
        result = name + ":" + num;
        System.out.println(result);
    }


    /**
     * This e.g. is to demo that the statements/ functions inside Runnable block
     * are executed sequentially but if new Threads created in the functions
     * inside Runnable, then we have to apply strategy to block the outer Thread
     * until all the Worker threads complete and return
     *
     * In this e.g., computeCon() creates new Threads
     */
    public static void nestedThreadsSequential() {
        new Thread(() -> {      // Init Thread
            initCon();
            computeCon();
            blockCon();
            finishCon();
        }).start();
    }

    public static void initCon() {
        name = "Start";
        counter.set(0);
        System.out.println(name);
    }

    public static void computeCon() {
        for(int i = 0; i < 10; i++) {   // Worker Threads
            new Thread(() -> counter.getAndIncrement()).start();
        }
    }

    // This will become the blocker function
    public static void blockCon() {
        while(true) {
            if(counter.get() >= 10){
                break;
            }else {
                System.out.println("waiting");
            }
        }
    }

    public static void finishCon() {
        name = "Finished";
        result = name + ":" + counter.get();
        System.out.println(result);
    }
}

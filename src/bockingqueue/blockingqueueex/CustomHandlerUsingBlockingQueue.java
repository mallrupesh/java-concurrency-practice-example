package bockingqueue.blockingqueueex;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Handler;

public class CustomHandlerUsingBlockingQueue {

    private static final int COUNT = 5;
    private static CustomHandler handler = new CustomHandler();


    public static void main(String[] args) {

        Runnable[] runnables = new Runnable[3];

        Runnable run1 = () -> {
            for(int i = 0; i < COUNT; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Iteration: " + i);
            }
        };

        Runnable run2 = () -> {
            for(int i = 0; i < COUNT; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Iteration: " + i);
            }
        };

        Runnable run3 = () -> {
            for(int i = 0; i < COUNT; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Iteration: " + i);
            }
        };

        runnables[0] = run1;
        runnables[1] = run2;
        runnables[2] = run3;

        for(Runnable runnable : runnables) {
            handler.post(runnable);
        }
    }

    public static void deployJob() {
        handler.post(() -> {
            for(int i = 0; i < COUNT; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Iteration: " + i);
            }
        });
    }
}


class CustomHandler {

    private final Runnable POISON = () -> {};  // empty runnable to signal thread termination

    private final BlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    public CustomHandler() {
        initMainWorkerThread();
    }

    public void post(Runnable job) {
        mQueue.add(job);
    }

    // Init the MainWorker thread and make it ready to take jobs
    // All the jobs should be executed within this MainWorker in a synchronous manner
    private void initMainWorkerThread() {
        new Thread(() -> {
            while(true) {
                Runnable runnable;
                try {
                    runnable = mQueue.take();       // take() make this Thread wait until element available in mQueue
                } catch (InterruptedException e) {  // when mQueue is empty
                    return;
                }
                if(runnable == POISON) {
                    return;
                }
                runnable.run();
            }
        }).start();
    }

    public void stop() {
        mQueue.clear();
        mQueue.add(POISON);
    }
}

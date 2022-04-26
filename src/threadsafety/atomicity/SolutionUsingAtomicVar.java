package threadsafety.atomicity;

import java.util.concurrent.atomic.AtomicInteger;

public class SolutionUsingAtomicVar {

    public static void main(String[] args) {
        CountPrinter countPrinter = new CountPrinter();

        Thread initThread = new Thread(() -> countPrinter.startCount());
        initThread.start();             // start init thread

        try {
            initThread.join();          // InitThread completes and joins main Thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Count: " + countPrinter.getmCount());
    }

    public static class CountPrinter {

        private static final int COUNT_UP_TO = 1000;
        private static final int NUM_OF_COUNTER_THREADS = 100;

        private volatile AtomicInteger mCount = new AtomicInteger(0);

        private void startCount() {
            mCount.set(0);
            System.out.println("Starting Counter...");

            for (int i = 0; i < NUM_OF_COUNTER_THREADS; i++) { // up to 100 threads created
                startWorkerThread();
            }
        }

        /**
         * This method creates Worker Threads and each thread runs a single Runnable
         * This method is called in startCount(), so multiple Threads are created
         */
        private void startWorkerThread() {
            Thread worker = new Thread(() -> {
                System.out.println("Thread: " + Thread.currentThread().getName() + " Count:" + mCount.get());
                for (int i = 0; i < COUNT_UP_TO; i++) {     // each thread created will increment count up to 1000
                    mCount.getAndIncrement();
                }
            });

            worker.start();         // Worker Thread starts

            try {
                worker.join();      // Worker Thread completes and joins the Init Thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * This thread creates a single MainWorker thread inside which multiple Runnables run
         */
        public void postRunnables() {
            mCount.set(0);
            System.out.println("Starting Counter...");

            Thread mainWorkerThread = new Thread(() -> {
                for (int i = 0; i < NUM_OF_COUNTER_THREADS; i++) { // up to 100 threads created
                    Runnable runnable = () -> {
                        System.out.println("Thread: " + Thread.currentThread().getName() + " Count:" + mCount.get());
                        for (int i1 = 0; i1 < COUNT_UP_TO; i1++) {     // each thread created will increment count up to 1000
                            mCount.getAndIncrement();
                        }
                    };
                    runnable.run();
                }
            });

            mainWorkerThread.start();               // MainWorker thread starts
            try {
                mainWorkerThread.join();            // finally, MainWorker Thread completes and joins Init Thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public AtomicInteger getmCount() {
            return mCount;
        }
    }
}

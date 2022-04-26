package threadsafety.atomicity;

public class AtomicityProblemTest {

    public static void main(String[] args) {

        CountPrinter countPrinter = new CountPrinter();
        countPrinter.startCount();

    }


    public static class CountPrinter {

        private static final int COUNT_UP_TO = 100;
        private static final int NUM_OF_COUNTER_THREADS = 10;

        private int mCount;

        private void startCount() {
            mCount = 0;
            System.out.println("Starting Counter...");
            for (int i = 0; i < NUM_OF_COUNTER_THREADS; i++) { // up to 10 threads created
                startCountThread();
            }
            System.out.println("Count: " + mCount);
        }

        private void startCountThread() {
            new Thread(() -> {
                System.out.println("Thread: " + Thread.currentThread().getName() + " Count:" + mCount);
                for (int i = 0; i < COUNT_UP_TO; i++) {     // each thread created will increment count up to 100
                    mCount++;
                }
            }).start();
        }
    }
}



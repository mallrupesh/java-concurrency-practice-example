package threadsafety.visibility;

public class VisibilityProblemTest {

    private static int sCount = 0;      // shared resource/state

    public static void main(String[] args) {

        // Main thread start 2 new Threads
        // Producer writes into shared var
        // Consumer reads shared var
        // There is no visibility between Write and Read in these multiple threads
        // Hence, need to make the shared var volatile
        new Consumer().start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }
        new Producer().start();
    }

    static class Consumer extends Thread {
        // sCount is not coming from memory but from the local cache inside this thread
        // the locally cached value never changes
        // therefore, there is no change in sCount and the loop executes infinitely
        @Override
        public void run() {
            int localValue = -1;
            while (true) {
                if (localValue != sCount) {
                    System.out.println("Consumer: detected count change " + sCount);
                    localValue = sCount;
                }
                if (sCount >= 5) {
                    break;
                }
            }
            System.out.println("Consumer: terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while (sCount < 5) {
                int localValue = sCount;
                localValue++;
                System.out.println("Producer: incrementing count to " + localValue);
                sCount = localValue;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Producer: terminating");
        }
    }
}

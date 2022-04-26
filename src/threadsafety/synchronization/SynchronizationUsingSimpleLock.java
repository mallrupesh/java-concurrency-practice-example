package threadsafety.synchronization;

public class SynchronizationUsingSimpleLock {

    private static final Object LOCK = new Object(); // LOCK object

    private volatile static int sCount = 0;         // shared resource/state

    public static void main(String[] args) {
        new Consumer().start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }
        new Producer().start();
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            int localValue = -1;
            while (true) {
                // Synchronized block will be executed as Atomic operation
                synchronized (LOCK) {
                    if (localValue != sCount) {
                        System.out.println("Consumer: detected count change " + sCount);
                        localValue = sCount;
                    }
                    if (sCount >= 5) {
                        break;
                    }
                }// release lock
            }
            System.out.println("Consumer: terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                // Synchronized block will be executed as Atomic operation
                synchronized (LOCK) {
                    if(sCount >= 5) {
                        break;
                    }
                    int localValue = sCount;
                    localValue++;
                    System.out.println("Producer: incrementing count to " + localValue);
                    sCount = localValue;
                }// release lock
            }
            System.out.println("Producer: terminating");
        }
    }
}

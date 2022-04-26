package happensbefore;

/**
 * The gist of the example is that there is no Happens-Before guarantee
 * in which thread acquires the LOCK first
 * It is the OS that decides which Thread acquires the LOCK first
 */
public class NoHappensBeforeLock {

    private static final Object LOCK = new Object();

    private static int sCount = 0;  // this assignment happen-before Consumer.start()
                                    // hence, sCount value is visible to Consumer
                                    // additionally the subsequent Thread are created from this Thread (main)
    public static void main(String[] args) {
        new Consumer().start();     // Consumer.start() happens-before the Consumer's run()
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }
        new Producer().start();    // Producer.start() happens-before the Consumer's run()
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            int localValue = -1;
            while(true) {
                synchronized (LOCK) {
                    if (localValue != sCount) {
                        System.out.println("Consumer: detected count change " + sCount);
                        localValue = sCount;
                    }
                    if (sCount >= 5) {
                        break;
                    }
                }
            }
            System.out.println("Consumer: terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while(true) {
                synchronized (LOCK) {
                    if(sCount >= 5) {
                        break;
                    }
                    int localValue = sCount;
                    localValue++;
                    System.out.println("Producer: incrementing count to " + localValue);
                    sCount = localValue;
                } // release LOCK

                // Placed outside the synchronized block since Thread.sleep doesn't release LOCK
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

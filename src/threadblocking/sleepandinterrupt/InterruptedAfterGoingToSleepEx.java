package threadblocking.sleepandinterrupt;

public class InterruptedAfterGoingToSleepEx {

    private volatile static int sCount = 0;      // shared resource/state

    public static void main(String[] args) {
        new Consumer().start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }
        Producer producer = new Producer();
        producer.start();

        try {
            Thread.sleep(500);          // while main sleeps, Producer already to sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producer.interrupt();   // producer interrupted immediately after sleep
    }

    static class Consumer extends Thread {
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
                if(isInterrupted()) {
                    System.out.println("Producer: interrupted flag set");
                    return;
                }
                try {
                    Thread.sleep(1000);     // Thread sleeps for 1s, no resources consumed
                } catch (InterruptedException e) {
                    System.out.println("Producer: interrupted during sleep");
                    return;
                }
            }
            System.out.println("Producer: terminating");
        }
    }
}

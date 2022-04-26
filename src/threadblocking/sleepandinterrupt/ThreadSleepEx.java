package threadblocking.sleepandinterrupt;

public class ThreadSleepEx {

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

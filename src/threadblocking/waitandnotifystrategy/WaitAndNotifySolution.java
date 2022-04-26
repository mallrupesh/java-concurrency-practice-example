package threadblocking.waitandnotifystrategy;

import java.util.concurrent.atomic.AtomicBoolean;

public class WaitAndNotifySolution {

    private volatile static int sCount = 0;

    private static final AtomicBoolean PRODUCER_FLAG = new AtomicBoolean(false);

    public static void main(String[] args) {

        new Consumer().start();     // main starts Consumer
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }

        new Producer().start();     // main starts Producer

        synchronized (PRODUCER_FLAG) {
            while(!PRODUCER_FLAG.get()) {   // False: wait before Producer calls notify otherwise notify signal lost
                try {                       // True: do not wait after Producer has already notified
                    PRODUCER_FLAG.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        System.out.println("Main returns");
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
                int localVal = sCount;
                localVal++;
                System.out.println("Producer: incrementing count to " + localVal);
                sCount = localVal;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Producer: terminating");

            synchronized (PRODUCER_FLAG) {
                PRODUCER_FLAG.set(true);    // Producer set FLAG true
                PRODUCER_FLAG.notifyAll();  // notify main
            }
        }
    }
}

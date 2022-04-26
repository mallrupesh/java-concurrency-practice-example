package threadblocking.waitandnotifystrategy;

public class WaitAndNotifyWithProblem {

    private volatile static int sCount = 0; // shared resource/state

    private static Object LOCK = new Object();

    public static void main(String[] args) {

        new Consumer().start();     // main starts Consumer.start()


        new Producer().start(); // if OS decides to start Producer first then Producer will
                                // notify before main waits, the notify signal will be missed


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            return;
        }


        // Producer finishes (100 * 5) and calls notify but the since no thread is waiting the signal is lost
        // main waits after 3000, signal already lost, main waits forever as no thread notifies again
        synchronized (LOCK) {
            try {
                LOCK.wait();
            } catch (InterruptedException e) {
                return;
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
                int localValue = sCount;
                localValue++;
                System.out.println("Producer: incrementing count to " + localValue);
                sCount = localValue;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Producer: terminating");

            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }
    }
}

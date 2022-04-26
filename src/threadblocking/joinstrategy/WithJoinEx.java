package threadblocking.joinstrategy;

public class WithJoinEx {

    private volatile static int sCount = 0; // shared resource/state

    public static void main(String[] args) {

        new Consumer().start();     // main starts Consumer.start()
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }

        Producer producer = new Producer();
        producer.start();           // main starts Producer.start()

        try {
            producer.join();        // join makes main thread wait until Producer terminates
        } catch (InterruptedException e) {
            return;
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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Producer: terminating");
        }
    }
}

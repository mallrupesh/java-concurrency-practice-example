package happensbefore;

public class HappensBeforeVolatile2 {

    private static class Counter {
        private volatile int sCount = 0;    // member var is volatile
    }

    private final static Counter counter = new Counter();

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
                if (localValue != counter.sCount) {
                    System.out.println("Consumer: detected count change " + counter.sCount);
                    localValue = counter.sCount;
                }
                if (counter.sCount >= 5) {
                    break;
                }
            }
            System.out.println("Consumer: terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while(counter.sCount < 5) {
                int localValue = counter.sCount;
                localValue++;
                System.out.println("Producer: incrementing count to " + localValue);
                counter.sCount = localValue;
            }
            System.out.println("Producer: terminating");
        }
    }
}


package happensbefore;

public class HappensBeforeVolatile {

    private volatile static int sCount = 0;      // this assignment happen-before Consumer.start()
                                        // hence, sCount value is visible to Consumer
    public static void main(String[] args) {
        new Consumer().start();         // Consumer.start() happens-before the Consumer's run()
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
                if (localValue != sCount) {     // read
                    System.out.println("Consumer: detected count change " + sCount);
                    localValue = sCount;        // read
                }
                if (sCount >= 5) {              // read
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
                int localValue = sCount;        // read
                localValue++;
                System.out.println("Producer: incrementing count to " + localValue);
                sCount = localValue;            // write to volatile var happen-before any subsequent read from the var
                try {                           // hence, any subsequent read from the var is guaranteed to observe
                    Thread.sleep(1000);   // this change
                } catch (InterruptedException e) {
                    return;
                }
            }
            System.out.println("Producer: terminating");
        }
    }
}

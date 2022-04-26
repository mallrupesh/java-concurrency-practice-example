package threadsafety.memorymodel;

import java.util.concurrent.atomic.AtomicInteger;

public class MemoryModelTester {

    static final MyObject sharedInstance = new MyObject();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Start counter value: " + sharedInstance.counter);

        while(true) {
            int res = sharedInstance.counter.get();
            if(res > 10) {          // checked in main thread
                break;
            }
            // Create new Threads with Shared resource
            new Thread(() -> {
                System.out.println("Thread: " + Thread.currentThread().getName() + " Value: " + res);
                sharedInstance.increment(); // atomic operation done in background threads
            }).start();
        }
        Thread.sleep(1000);
        System.out.println("Main ends");
    }

    // Instance of this class will be the Shared resource
    static class MyObject{

        // Volatile ensures visibility bypassing Cache by direct read and write from memory
        // Atomic ensures Atomicity fir increment() method
        volatile AtomicInteger counter = new AtomicInteger(0);

        public synchronized void increment(){
            counter.incrementAndGet();
        }
    }
}



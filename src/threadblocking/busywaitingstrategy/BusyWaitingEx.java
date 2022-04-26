package threadblocking.busywaitingstrategy;

import java.util.concurrent.atomic.AtomicInteger;

public class BusyWaitingEx {

    public static void main(String[] args) {

        // Create Initializer Thread
        new Thread(() -> {
            final AtomicInteger shared = new AtomicInteger(0);   // var initialized in outer thread

            // Outer thread starts inner thread
            new Thread(() -> {
                int res = shared.get() + 1;     // read, modify
                shared.set(res);                // write
            }).start();

            // Wait until the result is calculated
            while(true) {
                if(shared.get() == 0) {
                    System.out.println("waiting");
                    /*try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    System.out.println("Final result: " + shared.get());
                    break;
                }
            }

            System.out.println("Complete...");
        }).start();
    }
}

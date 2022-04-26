package threadblocking.waitandnotifystrategy;

public class WaitAndNotifyEx {

    private final static Object LOCK = new Object();

    public static void main(String[] args) {

        // Create Initializer Thread
        new Thread(() -> {
            final int[] shared = new int[10];   // var initialized in outer thread
            shared[0] = 0;

            // Outer thread starts inner thread
            new Thread(() -> {
                synchronized (LOCK) {
                    int res = shared[0];     // read, modify

                    for(int i = 0; i < 10000; i++) {
                        res++;
                    }
                    shared[0] = res;
                    System.out.println("Worker thread: " + res);
                    LOCK.notifyAll();   // notify waiting thread to awaken from wait()
                }
            }).start();

            synchronized (LOCK) {
                while(shared[0] < 10000) {   // once the shared[0] >= 10000, break out of the loop
                    try {
                        LOCK.wait();        // wait() until shared[0] >= 10000, release lock to other threads
                    } catch (InterruptedException e) {  // waiting for the LOCK
                        return;
                    }
                }
                System.out.println("Init thread: " + shared[0]);
            }

            System.out.println("Complete...");
        }).start();
    }
}

package intro.sleepsandjoins;

public class Runner2 {

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new HelloRunnable());
        thread1.start();


        long maxSleepTime = 2000L;                       // max waiting time for Thread1 is 5s
        long startTime = System.currentTimeMillis();    // get current time from System
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from Thread 2");

                while(thread1.isAlive()) {
                    try{
                        System.out.println("Thread 2 waiting for Thread 1 to wake up");
                        thread1.join(1000L);                                     // thread2 allows thread1 to join so thread2 becomes inactive
                        if((System.currentTimeMillis() - startTime) > maxSleepTime) {  // wake up thread1 if it sleeps
                            thread1.interrupt();                                       // for more than 5s
                        }
                    }catch (InterruptedException e) {
                        System.out.println("Thread 2 got interrupted");
                    }
                }
                System.out.println("Thread 1 finished its execution. Now, Thread 2 can finish off");
            }
        });

        thread2.start();
    }
}

class HelloRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello from Thread 1");
        try{
            Thread.sleep(3000);             // this thread sleeps for 8s during which other threads can use
        }catch (InterruptedException e) {         // the processor time
            System.out.println("Thread 1 got interrupted ");
        }
        System.out.println("I'm awake");  // after waking up from sleep prints this message
    }                                     // to finish off execution
}

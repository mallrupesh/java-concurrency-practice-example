package intro.basics;

public class Runner {
    /*
           -> main()
              Main thread
                  | -> MyAnotherRunnable
                  | -> new Thread
                  | -> new Thread start
                  |\
      Main T      |  \  -> new Thread
                  |    \
                  |      \
               sleep(1)  void run() while loop(true) -> println("print something")
               sleep(2)  sleep(1)
               sleep(3)  sleep(2)
               sleep(4)  sleep(3)
               sleep(5)  wake up -> while loop (true) -> println("print something")
               wake up
               do stop   while loop(false) -> GC handles clearing up
            -> function scope exit


     */

    public static void main(String[] args) {

        //System.out.println("Main thread name: " + Thread.currentThread().getName());    // main thread

        /*MyThread thread1 = new MyThread();               // MyThread, 1st worker thread
        thread1.start();

        Thread thread2 = new Thread(new MyRunnable());   //  MyRunnable 2nd worker thread
        thread2.start();*/


        /*for(int i = 0; i < 10; i++){
            new Thread(){
                @Override
                public void run() {
                    System.out.println("Thread: " + getName());
                }
            }.start();
        }*/

        // main thread
        MyAnotherRunnable myAnotherRunnable = new MyAnotherRunnable();      // MyAnotherRunnable init
        Thread thread3 = new Thread(myAnotherRunnable);                     // new Thread init
        thread3.start();                                                    // new Thread start()

        // main thread sleeps
        try{
            Thread.sleep(5L * 1000L);      // main thread sleep for 5s
        }catch (InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " got interrupted!");
        }

        myAnotherRunnable.doStop();
    }
}

// Thread subclass
class MyThread extends Thread{

    @Override
    public void run() {
        System.out.println("MyThread name: " + this.getName());
    }
}

// Class implementing Runnable interface
class MyRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("MyRunnable name: " + Thread.currentThread().getName());
    }
}


class MyAnotherRunnable implements Runnable{

    private boolean doStop = false;

    public synchronized void doStop(){              // doStop() is called from another thread
        this.doStop = true;
    }

    public synchronized  boolean keepRunning(){
        return !this.doStop;                        // return true if this.doStop == false
    }

    @Override
    public void run() {

        while(keepRunning()){                       // keepRunning() called internally
            System.out.println("Running...");

            try{
                Thread.sleep(3L * 1000L);     // sleep for 3s after each print
            }catch (InterruptedException e){
                System.out.println(Thread.currentThread().getName() + " got interrupted!");
            }
        }
    }
}
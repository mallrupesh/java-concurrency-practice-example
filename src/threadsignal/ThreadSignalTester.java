package threadsignal;

import java.util.Random;

/*

        Main thread
        |  -> inisilise Parcel
        |
        | ---------------|---------------------------|
        |                |                           |
        |                |                           |
        |                |                           |
        |                |                           |
        |            new thread(parcel)           new thread(parcel)
        |                |                           |
        |           array inisilised                 |
        |           constructor                    constructor
        |           for loop                      for loop

        --------------------------------------------- x4 --------------------------------------------------------
        |           parcel.produce(1st item)      parcel.consume(1st item)
        |           is consumed = true            isConsumed = true
        |           sync produce ->               sync consume ->
        |                          |                             |
        |               isConsumed = false                       |
        |                                                       wait
        |               this.message = mares eats oats          wait
        |                                                       wait
        |                                                       wait
        |               notify                                  received notification from notify
        |                                                       isConsumed = true
        |                                                       notify
        |                                                       message == mares eat oats
        |                                                       println(Message Received mares eats oats)
        |                                                       message != Done
        |                                         sync consume ->
        |           sleep(1)                                    isConsumed = false
        |           sleep(2)                                    isConsumed = true
        |           sleep(3)                                    message == mares eats oats
        |                                                        message != Done
        ---------------------------------------------------------------------------------------------------------
        |           sync produce ->               sync consume ->
        |           message = done                              isConsumed = true
        |           notify                                      wait ( might have to wait or not)
        |                                                       received notification from notify
        |                                                       message = Done
        |                                                       println(message received Done)
        |                                                       sleep(1)
        |                                                       sleep(2)

 */
public class ThreadSignalTester {

    public static void main(String[] args) {
        Parcel parcel = new Parcel();


        new Thread(new Consumer(parcel)).start();
        new Thread(new Producer(parcel)).start();
    }
}


class Producer implements Runnable {

    private Parcel parcel;          // local reference to Parcel object

    public Producer(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public void run() {
        String [] messages = {
                "Mares eat oat",
                "Does eat oat",
                "Little lambs eat ivy",
                "Kids eat ivy too"
        };

        for(String s: messages) {
            parcel.produce(s);
            try{
                // Producer sleeps, consumer takes over to consume the message
                Thread.sleep(3000);
            }catch (InterruptedException e){e.printStackTrace();}
        }

        parcel.produce("DONE");
    }
}


class Consumer implements Runnable {

    private Parcel parcel;                  // local reference to Parcel object

    public Consumer(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (String message = parcel.consume(); !message.equals("DONE"); message = parcel.consume()) {
            System.out.println("MESSAGE RECEIVED: " + message);
            try{
                // Consumer sleeps, producer takes over to update the message
                Thread.sleep(random.nextInt(2000));
            }catch (InterruptedException e){e.printStackTrace();}
        }
    }
}

class Parcel {

    private String message;                  // message to be produced and consumed

    private boolean isConsumed = true;       // producer needs to produce first

    public void produce(String message) {

        synchronized (this){
            while(!isConsumed) {            // !isConsumed means Consumer needs to consume message
                try{
                    this.wait();            // therefore, release lock and wait until consumer notifies
                }catch (InterruptedException e) {e.printStackTrace();}
            }

            isConsumed = false;             // message has been produced

            this.message = message;

            notify();                       // notify that the Parcel has been updated
        }
    }


    // Consumer calls take()
    // If the Consumer thread is fast and has consumed the message before Producer updates the message
    // Then it enter the loop again
    public String consume() {
        // wait() causes Consumer thread to become inactive and release the lock
        // so that Producer can update the Parcel object again
        // This is efficient since Consumer does not loop continuously until the condition is satisfied
        synchronized (this){
            while(isConsumed) {             // isConsumed means Producer needs Producer needs to produce message
                try{
                    this.wait();            // therefore, releases lock and wait until Producer notifies
                }catch (InterruptedException e){e.printStackTrace();}
            }

            isConsumed = true;              // message has been already consumed

            // Notify Producer that the Parcel has been consumed
            // It wakes up the Producer thread, so that Producer can update Parcel again
            notify();                // releases the lock

            return message;
        }
    }
}


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
        new Thread(new Consumer(parcel)).start();   // shared obj parcel
        new Thread(new Producer(parcel)).start();   // shared obj parcel
    }
}


class Producer implements Runnable {

    private Parcel parcel;          // local ref to Parcel obj

    public Producer(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public void run() {
        String [] messages = {
                "AAA",
                "BBB",
                "CCC",
                "DDD"
        };

        for(String s: messages) {
            parcel.produce(s);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){e.printStackTrace();}
        }
        parcel.produce("DONE");
    }
}


class Consumer implements Runnable {

    private Parcel parcel;          // local ref to Parcel obj

    public Consumer(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (String message = parcel.consume(); !message.equals("DONE"); message = parcel.consume()) {
            System.out.println("MESSAGE RECEIVED: " + message);
            try{
                Thread.sleep(random.nextInt(100));
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
            notify();                       // release the lock, notify the message is updated
        }
    }

    public String consume() {
        synchronized (this){
            while(isConsumed) {     // isConsumed true i.e. consumer has already consumed so wait
                try{
                    this.wait();    // release the lock and wait for notify
                }catch (InterruptedException e){e.printStackTrace();}
            }
            isConsumed = true;      // message is consumed
            notify();                // releases the lock, notify the message is consumed
            return message;
        }
    }
}


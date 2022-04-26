package locktypes.reentrantlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantTester {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        // Create lock i.e. shared among the threads
        ReentrantLock lock = new ReentrantLock();

        TheProducer producer = new TheProducer(list, lock);
        TheConsumer consumer1 = new TheConsumer(list, lock);
        TheConsumer consumer2 = new TheConsumer(list, lock);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(consumer1);
        Thread t3 = new Thread(consumer2);

        t1.setName("Producer");
        t2.setName("Consumer 1");
        t3.setName("Consumer 2");

        t1.start();
        t2.start();
        t3.start();
    }
}

class TheProducer implements Runnable {

    private List<String> buffer;

    private ReentrantLock lock;       // local copy of reentrant lock

    public TheProducer(List<String> buffer_, ReentrantLock lock_) {
        this.buffer = buffer_;
        this.lock = lock_;
    }

    @Override
    public void run() {
        String [] numbers = {"1", "2", "3", "4", "5", "6"};

        for(String s: numbers) {
            try {
                System.out.println(Thread.currentThread().getName() + ": Adding: " + s);
                lock.lock();          // lock() acquires the lock
                try {
                    buffer.add(s);
                } finally {
                    lock.unlock();    // unlock() releases the lock
                }
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println("Producer was interrupted");
            }
        }
        System.out.println(Thread.currentThread().getName() + ": Exiting...");
        lock.lock();                  // lock() acquires the lock
        try{
            buffer.add("DONE");
        } finally {
            lock.unlock();            // "finally" guarantees that the lock is released
        }
    }
}

class TheConsumer implements Runnable {

    private List<String> buffer;
    private ReentrantLock lock;       // local copy of reentrant lock

    public TheConsumer(List<String> buffer_, ReentrantLock lock_) {
        this.buffer = buffer_;
        this.lock = lock_;
    }

    @Override
    public void run() {
        //  int counter = 0;
        while(true){
            if(lock.tryLock()){           // check if the lock is available if yes acquire lock
                try {
                    if(buffer.isEmpty()){ // if buffer is empty start over from the beginning of the loop
                        continue;
                    }
                    //System.out.println("Counter: " + counter);
                    if(buffer.get(0).equals("DONE")) {       // if DONE exit
                        System.out.println(Thread.currentThread().getName() + ": Exiting...");
                        break;
                    }else{
                        System.out.println(Thread.currentThread().getName() + ": Removed: " + buffer.remove(0));
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            System.out.println(Thread.currentThread().getName() + ": Interrupted");
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}


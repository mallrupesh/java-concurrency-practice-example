package locktypes.synchronizevslock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VsTester {

    public static void main(String[] args) {

       // CounterSynced sharedObj = new CounterSynced();

        CounterLock sharedObj2 = new CounterLock();

        new Thread(new Run1(sharedObj2)).start();
        new Thread(new Run2(sharedObj2)).start();
    }
}


class Run1 implements Runnable{

    //CounterSynced counter;
    CounterLock counter;

    public Run1(CounterLock counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        int res = counter.increment();
        System.out.println(res);
    }
}

class Run2 implements Runnable{

    //CounterSynced counter;
    CounterLock counter;

    public Run2(CounterLock counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        int res = counter.increment();
        System.out.println(res);
    }
}



class CounterLock{
    private ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public int increment(){
        lock.lock();
        int newCount = ++count;
        lock.unlock();
        return newCount;
    }
}

class CounterSynced{

    int count = 0;

    public int increment(){
        synchronized (this){
            return ++count;
        }
    }
}

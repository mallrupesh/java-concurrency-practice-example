package locktypes.simplelock;

import java.util.ArrayList;
import java.util.List;

public class LockTester {

    public static void main(String[] args) {

        Lock lock = new Lock();
        List<String> list = new ArrayList<>();

        new Thread(new MyProducer(list, lock)).start();
        new Thread(new MyConsumer(list, lock)).start();
        new Thread(new MyConsumer(list, lock)).start();
    }
}


class MyProducer implements Runnable{

    private List<String> stringList;
    private Lock lock;

    public MyProducer(List<String> stringList, Lock lock) {
        this.stringList = stringList;
        this.lock = lock;
    }

    @Override
    public void run() {

        String[] myStrings = {"1", "2", "3", "4", "5"};


        for(String val : myStrings){
            try {
                lock.lock();
                try{
                    System.out.println(Thread.currentThread().getName() + " added: " + val);
                    stringList.add(val);
                }finally {
                    lock.unlock();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Producer interrupted");
            }
        }
        System.out.println(Thread.currentThread().getName() +  " exiting...");

        try {
            lock.lock();
            try{
                stringList.add("DONE");
            }finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +  " unable to finish the process");
        }
    }
}

class MyConsumer implements Runnable{

    private List<String> stringList;
    private Lock lock;

    public MyConsumer(List<String> stringList, Lock lock) {
        this.stringList = stringList;
        this.lock = lock;
    }

    @Override
    public void run() {

        while(true){

            try {
                lock.lock();

                if(stringList.isEmpty())
                    continue;

                if(stringList.get(0).equals("DONE")){
                    System.out.println(Thread.currentThread().getName() +  " existing...");
                    break;

                }else{
                    System.out.println(Thread.currentThread().getName() + " removed " + stringList.remove(0));

                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        System.out.println(Thread.currentThread().getName() + " interrupted");
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}


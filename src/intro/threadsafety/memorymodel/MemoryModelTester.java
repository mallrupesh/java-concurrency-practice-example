package intro.threadsafety.memorymodel;

public class MemoryModelTester {

    public static void main(String[] args) {

        MyObject sharedInstance = new MyObject();       // shared instance on Heap

        System.out.println("Value of counter: " + sharedInstance.counter);

        new Thread(new MyRunnable(sharedInstance)).start();
        new Thread(new MyRunnable(sharedInstance)).start();

    }
}

class MyRunnable implements Runnable{

    MyObject obj;                       // each instance of MyRunnable will have local copy of MyObject

    public MyRunnable(MyObject obj) {
        this.obj = obj;
    }

    @Override
    public void run() {
        int res = obj.increment();
        System.out.println("Value of counter: " + res);
    }
}

class MyObject{

    volatile int counter = 0;           // volatile makes sure that the changes made by one thread is
                                        // immediately flushed back to main memory so that the change is
                                        // immediately visible to other threads
    public int increment(){
        synchronized (this){
            return ++counter;
        }
    }
}
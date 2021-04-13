package deadlocks;

public class DeadLockTester {

    public static final Object LOCK1 = new Object();
    public static final Object LOCK2 = new Object();

    public static void main(String[] args) {

        new Thread(new RunnableA()).start();
        new Thread(new RunnableB()).start();

        new Thread(new Runnable1()).start();
        new Thread(new Runnable2()).start();
    }




    // Different LOCK order, DeadLock!!!
    public static class RunnableA implements Runnable{

        @Override
        public void run() {
            synchronized (LOCK1){
                System.out.println("Thread1 has LOCK1");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    System.out.println("Thread1 was interrupted");
                }
                System.out.println("Thread1 waiting for LOCK2" );

                synchronized (LOCK2){
                    System.out.println("Thread1 has LOCK2");
                }
                System.out.println("Thread1 released LOCK2");
            }
            System.out.println("Thread1 released LOCK1");
        }
    }

    public static class RunnableB implements Runnable{

        @Override
        public void run() {
            synchronized (LOCK2){
                System.out.println("Thread2 has LOCK2");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    System.out.println("Thread2 was interrupted");
                }
                System.out.println("Thread2 waiting for LOCK1" );

                synchronized (LOCK1){
                    System.out.println("Thread2 has LOCK1");
                }
                System.out.println("Thread2 released LOCK1");
            }
            System.out.println("Thread2 released LOCK2");
        }
    }



    // Same LOCK order, Non-blocking
    public static class Runnable1 implements Runnable{

        @Override
        public void run() {
            synchronized (LOCK1){
                System.out.println("Thread1 has LOCK1");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    System.out.println("Thread1 was interrupted");
                }
                System.out.println("Thread1 waiting for LOCK2" );

                synchronized (LOCK2){
                    System.out.println("Thread1 has LOCK2");
                }
                System.out.println("Thread1 released LOCK2");
            }
            System.out.println("Thread1 released LOCK1");
        }
    }

    public static class Runnable2 implements Runnable{

        @Override
        public void run() {
            synchronized (LOCK1){
                System.out.println("Thread2 has LOCK1");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    System.out.println("Thread2 was interrupted");
                }
                System.out.println("Thread2 waiting for LOCK2" );

                synchronized (LOCK2){
                    System.out.println("Thread2 has LOCK2");
                }
                System.out.println("Thread2 released LOCK2");
            }
            System.out.println("Thread2 released LOCK1");
        }
    }
}



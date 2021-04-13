package threadpool.javapool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceTester {

    public static void main(String[] args) {

        // With ArrayBlockingQueue we must provide the number of elements for the array
        // No need to track Object lock with this
        // Already synchronized collection
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(7);

        // ExecutorService instance
        ExecutorService executorService = Executors.newFixedThreadPool(3);   // Manage total of 3 threads

        // The Runnable classes
        ProducerOne producer = new ProducerOne(buffer);
        ConsumerOne consumer1 = new ConsumerOne(buffer);
        ConsumerOne consumer2 = new ConsumerOne(buffer);

        // This is equivalent to new Thread(new Runnable()).start
        executorService.execute(producer);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.shutdown();
    }
}

class ProducerOne implements Runnable {

    private ArrayBlockingQueue<String> buffer;

    public ProducerOne(ArrayBlockingQueue<String> buffer_) {
        this.buffer = buffer_;
    }

    @Override
    public void run() {
        String [] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        for(String s: numbers) {
            try {
                System.out.println(Thread.currentThread().getName() + ": Adding: " + s);
                buffer.put(s);
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println("Producer was interrupted");
            }
        }
        System.out.println(Thread.currentThread().getName() + ": Adding EOF and exiting...");
        try {
            buffer.put("EOF");
        } catch (InterruptedException e) {
            System.out.println("Producer was interrupted");
        }
    }
}

class ConsumerOne implements Runnable {

    private ArrayBlockingQueue<String> buffer;

    public ConsumerOne(ArrayBlockingQueue<String> buffer_) {
        this.buffer = buffer_;
    }
    @Override
    public void run() {
        //int counter = 0;
        while(true){
            if(buffer.isEmpty()){
                continue;
            }
            if(buffer.peek().equals("EOF")) {
                System.out.println(Thread.currentThread().getName() + ": Exiting...");
                break;
            }else{
                try {
                    System.out.println(Thread.currentThread().getName() + ": Removed: " + buffer.take());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + ": Interrupted");
                }
            }
        }
    }
}

/*
 * ------------------------------EXECUTOR SERVICE INTERFACE--------------------------
 *
 * - We use implementation of this interface to manage threads for us. This means we do not
 *   have to explicitly create and start threads
 *
 * - The implementation provided jdk manage things like thread scheduling and thread creation
 *   optimization but can be generally expensive in terms of performance and memory
 *
 * - The Executor Service allows us to focus on code we want to run without the fuss of managing
 *   threads and their lifecycles
 *
 * - It uses Thread Polls. Thread Polls is a managed set of threads, it reduces the overhead of
 *   thread creation specially in application that use large number of threads
 *
 * - Thread may also limit the number of threads that are active
 *
 */


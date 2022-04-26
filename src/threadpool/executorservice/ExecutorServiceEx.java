package threadpool.executorservice;

import javafx.concurrent.ScheduledService;

import java.util.concurrent.*;

public class ExecutorServiceEx {

    public static void main(String[] args) throws InterruptedException {

        //fixedThreadPoolEx();
        //cachedThreadPoolEx();
        //scheduledThreadPoolEx();
        //scheduledThreadPoolWithRepeatIntervalEx();
        //singleThreadPoolEx();
        futureEx();


        Thread.sleep(5000);
    }


    public static void fixedThreadPoolEx() {
        // Create thread pool of fixed no. of threads (3)
        ExecutorService service = Executors.newFixedThreadPool(3);

        // Submit task => execute Runnable
        service.execute(() -> System.out.println("Running on: " + Thread.currentThread().getName()));

        // Shutdown service
        service.shutdown();
    }


    public static void cachedThreadPoolEx() {
        // Create service
        ExecutorService service = Executors.newCachedThreadPool();

        // Submit task
        service.execute(() -> System.out.println("Running on: " + Thread.currentThread().getName()));

        // Shutdown service
        service.shutdown();
    }

    public static void scheduledThreadPoolEx() {
        // Create service
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        // Submit the task to run only once after 2 seconds of delay
        service.schedule(
                () -> System.out.println("Running on: " + Thread.currentThread().getName()),
                4000,
                TimeUnit.MILLISECONDS
        );

        // Shutdown service
        service.shutdown();
    }

    public static void scheduledThreadPoolWithRepeatIntervalEx() {

        long startTime = System.currentTimeMillis();

        // Create service
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        // Submit the task to run after 4 seconds of initial delay repeatedly every 2 seconds
        service.scheduleAtFixedRate(
                () -> System.out.println("Running on: " + Thread.currentThread().getName()),
                2000,
                2000,
                TimeUnit.MILLISECONDS
        );
    }

    public static void singleThreadPoolEx() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> System.out.println("Running on: " + Thread.currentThread().getName()));
        service.shutdown();
    }

    public static void futureEx() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<Integer> future = service.submit(() -> 8 * 10);

        try {
            System.out.println("Result: " + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }
}

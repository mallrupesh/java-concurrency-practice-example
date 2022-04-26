package threadblocking;

public class Ex3UsingJoin {

    public static void main(String[] args) {

        try {
            startAllConcurrently();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main exits...");
    }

    public static void startAllConcurrently() throws InterruptedException {
        doSomeWorkerTaskConcurrently();

        Thread task3 = new Thread(() -> {
            System.out.println("Starting task 3");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Task 3 completed");
        });
        task3.start();
        task3.join();       // wait for task3 to complete
    }


    /**
     * Start task1 and task2 concurrently and use join() to wait for their completion
     * @throws InterruptedException
     */
    public static void doSomeWorkerTaskConcurrently() throws InterruptedException {
        Thread task1 = new Thread(() -> {
            System.out.println("Starting task 1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Task 1 completed");
        });
        task1.start();


        Thread task2 = new Thread(() -> {
            System.out.println("Starting task 2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println("Task 2 completed");
        });
        task2.start();

        task1.join();       // wait for task1 to complete
        task2.join();       // wait for task2 to complete
    }
}






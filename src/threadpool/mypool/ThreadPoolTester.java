package threadpool.mypool;

public class ThreadPoolTester {

    public static void main(String[] args) {

        MyThreadPool threadPool = new MyThreadPool(3, 10);

        for(int i = 0; i < 10; i++){

            int taskNo = i;
            threadPool.execute(() -> {
                String message = Thread.currentThread().getName() + ": Task " + taskNo;
                System.out.println(message);
            });
        }

        threadPool.waitUntilAllTasksFinished();
        threadPool.stop();
    }
}

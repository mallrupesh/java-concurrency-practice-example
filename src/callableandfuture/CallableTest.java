package callableandfuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableTest {

    public static void main(String[] args) {

        // Callable object/lambda function that returns value
        Callable<Integer> compute = () -> 123 * 123;

        // FutureTask that takes Callable object
        // FutureTask is just like Runnable
        FutureTask<Integer> task = new FutureTask<>(compute);

        new Thread(task).start();   // start the new thread

        try {
            Integer res = task.get();
            System.out.println(res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}

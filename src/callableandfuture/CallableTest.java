package callableandfuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableTest {

    public static void main(String[] args) {

        // Callable object/lambda function that returns value
        Callable<Integer> compute = () -> 2 * 5;

        // FutureTask that takes Callable object
        // FutureTask is just like Runnable
        FutureTask<Integer> task = new FutureTask<>(compute);

        new Thread(task).start();   // start the new thread

        Integer result = null;

        try {
            result = task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(result);

    }
}

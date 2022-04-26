package callableandfuture;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) {


        try(Scanner in = new Scanner(System.in)) {

            System.out.print("Enter base directory (e.g. /user/local/jdk8.0/src): ");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            // Create callable object
            MatchCounter counter = new MatchCounter(new File(directory), keyword);

            // Create a Future task and pass the Callable
            FutureTask<Integer> task =  new FutureTask<>(counter);
            new Thread(task).start();   // start the thread

            try{
                System.out.println(task.get() + " matching files");
            }catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

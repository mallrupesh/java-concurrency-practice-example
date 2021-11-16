package threadpool.threadpool;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;

public class ThreadPoolTest {


    public static void main(String[] args) {

        try(Scanner in = new Scanner(System.in)) {

            System.out.print("Enter base directory (e.g. /user/local/jdk8.0/src): ");
            String directory = in.nextLine();
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            ExecutorService pool = Executors.newCachedThreadPool();
            WordCounter counter = new WordCounter(new File(directory),keyword, pool);
            Future<Integer> result = pool.submit(counter);

            try{
                System.out.println(result.get() + " matching files");
            }catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }

            pool.shutdown();    // shutdown pool

            int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
            System.out.println("Largest pool size: " + largestPoolSize);
        }
    }
}

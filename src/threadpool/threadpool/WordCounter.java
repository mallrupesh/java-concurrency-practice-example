package threadpool.threadpool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class WordCounter implements Callable<Integer> {

    private File directory;
    private String keyword;
    private ExecutorService pool;
    private int count;

    public WordCounter(File directory, String keyword, ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = pool;
    }

    @Override
    public Integer call() throws Exception {
        count = 0;

        try{
            File[] files = directory.listFiles();
            List<Future<Integer>> results = new ArrayList<>();

            for(File file : files) {
                if(file.isDirectory()) {
                    WordCounter counter = new WordCounter(file, keyword, pool);
                    Future<Integer> result = pool.submit(counter);
                    results.add(result);
                }else {
                    if (searchFile(directory))
                        count++;
                }
            }

            for(Future<Integer> result : results) {
                count += result.get();
            }
        }catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return count;
    }

    public boolean searchFile(File file) {

        try{
            try(Scanner in = new Scanner(file, "UTF-8")) {

                boolean found = false;
                while(!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if(line.contains(keyword)) {
                        found = true;
                    }
                }
                return found;
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}

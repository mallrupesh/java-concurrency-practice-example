package callableandfuture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class MatchCounter implements Callable<Integer> {

    private File directory;
    private String keyword;

    public MatchCounter(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }

    @Override
    public Integer call() throws Exception {
        int count = 0;

        try {
            File[] files = directory.listFiles();
            List<Future<Integer>> results = new ArrayList<>();

            for(File file : files) {
                if(file.isDirectory()) {
                    MatchCounter counter = new MatchCounter(file, keyword);
                    FutureTask<Integer> task = new FutureTask<>(counter);
                    results.add(task);
                    Thread t = new Thread(task);
                    t.start();
                }else {
                    if(searchFile(file))
                        count++;
                }
            }

            for(Future<Integer> result : results) {
                try {
                    count += result.get();
                }catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return count;
    }

    public boolean searchFile(File file) {
        try {
            try(Scanner in = new Scanner(file, "UTF-8")) {
                boolean found = false;
                while(!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if(line.contains(keyword))
                        found = true;
                }
                return found;
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

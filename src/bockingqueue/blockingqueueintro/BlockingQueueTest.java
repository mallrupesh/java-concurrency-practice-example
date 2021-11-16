package bockingqueue.blockingqueueintro;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

    private static final int FILE_QUEUE_SIZE = 10;              // blocking queue size
    private static final int SEARCH_THREADS = 100;              // number of search thread
    private static final File DUMMY = new File("");    // dummy file to signal search completion
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);


    public static void main(String[] args) {

        try(Scanner in = new Scanner(System.in)) {              // try with resources
            // Get directory to start search from
            System.out.print("Enter base directory (e.g. /opt/jdk1.8.3/src): ");
            String directory = in.nextLine();

            // Get keyword to search for
            System.out.print("Enter keyword to search (e.g. volatile): ");
            String keyword = in.nextLine();

            // Create Enumerator thread that adds files from directory into our queue
            Runnable enumerator = () -> {
                try {
                    enumerate(new File(directory));
                    queue.put(DUMMY);   // put DUMMY file at the end which will be used to signal search completion
                                        // eventually, the last file will be DUMMY file after the enumeration completely finishes
                }catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            };
            new Thread(enumerator).start(); // start thread


            // Create Search threads that retrieve files from our queue and searches for the keyword in the files
            for(int i = 0; i <= SEARCH_THREADS; i++) {

                Runnable searcher = () -> {
                    try {
                        boolean done = false;

                        while(!done) {
                            File file = queue.take();  // take file from the queue, remember take() blocks
                                                       // if the queue is empty
                            if(file == DUMMY) {
                                queue.put(file); // put back the dummy file that will eventually make all Runnable threads exit normally
                                done = true;
                            }else {
                                search(file, keyword);
                            }
                        }
                    }catch (InterruptedException | FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                };
                new Thread(searcher).start();   // start threads
            }
        }
    }

    /**
     * Recursively enumerate all files in a given directory and its sub-directories
     * the add into the queue
     * @param directory the directory in which to start
     */
    public static void enumerate(File directory) throws InterruptedException {

        File[] files = directory.listFiles();

        for(File file : files) {
            if(file.isDirectory())
                enumerate(file);    // if directory, enumerate
            else
                queue.put(file);    // if file, put into the queue, remember put() blocks if the queue is full
        }
    }

    /**
     * Searches file for a given keyword and prints all matching lines
     * First it retrieves file then searches for the keyword
     * @param file the file to search
     * @param keyword the keyword to search for
     */
    public static void search(File file, String keyword) throws FileNotFoundException {

        try (Scanner in = new Scanner(file, "UTF-8")) {

            int lineNumber = 0;

            while(in.hasNextLine()) {
                lineNumber++;
                String line = in.nextLine();

                if(line.contains(keyword)) {
                    System.out.printf("%s:%d:%s%n", file.getPath(), lineNumber, line);
                }
            }
        }
    }
}

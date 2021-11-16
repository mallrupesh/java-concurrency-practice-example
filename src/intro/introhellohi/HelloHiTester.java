package intro.introhellohi;

import java.util.Set;
import java.util.function.Function;

public class HelloHiTester {

    public static void main(String[] args) {

        // Create a Runnable object/ lambda function
        Runnable runnable  = () -> {

            int counter = 0;
            while(true) {
                counter++;
                System.out.println(Thread.currentThread().getName() + "->" +  counter + ". Hi there");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();       // start the thread, that executes Runnable run() method

       /* Set<Thread> threads = Thread.getAllStackTraces().keySet();

        for(Thread thread: threads) {
            System.out.println(thread.getName());
        }*/

        int counter = 0;
        while(true) {
            counter++;
            System.out.println(Thread.currentThread().getName() + "->" +  counter + ". Hello");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
}

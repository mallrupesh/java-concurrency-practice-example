package threadlocal;

import java.util.Date;

public class ThreadLocalRunner {

    public static void main(String[] args) {

        Runnable r1 = () -> {
            String dateStamp = MyEvent.dateFormat.get().format(new Date());
            System.out.println(dateStamp);
        };

        Runnable r2 = () -> {
            String dateStamp = MyEvent.dateFormat.get().format(new Date());
            System.out.println(dateStamp);
        };

        Thread t1 = new Thread(r1);
        t1.start();

        Thread t2 = new Thread(r2);
        t2.start();
    }
}

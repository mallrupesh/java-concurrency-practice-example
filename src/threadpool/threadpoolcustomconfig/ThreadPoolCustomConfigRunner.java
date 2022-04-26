package threadpool.threadpoolcustomconfig;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolCustomConfigRunner {

    public static void main(String[] args) {

        CustomThreadPool1 threadPool1 = new CustomThreadPool1();

        ThreadPoolExecutor executor = threadPool1.getThreadPool();

        executor.execute(() -> {
            for(int i = 0; i < 1000; i++) {
                executor.execute(() -> {
                    System.out.println("Hello");
                });
            }
        });

    }
}

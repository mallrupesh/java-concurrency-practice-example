package threadpool.threadpoolcustomconfig;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool2 {

    private ThreadPoolExecutor mThreadPoolExecutor;

    public ThreadPoolExecutor getThreadPool() {
        if (mThreadPoolExecutor == null) {
            mThreadPoolExecutor = new ThreadPoolExecutor(
                    3,               // core pool size
                    Integer.MAX_VALUE,          // maximum no of thread that will be available in this ThreadPool
                    60,             // defines the time that each specific Thread will be kept alive
                    TimeUnit.SECONDS,           // in ThreadPool after it is no longer in use
                    new SynchronousQueue<>(),   // work Queue where Runnables are kept
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            System.out.println(
                                    "size: " + mThreadPoolExecutor.getPoolSize() +
                                            "\nactive count: " + mThreadPoolExecutor.getActiveCount() +
                                            "\nqueue remaining: " + mThreadPoolExecutor.getQueue().remainingCapacity()
                            );
                            return new Thread(r);
                        }
                    }
            );
        }
        return mThreadPoolExecutor;
    }
}

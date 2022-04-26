package threadpool.threadpoolcustomconfig;

import java.util.concurrent.*;

public class CustomThreadPool1 {

    private ThreadPoolExecutor mThreadPoolExecutor;     // ThreadPoolExecutor implements ThreadPoolExecutorService

    /**
     *  - ThreadPoolExecutor will create new Threads for incoming Runnables as long as
     *    the ThreadPool size is less than Core pool size
     *
     *  - If it reaches Core pool size, then the next Runnables that arrive for the
     *    execution will not cause new Threads to be created but will go into
     *    Blocking Queue
     *
     *  - Only after the Work Queue is full and all Core Threads are busy then the
     *    ThreadPool can create Threads upto Max ThreadPool size specified
     *
     *  - This means that ThreadPool with MaxSize 10 and Work Queue size 20 can accommodate 30 Runnables
     *    simultaneously
     *
     * @return the custom config ThreadPollExecutor instance
     */
    public ThreadPoolExecutor getThreadPool() {
        if (mThreadPoolExecutor == null) {
            mThreadPoolExecutor = new ThreadPoolExecutor(
                    3,               // core pool size
                    Integer.MAX_VALUE,          // maximum no of thread that will be available in this ThreadPool
                    60,             // defines the time that each specific Thread will be kept alive
                    TimeUnit.SECONDS,           // in ThreadPool after it is no longer in use
                    new ArrayBlockingQueue<>(10),   // work Queue where Runnable are kept
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            System.out.println(
                                "size: " + mThreadPoolExecutor.getPoolSize() +
                                " active count: " + mThreadPoolExecutor.getActiveCount() +
                                " queue remaining: " + mThreadPoolExecutor.getQueue().remainingCapacity()
                            );
                            return new Thread(r);
                        }
                    }
            );
        }
        return mThreadPoolExecutor;
    }
}

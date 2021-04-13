package threadpool.mypool;

import java.util.concurrent.BlockingQueue;

public class PoolThreadRunnable implements Runnable {

    private Thread thread = null;
    private BlockingQueue taskQueue = null;
    private boolean isStopped = false;

    public PoolThreadRunnable(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        this.thread = Thread.currentThread();

        while(!isStopped){
            try{
                Runnable runnable = (Runnable) taskQueue.take();
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void doStop(){
        isStopped = true;
        this.thread.interrupt();
    }

    public synchronized boolean isStopped(){
        return isStopped;
    }
}

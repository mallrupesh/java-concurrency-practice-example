package threadpool.custompool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyThreadPool {

    private BlockingQueue taskQueue = null;
    private List<PoolThreadRunnable> runnableList = new ArrayList<>();
    private boolean isStopped = false;


    public MyThreadPool(int noOfThreads, int maxNoOfTasks){
        taskQueue = new ArrayBlockingQueue(maxNoOfTasks);

        for(int i = 0; i < noOfThreads; i++){
            PoolThreadRunnable poolThreadRunnable = new PoolThreadRunnable(taskQueue);

            runnableList.add(poolThreadRunnable);
        }

        for(PoolThreadRunnable runnable: runnableList){
            new Thread(runnable).start();
        }
    }

    public synchronized void execute(Runnable task){
        if(this.isStopped) throw new IllegalStateException("ThreadPool is stopped");

        this.taskQueue.offer(task);
    }

    public synchronized void stop(){
        this.isStopped = true;
        for(PoolThreadRunnable runnable: runnableList){
            runnable.doStop();
        }
    }

    public synchronized void waitUntilAllTasksFinished(){
        while(this.taskQueue.size() > 0){
            try{
                Thread.sleep(1);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

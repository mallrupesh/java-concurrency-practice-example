package locktypes.simplelock;

public class Lock {

    private boolean isLocked = false;
    private Thread lockingThread = null;

    public synchronized void lock() throws InterruptedException {
        while(isLocked){    // while the lock is still with another thread (this means while otherThread.unlock() is not called)
            wait();         // wait for the notification
        }

        isLocked = true;
        lockingThread = Thread.currentThread();
    }

    public synchronized void unlock(){
        if(this.lockingThread != Thread.currentThread()){
            throw new IllegalMonitorStateException("Calling thread has not locked this lock");
        }

        isLocked = false;
        lockingThread = null;       // make no thread acquires the lock
        notify();                   // release the lock
    }
}

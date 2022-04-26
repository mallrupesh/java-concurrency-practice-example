package locktypes.simplelock;

public class Lock {

    private boolean isLocked = false;
    private Thread lockingThread = null;

    public synchronized void lock() throws InterruptedException {
        while(isLocked){    // while the locked (another thread holds the lock now)
            wait();         // wait for the notification
        }
        isLocked = true;    // if !isLocked, get the lock
        lockingThread = Thread.currentThread();
    }

    public synchronized void unlock(){
        if(this.lockingThread != Thread.currentThread()){
            throw new IllegalMonitorStateException("This thread is does not own the lock");
        }
        isLocked = false;
        lockingThread = null;   // make no thread acquires the lock
        notify();               // release the lock
    }
}

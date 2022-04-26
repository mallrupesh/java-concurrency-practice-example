package bockingqueue.customblockingqueue;

import java.util.LinkedList;
import java.util.List;

public class MyBlockingQueue<T> {

    private final List<T> queue = new LinkedList<>();
    private final int capacity;

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;           // init with capacity
    }

    // This method is executed by multiple Producer Threads generated in the main
    public synchronized void put(T item) throws InterruptedException {
        while(queue.size() >= capacity){
            wait();                        // wait if the Queue size >= capacity
        }

        queue.add(item);                   // otherwise, insert element
        notifyAll();                       // notify other Threads
    }

    // This method is executed by multiple Consumer Threads generated in the main
    public synchronized T take() throws InterruptedException {
        while(queue.size() <= 0){          // wait if the Queue size == 0
            wait();
        }

        notifyAll();                       // notify other Threads before return
        return queue.remove(0);      // remove from head
    }
}

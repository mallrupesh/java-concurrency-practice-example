package bockingqueue;

import java.util.LinkedList;
import java.util.List;

public class MyBlockingQueue<T> {

    private List<T> queue = new LinkedList<>();
    private int capacity = 10;

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;               // init with capacity
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        while(this.queue.size() == this.capacity){
            wait();                             // if capacity is full, wait(), become inactive, do nothing
        }

        this.queue.add(item);                   // otherwise add task/item

        if(this.queue.size() == 1){             // in dequeue() while queue.size = 0, other threads wait
            notifyAll();                        // now this thread has just added an item so notifyAll threads
        }
    }

    public synchronized T dequeue() throws InterruptedException {
        while(this.queue.size() == 0){
            wait();                             // if size is 0, wait(), become inactive, do nothing
        }

        if(this.queue.size() == this.capacity){
            notifyAll();                        // in enqueue(), while queue.size = full capacity, other threads wait
        }                                       // now this thread needs to notifyAll waiting threads

        return this.queue.remove(0);      // remove from head
    }
}

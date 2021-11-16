package bockingqueue.customblockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class BlockingQueueTester {

    public static void main(String[] args) {

        final int BOUND = 10;                                                   // Queue capacity 10
        final int nPRODUCERS = 4;                                               // 4 producers
        final int nCONSUMERS = Runtime.getRuntime().availableProcessors();      // N consumers
        int terminatorInt = Integer.MAX_VALUE;                                  // Producer will never send MAX under normal working condition
        int terminatorPerProducer = nCONSUMERS/nPRODUCERS;
        int mod = nCONSUMERS % nPRODUCERS;

        // BlockingQueue is the shared resource which is used to coordinate work between Producer and Consumer
        // 4 Producers will be putting random Integers in a BlockingQueue and Consumers will be taking those
        // elements from that BlockingQueue
        MyBlockingQueue<Integer> queue = new MyBlockingQueue<Integer>(BOUND);

        int i, j;
        for(i = 1; i < nPRODUCERS; i++){
            new Thread(new NumberProducer(queue, terminatorInt, terminatorPerProducer)).start();
        }

        for(j = 0; j < nCONSUMERS; j++){
            new Thread(new NumberConsumer(queue, terminatorInt)).start();
        }

        // This is done to stop consumer indefinitely waiting for the number from the queue
        new Thread(new NumberProducer(queue, terminatorInt, terminatorPerProducer + mod)).start();
    }
}

class NumberProducer implements Runnable{

    private MyBlockingQueue<Integer> numbersQueue;        // local ref to BlockingQueue which will sharedObject
    private final int terminatorInt;
    private final int terminatorPerProducer;

    public NumberProducer(MyBlockingQueue<Integer> numbersQueue, int terminatorInt, int terminatorPerProducer) {
        this.numbersQueue = numbersQueue;
        this.terminatorInt = terminatorInt;
        this.terminatorPerProducer = terminatorPerProducer;
    }

    @Override
    public void run() {
        try{
            generateNumbers();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public void generateNumbers() throws InterruptedException {
        int i, j;
        for(i = 0; i < 4; i++){
            numbersQueue.enqueue(ThreadLocalRandom.current().nextInt(10));
        }

        for(j = 0; j < terminatorPerProducer; j++){
            numbersQueue.enqueue(terminatorInt);
        }
    }
}

class NumberConsumer implements Runnable{

    private MyBlockingQueue<Integer> numbersQueue;        // local ref to BlockingQueue which will sharedObject
    private final int terminatorInt;

    public NumberConsumer(MyBlockingQueue<Integer> numbersQueue, int terminatorInt) {
        this.numbersQueue = numbersQueue;
        this.terminatorInt = terminatorInt;
    }

    @Override
    public void run() {
        try{
            while(true){                                // consumer waits for the number to take from the queue
                Integer number = numbersQueue.dequeue();
                if(number.equals(terminatorInt)){       // to stop consumer from indefinitely waiting for the number from queue
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " result: " + number);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}

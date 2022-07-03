package com.example.waitandnotify;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        Thread tProducer = new Thread(producer,"Producer");
        Thread tConsumer = new Thread(consumer,"Consumer");
        tProducer.start();
        tConsumer.start();
    }
}

class Producer implements Runnable{
    private Queue<Integer> queue;
    private static final Logger logger = Logger.getLogger(Producer.class.getName());
    public Producer(Queue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        for(int i=0; i<10; i++){
            synchronized (queue){
                while (queue.size() >= 1){
                    logger.info("queue is full.." + threadName + " thread is waiting.");
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                logger.info( threadName + " thread produced value = " + i);
                queue.add(i);
                queue.notifyAll();
            }
        }
    }
}

class Consumer implements Runnable{
    private Queue<Integer> queue;
    private static final Logger logger = Logger.getLogger(Consumer.class.getName());
    public Consumer(Queue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        while(true){
            synchronized (queue){
                while (queue.size() == 0){
                    logger.info("queue is empty..." + threadName + " thread is waiting.");
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                int i = queue.poll();
                logger.info( threadName + " thread consumed value = " + i);
                queue.notifyAll();

                //introduced break condition to stop thread
                if(i==9)
                    break;
            }
        }
    }
}

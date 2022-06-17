package com.examples.semaphores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class ProducerConsumerUsingSemaphore {

    public static void main(String[] args) {
        Semaphore prodSema = new Semaphore(1);
        Semaphore consSema = new Semaphore(0);
        Queue<Integer> queue = new LinkedList<>();
        Thread t1 = new Thread(new Producer(prodSema,consSema,queue),"producer");
        Thread t2 = new Thread(new Consumer(prodSema,consSema,queue),"consumer");
        t1.start();
        t2.start();
    }

}

class Producer implements Runnable{

    private Semaphore prodSema, consSema;
    private Queue<Integer> queue;

    public Producer(Semaphore prodSema, Semaphore consSema, Queue<Integer> queue){
        this.prodSema = prodSema;
        this.consSema = consSema;
        this.queue = queue;
    }

    @Override
    public void run() {
        for(int i=0; i<11; i++){
            while(queue.size()>=1){
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting ");
                    consSema.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("produced >>> " + i);
            queue.add(i);
            prodSema.release();
        }
    }
}

class Consumer implements Runnable{


    private Semaphore prodSema, consSema;
    private Queue<Integer> queue;

    public Consumer(Semaphore prodSema, Semaphore consSema, Queue<Integer> queue){
        this.prodSema = prodSema;
        this.consSema = consSema;
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            while(queue.size()==0){
                try {
                    System.out.println(Thread.currentThread().getName() + " waiting ");
                    prodSema.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            int data = queue.poll();
            System.out.println("consumed >>> " + data);
            consSema.release();
            if(data>10)
                break;
        }
    }
}

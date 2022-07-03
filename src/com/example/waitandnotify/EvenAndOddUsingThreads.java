package com.example.waitandnotify;

public class EvenAndOddUsingThreads {
    public static void main(String[] args) {
        Thread even = new Thread(new EvenAndOdd(1),"Even");
        Thread odd = new Thread(new EvenAndOdd(0),"Odd");
        even.start();
        odd.start();
    }
}

class EvenAndOdd implements Runnable{

    private int remainder;
    private static final Object object = new Object();
    private static int number = 1;

    public EvenAndOdd(int remainder) {
        this.remainder = remainder;
    }

    @Override
    public void run() {
        while(true){
            synchronized (object){
                while (number%2==remainder){
                    try {
                        System.out.println(Thread.currentThread().getName() + " thread is waiting");
                        object.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("printing value " + number + " by " + Thread.currentThread().getName() + " thread");
                ++number;
                object.notifyAll();

                if(number>=10)
                    break;
            }
        }
    }
}

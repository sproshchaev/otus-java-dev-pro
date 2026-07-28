package ru.otus.jmm.demo;

public class SynchronizedBlockCounter {

    private final Object lock = new Object();
    private int value = 0;
    private long callCount = 0;

    public void increment() {
        callCount++;                 // вне критической секции
        synchronized (lock) {
            value++;                 // внутри критической секции
        }
    }

    public int getValue() {
        synchronized (lock) {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedBlockCounter counter = new SynchronizedBlockCounter();

        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Защищённое поле value: " + counter.getValue());
        System.out.println("Незащищённое поле callCount: " + counter.callCount);
    }
}

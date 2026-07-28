package ru.otus.jmm.demo;

public class CounterSynchronized {

    private static int value = 0;

    private static synchronized void increment() {
        value++;
    }

    private static synchronized int getValue() {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long time = System.currentTimeMillis() - start;

        System.out.println("Получили: " + getValue());
        System.out.println("Время, мс: " + time);
    }
}

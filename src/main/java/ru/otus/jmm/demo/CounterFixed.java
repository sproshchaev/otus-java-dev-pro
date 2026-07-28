package ru.otus.jmm.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterFixed {

    private static final AtomicInteger value = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                value.incrementAndGet();
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

        System.out.println("Получили: " + value.get());
        System.out.println("Время, мс: " + time);
    }
}

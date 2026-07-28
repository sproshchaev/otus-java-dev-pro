package ru.otus.jmm.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class CasLoopCounter {

    private static final AtomicInteger value = new AtomicInteger(0);
    private static final AtomicInteger retries = new AtomicInteger(0);

    private static void increment() {
        while (true) {
            int oldValue = value.get();      // ожидаемое значение
            int newValue = oldValue + 1;     // новое значение
            if (value.compareAndSet(oldValue, newValue)) {
                return;                      // получилось, выходим
            }
            retries.incrementAndGet();       // помешали, пробуем снова
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Получили: " + value.get());
        System.out.println("Повторных попыток: " + retries.get());
    }
}

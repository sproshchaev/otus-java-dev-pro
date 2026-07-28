package ru.otus.jmm.demo;

public class VolatileCounterDemo {

    private static volatile int value = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                value++;
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Ожидали: " + 2_000_000);
        System.out.println("Получили: " + value);
    }
}

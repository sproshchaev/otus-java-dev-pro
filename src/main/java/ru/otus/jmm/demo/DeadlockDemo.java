package ru.otus.jmm.demo;

public class DeadlockDemo {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread first = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Поток 1 захватил lockA");
                sleep(100);
                System.out.println("Поток 1 ждёт lockB");
                synchronized (lockB) {
                    System.out.println("Поток 1 захватил lockB");
                }
            }
        }, "Поток-1");

        Thread second = new Thread(() -> {
            synchronized (lockB) {                  // порядок захвата обратный
                System.out.println("Поток 2 захватил lockB");
                sleep(100);
                System.out.println("Поток 2 ждёт lockA");
                synchronized (lockA) {
                    System.out.println("Поток 2 захватил lockA");
                }
            }
        }, "Поток-2");

        first.setDaemon(true);
        second.setDaemon(true);

        first.start();
        second.start();

        first.join(3000);
        second.join(3000);

        System.out.println("Поток 1 жив: " + first.isAlive());
        System.out.println("Поток 2 жив: " + second.isAlive());
        System.out.println("Программа остановилась, ошибок при этом нет");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

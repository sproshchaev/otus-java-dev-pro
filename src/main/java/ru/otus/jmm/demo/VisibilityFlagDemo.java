package ru.otus.jmm.demo;

public class VisibilityFlagDemo {

    private static boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            long count = 0;
            while (running) {
                count++;
            }
            System.out.println("Рабочий поток остановлен, итераций: " + count);
        });
        worker.setDaemon(true);
        worker.start();

        Thread.sleep(1000);
        running = false;
        System.out.println("Главный поток выставил running = false");

        worker.join(3000);
        System.out.println("Рабочий поток всё ещё жив: " + worker.isAlive());
    }
}

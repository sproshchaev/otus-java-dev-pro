package ru.otus.jmm.demo;

import java.util.ArrayList;
import java.util.List;

public class StartHappensBeforeDemo {

    private static int timeout = 0;                       // без volatile
    private static String name = null;                    // без volatile
    private static List<String> items = new ArrayList<>(); // обычная коллекция

    public static void main(String[] args) throws InterruptedException {
        // подготовка состояния выполняется до запуска потока
        timeout = 5000;
        name = "worker-config";
        items.add("первый");
        items.add("второй");

        Thread worker = new Thread(() -> {
            System.out.println("timeout = " + timeout);
            System.out.println("name = " + name);
            System.out.println("items = " + items);
        });

        worker.start();     // граница happens-before
        worker.join();

        System.out.println("Главный поток завершён");
    }
}

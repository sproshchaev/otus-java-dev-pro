package ru.otus.jmm.demo;

public class FinalFieldExample {

    final int x;
    int y;
    static FinalFieldExample f;

    public FinalFieldExample() {
        x = 3;
        y = 4;
    }

    static void writer() {                  // Один поток
        f = new FinalFieldExample();
    }

    static void reader() {                  // Другой поток
        FinalFieldExample local = f;
        if (local != null) {
            int i = local.x;                // guaranteed to see 3
            int j = local.y;                // could see 0
            if (j == 0) {
                System.out.println("Увидели недостроенный объект: x=" + i + ", y=" + j);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                writer();
            }
        });

        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < 10_000_000; i++) {
                reader();
            }
        });

        writerThread.start();
        readerThread.start();
        writerThread.join();
        readerThread.join();

        System.out.println("Готово");
    }
}

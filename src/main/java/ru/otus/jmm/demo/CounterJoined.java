package ru.otus.jmm.demo;

public class CounterJoined {

    static class CountingTask implements Runnable {

        private final int iterations;
        private int localValue = 0;      // персональная переменная потока

        CountingTask(int iterations) {
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for (int i = 0; i < iterations; i++) {
                localValue++;            // синхронизация не нужна
            }
        }

        int getLocalValue() {
            return localValue;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountingTask task1 = new CountingTask(1_000_000);
        CountingTask task2 = new CountingTask(1_000_000);

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();

        t1.join();                       // дождались первого
        t2.join();                       // дождались второго

        int total = task1.getLocalValue() + task2.getLocalValue();
        long time = System.currentTimeMillis() - start;

        System.out.println("Поток 1 насчитал: " + task1.getLocalValue());
        System.out.println("Поток 2 насчитал: " + task2.getLocalValue());
        System.out.println("Итого: " + total);
        System.out.println("Время, мс: " + time);
    }
}

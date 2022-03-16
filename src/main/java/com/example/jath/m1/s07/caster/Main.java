package com.example.jath.m1.s07.caster;

public class Main {
    private static final int NR = 3;

    public static void main(String[] args) {
        System.out.printf("Casting %d dice, sometimes a die get lost!%n", NR);

        Worker[] workers = new Worker[NR];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker();
        }

        for (Worker worker : workers) {
            worker.start();
        }

        int result = 0;
        for (Worker worker : workers) {
            try {
                worker.join(100);
                int die = worker.getResult();
                result += die;

                System.out.print(die);
                if (worker.isAlive()) {
                    System.out.println(" lost!");
                } else {
                    System.out.println(" ok");
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        System.out.println("Your result is " + result);
    }

}

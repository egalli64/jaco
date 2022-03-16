package com.example.jath.m1.s07.caster;

public class Worker extends Thread {
    private static final int MAX_VALUE = 6;
    private int result = 0;

    @Override
    public void run() {
        // !!! simulating a bug !!!
        if (Math.random() > 0.5) {
            // simulating something goes horribly wrong
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                // very brutal reaction
                throw new IllegalStateException(e);
            }
        }

        result = (int) Math.ceil(Math.random() * MAX_VALUE);
        System.out.println("[" + result + "]");
    }

    public int getResult() {
        return result;
    }
}

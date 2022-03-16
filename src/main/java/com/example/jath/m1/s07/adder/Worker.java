package com.example.jath.m1.s07.adder;

public class Worker extends Thread {
    private long[] data;
    private int begin;
    private int end;
    private double result = 0.0;

    Worker(long[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            result += Math.cbrt(data[i]);
        }
    }

    public double getResult() {
        return result;
    }
}

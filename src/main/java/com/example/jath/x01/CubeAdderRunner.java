package com.example.jath.x01;

public class CubeAdderRunner implements Runnable {

    private double[] data;
    private int begin;
    private int end;
    private double result;

    public CubeAdderRunner(double[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
        this.result = 0;
    }

    public double result() {
        return result;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            result += Math.pow(data[i], 3);
        }
    }
}

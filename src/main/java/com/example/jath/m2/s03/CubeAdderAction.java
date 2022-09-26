package com.example.jath.m2.s03;

import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class CubeAdderAction extends RecursiveAction {
    private static final int THRESHOLD = 200_000;

    private double[] data;
    private int begin;
    private int end;
    private double result;

    public CubeAdderAction(double[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
        this.result = 0;
    }

    @Override
    protected void compute() {
        if (end - begin <= THRESHOLD) {
            for (int i = begin; i < end; i++) {
                result += Math.pow(data[i], 3);
            }
        } else {
            CubeAdderAction left = new CubeAdderAction(data, begin, (begin + end) / 2);
            CubeAdderAction right = new CubeAdderAction(data, (begin + end) / 2, end);
            left.fork();
            right.compute();
            left.join();

            result = left.result + right.result;
        }
    }

    public double result() {
        return result;
    }
}

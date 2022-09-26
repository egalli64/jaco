package com.example.jath.m2.s02;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FixedThreadExecutor {
	private static final int POOL_SIZE = 2;

	public static void main(String[] args) {
		System.out.println("Fixed " + POOL_SIZE + " Thread Pool on Runnables");
		Executor executor = Executors.newFixedThreadPool(POOL_SIZE);
		for (int i = 0; i < 5; i++) {
			executor.execute(new Hello(i));
		}
	}
}
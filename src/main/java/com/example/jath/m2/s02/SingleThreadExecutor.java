package com.example.jath.m2.s02;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingleThreadExecutor {
	public static void main(String[] args) {
		System.out.println("Single Thread Executor on Runnables");
		Executor executor = Executors.newSingleThreadExecutor();
		for (int i = 0; i < 5; i++) {
			executor.execute(new Hello(i));
		}
	}
}
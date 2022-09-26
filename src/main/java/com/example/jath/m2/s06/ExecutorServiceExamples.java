package com.example.jath.m2.s06;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExamples {
	private static final int POOL_SIZE = 2;

	public static void main(String[] args) throws Exception {
		System.out.println("Fixed Thread Pool on " + POOL_SIZE + " Runnables");
		ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

		List<Future<Integer>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Future<Integer> future = es.submit(new Hello(i));
			futures.add(future);
		}
		es.shutdown();

		for (var future : futures) {
			System.out.println("Result " + future.get() + " delivered");
		}
	}
}
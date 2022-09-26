package com.example.jath.m2.s06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class ShutdownExecutor {
	public static void main(String[] args) {
		System.out.println("Single Thread Executor on Runnables");
		ExecutorService es = Executors.newSingleThreadExecutor();
		for (int i = 0; i < 3; i++) {
			es.execute(() -> System.out.println("Hello"));
		}
		es.shutdown();
		try {
			es.execute(() -> System.out.println("Rejected"));
		} catch (RejectedExecutionException ex) {
			System.out.println("Can't add a task after shutdown");
		}
	}
}
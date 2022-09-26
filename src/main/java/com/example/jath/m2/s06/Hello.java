package com.example.jath.m2.s06;

import java.util.concurrent.Callable;

public class Hello implements Callable<Integer> {
	private int x;

	public Hello(int x) {
		this.x = x;
	}

	@Override
	public Integer call() throws Exception {
		System.out.println("Hello " + x);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Goodbye " + x);

		return x;
	}
}

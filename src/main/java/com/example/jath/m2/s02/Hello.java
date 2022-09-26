package com.example.jath.m2.s02;

public class Hello implements Runnable {
	private int x;

	public Hello(int x) {
		this.x = x;
	}

	@Override
	public void run() {
		System.out.println("Hello " + x);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Goodbye " + x);
	}

}

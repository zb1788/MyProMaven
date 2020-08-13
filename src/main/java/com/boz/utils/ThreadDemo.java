package com.boz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ThreadDemo {
	private static final Logger LOGGER  = LoggerFactory.getLogger(ThreadDemo.class);
	private static void threadMany(){
		for(int i=0; i<20; i++){
			int len=500;
			final CountDownLatch countDownLatch = new CountDownLatch(len);
			for(int j=0;j<len;j++)
			{
				new Thread() {
					public void run(){
						//System.out.println("Thread:"+currentThread().getName());
						LOGGER.info("Thread:"+currentThread().getName());
						countDownLatch.countDown();
					}
				}.start();
			}
		}
	}
	
	public static void main(String[] args) {
		threadMany();
	}
}

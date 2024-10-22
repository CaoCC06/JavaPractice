package com.cancan;

public class WaitNotifyDemo {

	public static void main(String[] args) {
		
	}
}

//生产者
class Producer implements Runnable{

	@Override
	public void run() {
		
	}
}

//消费者

//产品
class Product{
	private int count;
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	//false表示生产者生产，true表示消费者消费
	public boolean flag = false;
}

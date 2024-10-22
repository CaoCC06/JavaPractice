package com.cancan;


//一个简单的死锁
public class synchronizedThreadDemo {
	private static Printer p = new Printer();
	private static Scanner s = new Scanner();
	
	public static void main(String[] args) {
		//第一个员工
		new Thread(()->{
			try {
				synchronized (s) {
					s.scan();
					Thread.sleep(1000);
					synchronized (p) {
						p.print();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		//第二个员工
		new Thread(()->{
			try {
				synchronized (p) {
					p.print();
					Thread.sleep(1000);
					synchronized (s) {
						s.scan();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
class Printer {
	public void print() {
		System.out.println("print-----");
	}
}

class Scanner {
	public void scan() {
		System.out.println("scan-----");
	}
}

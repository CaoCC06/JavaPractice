package com.cancan;

public class SingleTonDemo {
	public static void main(String[] args) {
		TaskManager tm = TaskManager.getInstance();
		TaskManager tm1 = TaskManager.getInstance();
		
		
		
		
		
	}
}


//饿汉式
class TaskManager {
	private static TaskManager tManager = new TaskManager();
	
	private TaskManager() {
		
	}
	
	public static TaskManager getInstance() {
		System.out.println(tManager);
		return tManager;
	}
}

//懒汉式（饱汉式）线程不安全
class LazyTaskManager{
private static LazyTaskManager lazyTask;
	
	private LazyTaskManager() {
		
	}
	
	public static LazyTaskManager getInstance() {
		if (lazyTask == null) lazyTask = new LazyTaskManager();
		return lazyTask;
	}
}

//懒汉式（饱汉式）线程安全（加锁）
class LazyLockTaskManager{
private static LazyLockTaskManager lazyLockTask;
	
	private LazyLockTaskManager() {
		
	}
	
	public static LazyLockTaskManager getInstance() {
		synchronized (LazyTaskManager.class) {
			if (lazyLockTask == null) lazyLockTask = new LazyLockTaskManager();
		}
		return lazyLockTask;
	}
}


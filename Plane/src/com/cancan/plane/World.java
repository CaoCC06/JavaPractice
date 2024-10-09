package com.cancan.plane;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
//	导演类，用于管理所有对象的创建、销毁以及对象的数量
//	定义游戏界面的大小
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	
	Sky sky = new Sky();
	EnemyPlane enemyPlane1 = new EnemyPlane();
	EnemyPlane enemyPlane2 = new EnemyPlane();
	EnemyPlane enemyPlane3 = new EnemyPlane();
	
	public void paint(Graphics g) {
		sky.paintObject(g);
		enemyPlane1.paintObject(g);
		enemyPlane2.paintObject(g);
		enemyPlane3.paintObject(g);
	}
	
//	定义启动方法
	public void start() {
		
//		定义定时器
		Timer timer = new Timer();
//		定义一个变量，设定游戏运行速度（运行间隔）,间隔越小，运行速度越快，难度越大（建议范围30-50）
		int interval = 30;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sky.step();//天空移动
				enemyPlane1.step();
				enemyPlane2.step();
				enemyPlane3.step();
//				改变了对象坐标之后重新绘制整个窗口
				repaint();
			}
		};
//		周期性运行定时器
		timer.schedule(task, interval, interval);
	}
	
	
	public static void main(String[] args) {
		World w = new World();
//		创建主窗体
		JFrame frame = new JFrame("plane war");
//		将world对象显示到窗口
		frame.add(w);
		
//		设置窗体大小
		frame.setSize(WIDTH,HEIGHT);
		
		
//		居中显示
		frame.setLocationRelativeTo(null);
		
//		默认窗体销毁后不结束进程，故设置窗体销毁后结束进程
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		显示窗体
		frame.setVisible(true);
		
		w.start();
	}

}

package com.cancan.plane;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel{
//	导演类，用于管理所有对象的创建、销毁以及对象的数量
//	定义游戏界面的大小
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	
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
	}

}

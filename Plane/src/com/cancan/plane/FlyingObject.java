package com.cancan.plane;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

//所有飞行物的父类
public class FlyingObject {
//	定义飞行物状态，三种状态：活着，死亡，移除
	public static final int LIFE = 0;	//活着
	public static final int DEAD = 1;	//死亡
	public static final int REMOVE = 2;	//移除
	
//	设置当前飞行物状态
	protected int state = LIFE;	//默认存活
	
//	设置大小
	int width;
	int height;
	
//	坐标
	int x;	//X轴
	int y;	//Y轴
	
//	敌机构造方法:大小敌机与奖励机
	public FlyingObject(int width,int height) {
		this.width = width;
		this.height = height;
		
//		从顶部随机出现，故y轴固定
		this.y = -height;
		
		Random random = new Random();
		this.x = random.nextInt(400-width);
		
	}
	
//	天空、子弹和角色
	public FlyingObject(int x,int y,int width,int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
	}
	
//	将图片加载到内存中
	static public BufferedImage readImage(String filename) {
//		根据filename指定的文件名，将图片文件转换成Java中的图片类型对象，一般用相对路径
		try {
			BufferedImage image = ImageIO.read(FlyingObject.class.getResource(filename));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
}

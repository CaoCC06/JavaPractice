package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSize.Other;

//所有飞行物的父类
public abstract class FlyingObject {
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
	
	int life;
	
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
	
//	扣血
	public void subLife() {
		life --;
		if (life < 0) {
			goDead();
		}
	}
	
//	判断敌机是否出界
	public boolean isOutBounds() {
		return this.y >=World.HEIGHT;
	}
	
//	判断是否活着
	public boolean isLife() {
		return this.state == LIFE;
	}
	
//	判断是否是活着
	public boolean isDead() {
		return this.state == DEAD;
	}
	
//	判断是否发生碰撞
	public boolean isHit(FlyingObject other) {
		//this表示角色和子弹
		//other表示敌人
		//计算碰撞区域x1,x2,y1,y2
		int x1 = other.x - this.width;
		int x2 = other.x + other.width;
		int y1 = other.y - this.width;
		int y2 = other.y + other.width;
		
		return x>x1 && y>y1 && x<x2 && y<y2;
	}
	
	
//	修改状态
	public void goDead() {
		state = DEAD;
	}
	
//	是否移除
	public boolean isRemove() {
		return state == REMOVE;
	}
//	将获取的图片绘制到窗体
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}
	
	public abstract void step();
	public abstract BufferedImage getImage();
}

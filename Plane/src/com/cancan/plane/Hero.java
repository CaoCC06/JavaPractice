package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Hero extends FlyingObject{

	int newX;
	int newY;
	int index = 0;
	int step = 6;
	boolean present = true;
	int direction = 0;
	private static BufferedImage[] bufferedImage;
	private static BufferedImage[] bufferedImage1;
	private static BufferedImage[] bufferedImage2;
	private static BufferedImage[] heroImage;
	private static BufferedImage[] image;
	
	public Hero(int heroType) {
//		x轴是400的一般再减去角色的一半
		super(170, 500, 56, 85);
		newX = x;
		newY = World.HEIGHT;
		life = 3;

		image = new BufferedImage[6];
		switch (heroType) {
		case 0:
			heroImage = bufferedImage;
			break;
		case 1:
			heroImage = bufferedImage1;
			break;
		case 2:
			heroImage = bufferedImage2;
			break;
		}
		image[0] = heroImage[0].getSubimage(0, 0, 56, 85);
		image[1] = heroImage[0].getSubimage(57, 0, 56, 85);
		image[2] = heroImage[0].getSubimage(115, 0, 56, 85);
		image[3] = heroImage[0].getSubimage(168, 0, 56, 85);
		image[4] = heroImage[1].getSubimage(0, 0, 54, 85);
		image[5] = heroImage[1].getSubimage(56, 0, 54, 85);
	}
	
	static {
		//一号机
		bufferedImage = new BufferedImage[2];
		bufferedImage[0] = readImage("./imgs/plane1.png");
		bufferedImage[1] = readImage("./imgs/plane1a.png");
		//二号机
		bufferedImage1 = new BufferedImage[2];
		bufferedImage1[0] = readImage("./imgs/plane2.png");
		bufferedImage1[1] = readImage("./imgs/plane2a.png");
		//二号机
		bufferedImage2 = new BufferedImage[2];
		bufferedImage2[0] = readImage("./imgs/plane3.png");
		bufferedImage2[1] = readImage("./imgs/plane3a.png");
	}

	@Override
	public void step() {
		if (newY >= y) {
			newY -= step;
		}else {
			present = false;
		}
	}
//	移动
	public void moveTo(int xdirection,int ydirection) {
		if (xdirection > 0) {
			direction ++;
		}else if (xdirection < 0) {
			direction --;
		}else if (xdirection == 0) {
			direction = 0;
		}
		newX += xdirection;
		newY += ydirection;
//		System.out.print("X:"+newX+" "+"Y:"+newY);
	}

	@Override
//	获取图片
	public BufferedImage getImage() {
		if(direction > 0 && direction <= 5){
			return image[4];
		}else if(direction > 5){
			return image[5];
		}else if(direction < 0 && direction >= -5){
			return image[2];
		}else if(direction < -5){
			return image[3];
		}
		return image[index++%2];
	}

	@Override
//	将获取的图片绘制到窗体
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), newX, newY, null);
	}
	
//	发射子弹
	public Bullet[] shoot(int fire,int bulletType) {
		Bullet[] bullets = null;
		//获取1/4宽度
		int w = this.width/4 - 6;
		//获取1/3宽度
		int w1 = this.width/3 - 9;
		if (fire == 30) {
			//单子弹
			bullets = new Bullet[1];
			bullets[0] = new Bullet(this.newX+2*w,this.newY-20,bulletType);
		}else if(fire == 20) {
			//双子弹
			bullets = new Bullet[2];
			bullets[0] = new Bullet(this.newX+1*w,this.newY-20,bulletType);
			bullets[1] = new Bullet(this.newX+3*w,this.newY-20,bulletType);
		}else if(fire == 10) {
			//三子弹
			bullets = new Bullet[3];
			bullets[0] = new Bullet(this.newX+1*w1,this.newY-20,bulletType);
			bullets[1] = new Bullet(this.newX+2*w1,this.newY-20,bulletType);
			bullets[2] = new Bullet(this.newX+3*w1,this.newY-20,bulletType);
		}
		else if(fire == 5) {
			//极限火力
			bullets = new Bullet[4];
			bullets[0] = new Bullet(this.newX+1*w,this.newY-20,bulletType);
			bullets[1] = new Bullet(this.newX+2*w,this.newY-20,bulletType);
			bullets[2] = new Bullet(this.newX+3*w,this.newY-20,bulletType);
			bullets[3] = new Bullet(this.newX+4*w,this.newY-20,bulletType);
		}
		return bullets;
	}
	
//	判断是否发生碰撞(重写)
	public boolean isHit(FlyingObject other) {
		//this表示角色和子弹
		//other表示敌人
		//计算碰撞区域x1,x2,y1,y2
		int x1 = other.x - this.width;
		int x2 = other.x + other.width;
		int y1 = other.y - this.width;
		int y2 = other.y + other.width;
		
		return newX>x1 && newY>y1 && newX<x2 && newY<y2;
	}
	
	public void subLife() {
		this.life --;
	}
}

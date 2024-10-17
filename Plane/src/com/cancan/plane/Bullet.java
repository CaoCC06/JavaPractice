package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Bullet extends FlyingObject{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index = 2;
	int normalIndex = 1;

	public Bullet(int x,int y,int bulletType) {
		super(x, y, 20, 20);
		step = 7;
		switch (bulletType) {
		case 0:
			bufferedImage[0] = readImage("./imgs/bomb1_1.png");
			bufferedImage[1] = readImage("./imgs/bomb1_2.png");
			bufferedImage[2] = readImage("./imgs/bomb1_3.png");
			break;
		case 1:
			bufferedImage[0] = readImage("./imgs/bomb2_1.png");
			bufferedImage[1] = readImage("./imgs/bomb2_2.png");
			bufferedImage[2] = readImage("./imgs/bomb2_3.png");
			break;
		case 2:
			bufferedImage[0] = readImage("./imgs/bomb3_1.png");
			bufferedImage[1] = readImage("./imgs/bomb3_2.png");
			bufferedImage[2] = readImage("./imgs/bomb3_3.png");
			break;
		}
	}
	
	static {
		BufferedImage bomb = readImage("./imgs/blast_b.png");
		bufferedImage = new BufferedImage[7];
		bufferedImage[3] = bomb.getSubimage(0, 0, 36, 36);
		bufferedImage[4] = bomb.getSubimage(36, 0, 36, 36);
		bufferedImage[5] = bomb.getSubimage(72, 0, 36, 36);
		bufferedImage[6] = bomb.getSubimage(108, 0, 36, 36);
	}
	
	public boolean isOutBounds() {
		return y <= 0;
	}

	@Override
	public void step() {
		y -=step;
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage img = null;
		if (state == LIFE) {
			normalIndex++;
			img = bufferedImage[normalIndex%3];
		}else if(isDead()) {
			img = bufferedImage[index];//获取爆炸图
			index++;//准备下一张图片
			if(index==6) {//如果是最后一张图片
				state = REMOVE;//移除，使其消失
			}
		}
		return img;
	}
}

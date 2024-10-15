package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BigEnemyPlane extends FlyingObject implements Score{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index = 1;

	public BigEnemyPlane() {
		super(333, 369);
		step = 3;
		life = 6;
	}
	
	static {
		bufferedImage = new BufferedImage[2];
		bufferedImage[0] = readImage("./imgs/boss0_0.png");
		bufferedImage[1] = readImage("./imgs/boss0_3.png");
	}
	
//	获取图片
	public BufferedImage getImage() {
		BufferedImage img = null;
		if (state == LIFE) {
			return bufferedImage[0];
		}else if(isDead()) {
//			img = bufferedImage[index];//获取爆炸图
//			index++;//准备下一张图片
//			if(index==8) {//如果是最后一张图片
//				state = REMOVE;//移除，使其消失
//			}
			state = REMOVE;
		}
		return img;
	}

	@Override
	public void step() {
		if (y <= -30) {
			y += step;
		}
	}

	@Override
	public int getScore() {
		return 10;
	}

}

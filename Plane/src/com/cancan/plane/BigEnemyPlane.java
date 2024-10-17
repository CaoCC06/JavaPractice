package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BigEnemyPlane extends FlyingObject implements Score{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index = 1;

	public BigEnemyPlane() {
		super(200, 200);
		step = 3;
		life = 8;
	}
	
	static {
		BufferedImage bomb = readImage("./imgs/blast_be.png");
		bufferedImage = new BufferedImage[7];
		bufferedImage[0] = readImage("./imgs/boss0_0.png");
		bufferedImage[1] = readImage("./imgs/boss0_1.png");
		bufferedImage[2] = bomb.getSubimage(0, 0, 339, 333);
		bufferedImage[3] = bomb.getSubimage(339, 0, 339, 333);
		bufferedImage[4] = bomb.getSubimage(678, 0, 339, 333);
		bufferedImage[5] = bomb.getSubimage(1017, 0, 339, 333);
		bufferedImage[6] = bomb.getSubimage(1356, 0, 339, 333);
	}
	
//	获取图片
	public BufferedImage getImage() {
		BufferedImage img = null;
		if (state == LIFE) {
			if (life >= 5) {
				img = bufferedImage[0];
			}else if (life < 5) {
				img = bufferedImage[1];
			}
		}else if(isDead()) {
			img = bufferedImage[index];//获取爆炸图
			index++;//准备下一张图片
			if(index==7) {//如果是最后一张图片
				state = REMOVE;//移除，使其消失
			}
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

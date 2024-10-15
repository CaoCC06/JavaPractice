package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyPlane extends FlyingObject implements Score{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index;
	
	Boolean direction;
	Random random = new Random();

	public EnemyPlane() {
		super(79, 79);
		step = 4;
		direction = random.nextBoolean();
		life = 1;
	}
	
	static {
		BufferedImage bomb = readImage("./imgs/e_bomb.png");
		bufferedImage = new BufferedImage[8];
		bufferedImage[0] = readImage("./imgs/enemy13_0.png");
		bufferedImage[1] = readImage("./imgs/enemy13_1.png");
		bufferedImage[2] = bomb.getSubimage(0, 0, 79, 79);
		bufferedImage[3] = bomb.getSubimage(79, 0, 79, 79);
		bufferedImage[4] = bomb.getSubimage(158, 0, 79, 79);
		bufferedImage[5] = bomb.getSubimage(237, 0, 79, 79);
		bufferedImage[6] = bomb.getSubimage(316, 0, 79, 79);
		bufferedImage[7] = bomb.getSubimage(395, 0, 79, 79);
	}
	
//	获取图片
	public BufferedImage getImage() {
		BufferedImage img = null;
		if (state == LIFE) {
			return bufferedImage[index % 2];
		}else if(isDead()) {
			img = bufferedImage[index];//获取爆炸图
			index++;//准备下一张图片
			if(index==8) {//如果是最后一张图片
				state = REMOVE;//移除，使其消失
			}
		}
		return img;
	}
	
	public void step() {
		if(direction) {
			if (y <= height) {
				y += step;
			}
		}else {
			y += step;
		}
	}

	@Override
	public int getScore() {
		return 2;
	}
}

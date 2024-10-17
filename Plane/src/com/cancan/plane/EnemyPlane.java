package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyPlane extends FlyingObject implements Score{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index = 4;
	int normalIndex = 1;
	
	Random random = new Random();
	int form;

	public EnemyPlane() {
		super(70, 70);
		step = 4;
		life = 1;
		form = random.nextInt(3);
	}
	
	static {
		BufferedImage bomb = readImage("./imgs/blast_e.png");
		bufferedImage = new BufferedImage[10];
		bufferedImage[0] = readImage("./imgs/enemy1_0.png");
		bufferedImage[1] = readImage("./imgs/enemy1_1.png");
		bufferedImage[2] = readImage("./imgs/enemy2.png");
		bufferedImage[3] = readImage("./imgs/enemy3.png");
		bufferedImage[4] = bomb.getSubimage(0, 0, 79, 79);
		bufferedImage[5] = bomb.getSubimage(79, 0, 79, 79);
		bufferedImage[6] = bomb.getSubimage(158, 0, 79, 79);
		bufferedImage[7] = bomb.getSubimage(237, 0, 79, 79);
		bufferedImage[8] = bomb.getSubimage(316, 0, 79, 79);
		bufferedImage[9] = bomb.getSubimage(395, 0, 79, 79);
	}
	
//	获取图片
	public BufferedImage getImage() {
		BufferedImage img = null;
		if (state == LIFE) {
			if (form == 0) {
				normalIndex++;
				img = bufferedImage[normalIndex%2];
			}else if (form == 1) {
				img = bufferedImage[2];
			}else {
				img = bufferedImage[3];
			}
		}else if(isDead()) {
			img = bufferedImage[index];//获取爆炸图
			index++;//准备下一张图片
			if(index==10) {//如果是最后一张图片
				state = REMOVE;//移除，使其消失
			}
		}
		return img;
	}
	
	public void step() {
		y += step;
	}

	@Override
	public int getScore() {
		return 2;
	}
}

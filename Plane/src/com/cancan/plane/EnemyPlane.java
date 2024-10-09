package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyPlane extends FlyingObject{
	
	private static BufferedImage[] bufferedImage;
	int step;
	
	Boolean direction;

	public EnemyPlane() {
		super(79, 79);
		step = 4;
		Random random = new Random();
		direction = random.nextBoolean();
	}
	
	static {
		bufferedImage = new BufferedImage[2];
		bufferedImage[0] = readImage("enemy13_0.png");
		bufferedImage[1] = readImage("enemy13_1.png");
	}
	
//	获取图片
	public BufferedImage getImage() {
		return bufferedImage[0];
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

//	将获取的图片绘制到窗体
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}
}

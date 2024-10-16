package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Road extends FlyingObject{
	
	private static BufferedImage bufferedImage;
	int step;

	public Road() {
		super(-40, 200, 400,700);
		step = 5;
	}
	
	static {
		bufferedImage = readImage("./imgs/bg_road.png");
	}

	@Override
	public void step() {
		y += step;
		if (y > World.HEIGHT) {
			state = REMOVE;
		}
	}

	@Override
	public BufferedImage getImage() {
		return bufferedImage;
	}
	
//	将获取的图片绘制到窗体
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}
	
	
}

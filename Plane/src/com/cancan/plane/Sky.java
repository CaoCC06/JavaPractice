package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
	
	private static BufferedImage bufferedImage;
	int step;
	
	static {
		bufferedImage = readImage("./imgs/bg1.jpg");
	}
	
//	第二张图片
	int y1;

	public Sky() {
		super(0, 0, 400,700);
		step = 1;
		y1 = -700;
	}
//	获取图片
	public BufferedImage getImage() {
		return bufferedImage;
	}
	
	public void step() {
		y += step;
		y1 += step;
//		当第一张图片完全移出窗口后重新回到上方
		if(y >= World.HEIGHT) {
			y = -World.HEIGHT;
		}
		
//		第二张同理
		if(y1 >= World.HEIGHT) {
			y1 = -World.HEIGHT;
		}
	}
	
//	将获取的图片绘制到窗体
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
		g.drawImage(getImage(), x, y1, null);
	}
	
}

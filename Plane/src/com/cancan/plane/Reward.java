package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Reward extends FlyingObject{
	
	private static BufferedImage bufferedImage;
	private static BufferedImage[] image;
	int yStep;
	int xStep;
	int index;
	int direction;
	int num = 0;
	Random random = new Random();

	public Reward() {
		super(50, 50);
		yStep = 5;
		xStep = random.nextInt(10) - 5;
		direction = random.nextInt(2);
	}
	static {
		bufferedImage = readImage("./imgs/props2.png");
		image = new BufferedImage[3];
		image[0] = bufferedImage.getSubimage(0, 0, 51, 50);
		image[1] = bufferedImage.getSubimage(52, 0, 51, 50);
		image[2] = bufferedImage.getSubimage(104, 0, 51, 50);
	}

	
	public void step() {
		int d = bounds();
//		System.out.println(d);
		if (num != 0) {
			switch (d) {
			case 0: 
				break;
			case 1: 
				xStep = -xStep;
				break;
			case 2: 
				yStep = -yStep;
				break;
			case 3: 
				xStep = -xStep;
				break;
			case 4: 
				yStep = -yStep;
				break;
			}
		}
		if (num >= 4) {
			this.state = REMOVE;
		}
		y += yStep;
		x += xStep;
	}

	@Override
	public BufferedImage getImage() {
		return image[index++%3];
	}
	
//	是否碰到边界
	public int bounds() {
		int xr = this.x + this.width;
		int yb = this.y + this.height;
		if (x <= 0) {
			num ++;
			return 1;
		}else if (y <=0 && num != 0) {
			num ++;
			return 2;
		}else if (xr >= World.WIDTH) {
			num ++;
			return 3;
		}else if (yb >= World.HEIGHT) {
			num ++;
			return 4;
		}
		return 0;
	}
}

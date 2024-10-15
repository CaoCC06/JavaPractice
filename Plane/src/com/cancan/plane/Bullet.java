package com.cancan.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class Bullet extends FlyingObject{
	
	private static BufferedImage[] bufferedImage;
	int step;
	int index = 2;

	public Bullet(int x,int y) {
		super(x, y, 26, 42);
		step = 7;
	}
	
	static {
		bufferedImage = new BufferedImage[9];
		bufferedImage[0] = readImage("./imgs/assisent1_3.png");
		bufferedImage[1] = readImage("./imgs/assisent1_4.png");
		for (int i = 0; i <= 6; i++) {
			bufferedImage[i+2] = readImage("./imgs/f_bomb_"+i+".png");
		}
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
			index++;
			img = bufferedImage[index%2];
		}else if(isDead()) {
			img = bufferedImage[index];//获取爆炸图
			index++;//准备下一张图片
			if(index==9) {//如果是最后一张图片
				state = REMOVE;//移除，使其消失
			}
		}
		return img;
	}
}

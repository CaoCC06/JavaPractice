package com.cancan.plane;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel implements KeyListener{
//	导演类，用于管理所有对象的创建、销毁以及对象的数量
//	定义游戏界面的大小
	public static final int WIDTH = 400;
	public static final int HEIGHT = 700;
	public static final int[] FIRELEVEL = {30,20,10,5};
	public static final int SUPERFIRE = 100;
	
//	游戏状态
	public static final int START = 0;	//开始
	public static final int	RUNNING =1;	//进行
	public static final int	PAUSE = 2;	//暂停
	public static final int	GAME_OVER = 3;	//结束
	
//	游戏状态图片
	private static BufferedImage startImage;
	private static BufferedImage pauseImage;
	private static BufferedImage overImage;
	
	int newFire = 0;
	int score = 0;
	
//	加载图片
	static {
		
	}
	
	Sky sky = new Sky();
	Hero hero = new Hero();
	FlyingObject[] bullets = new Bullet[] {};
	FlyingObject[] enemys = new FlyingObject[] {};
	FlyingObject[] rewards = new FlyingObject[] {};
	
//	绘制
	public void paint(Graphics g) {
		sky.paintObject(g);
		//绘制敌机
		for(int i=0;i < enemys.length;i++) {
			enemys[i].paintObject(g);
		}
		for (int i=0;i < bullets.length;i++) {
			bullets[i].paintObject(g);
		}
		for (int i=0;i < rewards.length;i++) {
			rewards[i].paintObject(g);
		}
		hero.paintObject(g);
		
		//显示游戏信息
		g.drawString("分数："+score, 10, 20);
		g.drawString("生命："+hero.life, 10, 40);
		g.drawString("分数："+newFire, 10, 20);
	}
	
//	定义启动方法
	public void start() {
		//定义定时器
		Timer timer = new Timer();
		//定义一个变量，设定游戏运行速度（运行间隔）,间隔越小，运行速度越快，难度越大（建议范围30-50）
		int interval = 40;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				//天空移动
				sky.step();
				if (hero.present == true) {
					hero.step();
				}else {
					enemyEnterAction();
					bulletEnterAction(FIRELEVEL[newFire]);
					flyObjectMoveEvent();
					bulletHitEnemy();
					outBoundsAction();
					heroHitReward();
					heroHitEnemy();
				}
				//改变了对象坐标之后重新绘制整个窗口
				repaint();
			}
		};
		//周期性运行定时器
		timer.schedule(task, interval, interval);
	}
	
//	除角色外所有对象的移动方法
	public void flyObjectMoveEvent() {
		for (int i=0;i < bullets.length;i++) {
			bullets[i].step();
		}
		for(int i=0;i < enemys.length;i++) {
			enemys[i].step();
		}
		for(int i=0;i < rewards.length;i++) {
			rewards[i].step();
		}
	}
	
//	控制子弹频率
	int bulletIndex = 1;
	int superFireIndex = 1;
//	子弹入场(火力fire{5,10,20,30}越小强)
	public void bulletEnterAction(int fire) {
		if (bulletIndex % (fire + 10) == 0) {
			Bullet[] bls = hero.shoot(fire);
			//扩容
			bullets = Arrays.copyOf(bullets, bullets.length + bls.length);
			System.arraycopy(bls, 0, bullets, bullets.length - bls.length, bls.length);
		}
		if (fire == 5) {
			superFireIndex ++;
			if (superFireIndex % SUPERFIRE == 0) {
				newFire -= 1;
				superFireIndex = 0;
			}
		}
		bulletIndex++;
	}
	
//	随机产生敌机
	public FlyingObject nextEnemy() {
		FlyingObject flyingObject = null;
		Random random = new Random();
		int num = random.nextInt(100);
//		System.out.println(num);
		if (num<40) {
			flyingObject = new BigEnemyPlane();
		}else if (num>=40 && num <= 90) {
			flyingObject = new EnemyPlane();
		}else {
			flyingObject = new Reward();
		}
//		flyingObject = new Reward();
		return flyingObject;
	}
	
//	敌机入场
	int enemyIndex = 1;
	public void enemyEnterAction() {
		if(enemyIndex % 60 ==0) {
			FlyingObject flyingObject = nextEnemy();
//			System.out.println(flyingObject instanceof Reward);
			if (flyingObject instanceof Reward) {
				rewards = Arrays.copyOf(rewards, rewards.length+1);
				rewards[rewards.length - 1] = flyingObject;
			}else {
				enemys = Arrays.copyOf(enemys, enemys.length+1);
				enemys[enemys.length - 1] = flyingObject;
			}
//			System.out.println(rewards.length);
		}
		enemyIndex++;
	}
	
	
//	将对象移出数组
	public void outBoundsAction() {
		//index既可以在复制的时候作为下标，也可以在遍历完数组后作为新数组的长度
		int index = 0;
		FlyingObject[] fs = new FlyingObject[enemys.length];
		//遍历enemys数组，判断飞机是否出界与移除
		for (int i = 0; i < enemys.length; i++) {
			FlyingObject f = enemys[i];
			//如果没有出界也没有移除，则放入新数组
			if (!f.isOutBounds() && !f.isRemove()) {
				fs[index] = f;
				index++;
			}
		}
		enemys = Arrays.copyOf(fs, index);
//		System.out.println("enemys的长度:"+enemys.length);
		
		int indexB = 0;
		FlyingObject[] bs = new FlyingObject[bullets.length];
		for (int i = 0; i < bs.length; i++) {
			FlyingObject b = bullets[i];
			//如果没有出界也没有移除，则放入新数组
			if (!b.isOutBounds() && !b.isRemove()) {
				bs[indexB] = b;
				indexB++;
			}
		}
		bullets = Arrays.copyOf(bs, indexB);
//		System.out.println("bullets的长度:"+bullets.length);
		
		int indexR = 0;
		FlyingObject[] rs = new FlyingObject[rewards.length];
		for (int i = 0; i < rs.length; i++) {
			FlyingObject r = rewards[i];
			//如果没有出界也没有移除，则放入新数组
			if (!r.isRemove() && !r.isDead()) {
				rs[indexR] = r;
				indexR++;
			}
		}
		rewards = Arrays.copyOf(rs, indexR);
	}
	
//	碰撞测试(子弹碰撞敌机)
	public void bulletHitEnemy() {
		if (bullets.length != 0 && enemys.length != 0) {
			for(int i = 0; i < bullets.length; i++) {
				FlyingObject b = bullets[i];
				for(int j = 0; j < enemys.length; j++) {
					FlyingObject f = enemys[j];
//					System.out.println(b.isHit(f));
					if(b.isHit(f) && b.isLife() && f.isLife()) {
						Random r = new Random();
						int n = r.nextInt(10);
						b.goDead();
						if (f instanceof EnemyPlane) {
							if (n > 7) {
								Reward er = new Reward();
								rewards = Arrays.copyOf(rewards, rewards.length+1);
								rewards[rewards.length - 1] = er;
							}
							f.goDead();
						}else if (f instanceof BigEnemyPlane) {
							f.subLife();
						}
//						System.out.println(b.state);
						//判断击中的敌机的类型
						if(f instanceof Score) {
							//获取不同类型的敌机的分数，并且加载到总分中
							Score s = (Score)f;
							score += s.getScore();
						}
					}
				}
			}
		}
	}
	
//	碰撞测试(角色碰撞奖励)
	public void heroHitReward() {
		if (rewards.length != 0) {
			for (int i = 0; i < rewards.length; i++) {
				FlyingObject r = rewards[i];
				if(hero.isHit(r) && r.isLife()) {
					r.goDead();
					if (newFire != FIRELEVEL.length-1) {
						newFire += 1;
					}
				}
			}
		}
	}
	
//	碰撞测试（角色与敌机）
	public void heroHitEnemy() {
		if (enemys.length != 0) {
			for(int i=0;i<enemys.length;i++) {
				FlyingObject f = enemys[i];
				//判断英雄机是否与敌机发生碰撞
				if(hero.isHit(f) && f.isLife()) {
					f.goDead();
					hero.subLife();
					newFire --;
				}
			}
		}
	}
	
//	在画面里移出死亡、出界的飞行物

	
	
	public static void main(String[] args) {
		World w = new World();
		//创建主窗体
		JFrame frame = new JFrame("plane war");
		//将world对象显示到窗口
		frame.add(w);
		//设置窗体大小
		frame.setSize(WIDTH,HEIGHT);
		//居中显示
		frame.setLocationRelativeTo(null);
		//默认窗体销毁后不结束进程，故设置窗体销毁后结束进程
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//显示窗体
		frame.setVisible(true);
		
		frame.addKeyListener(w);
		w.start();
	}

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		hero.moveTo(0, 6);
	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
		hero.moveTo(0, -6);
	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		hero.moveTo(6, 0);
	} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		hero.moveTo(-6, 0);
	}
}

@Override
public void keyReleased(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_DOWN ||e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode() == KeyEvent.VK_RIGHT||e.getKeyCode() == KeyEvent.VK_LEFT) {
		hero.moveTo(0, 0);
	}
}

}

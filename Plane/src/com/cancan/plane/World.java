package com.cancan.plane;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
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
	private static BufferedImage[] startImage;
	private static BufferedImage[] pauseImage;
	private static BufferedImage[] overImage;
	
	int newFire = 0;
	int score = 0;
	
	//定义游戏的当前状态
	private int state = START;
	
//	加载图片
	static {
		//加载开始前图片
		startImage = new BufferedImage[4];
		startImage[0] = readImage("./imgs/back_of_about.jpg");
		startImage[1] = readImage("./imgs/main_Btn_pressed8.png");
		startImage[2] = readImage("./imgs/main_logo.png");
		startImage[3] = readImage("./imgs/main_plane_0.png");
	}
	
	Sky sky = new Sky();
	Road road = new Road();
	Hero hero = new Hero();
	FlyingObject[] bullets = new Bullet[] {};
	FlyingObject[] enemys = new FlyingObject[] {};
	FlyingObject[] rewards = new FlyingObject[] {};
	
//	绘制
	public void paint(Graphics g) {
		switch (state) {
		case START:
			//绘制开始界面
			g.drawImage(startImage[0], -21, -10, null);
			g.drawImage(startImage[1], 100, 400, null);
			g.drawImage(startImage[3], -10, 50, null);
			g.drawImage(startImage[2], -25, 0, null);
			break;
		case RUNNING:
		case PAUSE:
			sky.paintObject(g);
			if (road.state == 0) {
				road.paintObject(g);
			}
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
			//绘制暂停界面
			break;
		case GAME_OVER:
			//绘制开始界面
			break;
		}
	}
	
//	定义启动方法
	public void start() {
		//鼠标操作
		MouseAdapter l = new MouseAdapter() {
			//鼠标的点击操作
			public void mouseClicked(MouseEvent e) {
				//点击鼠标之后切换到运行态
				if (state == START) {
					state = RUNNING;
				}
				//重置游戏参数
//				score = 0;
//				sky = new Sky();
//				road = new Road();
//				hero = new Hero();
//				enemys = new FlyingObject[0];
//				bullets = new Bullet[0];
				//System.gc();
			}
		};
		//将鼠标的移动事件注册到监听器中，监听鼠标的移动事件
		//监听鼠标的移动的功能
		this.addMouseListener(l);
		//鼠标滑动事件监听
		this.addMouseMotionListener(l);
		//定义定时器
		Timer timer = new Timer();
		//定义一个变量，设定游戏运行速度（运行间隔）,间隔越小，运行速度越快，难度越大（建议范围30-50）
		int interval = 40;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (state == RUNNING) {
					//天空移动
					sky.step();
					road.step();
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
		//System.out.println(num);
		if (num<40) {
			flyingObject = new BigEnemyPlane();
		}else if (num>=40 && num <= 98) {
			flyingObject = new EnemyPlane();
		}else {
			flyingObject = new Reward();
		}
		return flyingObject;
	}
	
//	敌机入场
	int enemyIndex = 1;
	boolean newBoss = false;
	public void enemyEnterAction() {
		if(enemyIndex % 60 ==0) {
			FlyingObject flyingObject = nextEnemy();
			if (flyingObject instanceof Reward) {
				rewards = Arrays.copyOf(rewards, rewards.length+1);
				rewards[rewards.length - 1] = flyingObject;
			}else if(flyingObject instanceof BigEnemyPlane){
				if (!newBoss) {
					enemys = Arrays.copyOf(enemys, enemys.length+1);
					enemys[enemys.length - 1] = flyingObject;
					newBoss = !newBoss;
				}
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
	
	Random r = new Random();
//	碰撞测试(子弹碰撞敌机)
	public void bulletHitEnemy() {
		if (bullets.length != 0 && enemys.length != 0) {
			for(int i = 0; i < bullets.length; i++) {
				FlyingObject b = bullets[i];
				for(int j = 0; j < enemys.length; j++) {
					FlyingObject f = enemys[j];
					//System.out.println(b.isHit(f));
					if(b.isHit(f) && b.isLife() && f.isLife()) {
						b.goDead();
						if (f instanceof EnemyPlane) {
							f.goDead();
						}else if (f instanceof BigEnemyPlane) {
							f.subLife();
							if (f.isDead()) {
								newBoss = !newBoss;
								if (r.nextInt(2) == 1) {
									Reward er = new Reward();
									rewards = Arrays.copyOf(rewards, rewards.length+1);
									rewards[rewards.length - 1] = er;
								}
							}
						}
						//System.out.println(b.state);
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
					if (newFire != 0) {
						newFire --;
					}
				}
			}
		}
	}
	
//	将图片加载到内存中
	static public BufferedImage readImage(String filename) {
		//根据filename指定的文件名，将图片文件转换成Java中的图片类型对象，一般用相对路径
		try {
			BufferedImage image = ImageIO.read(FlyingObject.class.getResource(filename));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	
	
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
	if (state == RUNNING) {
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
	if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
		if (state == RUNNING) {
			state = PAUSE;
		}else if (state == PAUSE) {
			state = RUNNING;
		}
	}
}

@Override
public void keyReleased(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_DOWN ||e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode() == KeyEvent.VK_RIGHT||e.getKeyCode() == KeyEvent.VK_LEFT) {
		hero.moveTo(0, 0);
	}
}

}

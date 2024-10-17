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
	public static final int READY = 1;	//准备
	public static final int	RUNNING =2;	//进行
	public static final int	PAUSE = 3;	//暂停
	public static final int	GAME_OVER = 4;	//结束
	
//	游戏状态图片
	private static BufferedImage[] startImage;
	private static BufferedImage[] readyImages;
	private static BufferedImage[] runningImages;
	private static BufferedImage[] pauseImage;
	private static BufferedImage[] overImage;
	
	int newFire = 0;
	int score = 0;
	int indexLogo = 0;
	int chooseHero = 0;
	int chooseFire = 0;
	
	//定义游戏的当前状态
	private int state = START;
	
//	加载图片
	static {
		//加载开始前图片
		startImage = new BufferedImage[5];
		startImage[2] = readImage("./imgs/back_of_about.jpg");
		startImage[3] = readImage("./imgs/main_Btn_pressed8.png");
		startImage[4] = readImage("./imgs/main_logo.png");
		startImage[0] = readImage("./imgs/main_plane_0.png");
		startImage[1] = readImage("./imgs/main_plane_1.png");
		//加载准备阶段图片
		readyImages = new  BufferedImage[11];
		readyImages[0] = readImage("./imgs/main_bgDown.jpg");
		readyImages[1] = readImage("./imgs/qia_tishikuang.png");
		readyImages[2] = readImage("./imgs/qia_plane_sel.png");
		readyImages[3] = readImage("./imgs/qia_plane1.png");
		readyImages[4] = readImage("./imgs/qia_plane2.png");
		readyImages[5] = readImage("./imgs/qia_plane3.png");
		readyImages[6] = readImage("./imgs/bomb1_1.png");
		readyImages[7] = readImage("./imgs/bomb2_1.png");
		readyImages[8] = readImage("./imgs/bomb3_1.png");
		readyImages[9] = readImage("./imgs/zb_intro_Btn_nor1.png");
		readyImages[10] = readImage("./imgs/zb_sum_btn_nor0.png");
		//加载进行时的图片
		runningImages = new BufferedImage[5];
		runningImages[0] = readImage("./imgs/uiimg2.png");
		//加载暂停图片
		pauseImage = new BufferedImage[4];
		pauseImage[0] = readImage("./imgs/fh_backImg.png");
		pauseImage[1] = readImage("./imgs/fh_btn2.png");
		pauseImage[2] = readImage("./imgs/fh_btn1.png");
	}
	
	Sky sky = new Sky();
	Road road = new Road();
	Hero hero;
	FlyingObject[] bullets = new Bullet[] {};
	FlyingObject[] enemys = new FlyingObject[] {};
	FlyingObject[] rewards = new FlyingObject[] {};
	
//	绘制
	public void paint(Graphics g) {
		switch (state) {
		case START:
			//绘制开始界面
			g.drawImage(startImage[2], -21, -10, null);
			g.drawImage(startImage[3], 100, 400, null);
			g.drawImage(startImage[indexLogo % 2], -10, 50, null);
			g.drawImage(startImage[4], -25, 0, null);
			indexLogo ++;
			break;
		case READY:
			//绘制选角界面
			g.drawImage(readyImages[0], -21, -10, null);
			g.drawImage(readyImages[1], 0, 10, 380, 111, null);
			g.drawImage(readyImages[1], 0, 131, 380, 111, null);
			g.drawImage(readyImages[1], 0, 252, 380, 111, null);
			g.drawImage(readyImages[1], 0, 373, 380, 111, null);
			switch (chooseHero) {
			case 0:
				g.drawImage(readyImages[2], 8, 133, 74, 108,null);
				break;
			case 1:
				g.drawImage(readyImages[2], 78, 133, 74, 108,null);
				break;
			case 2:
				g.drawImage(readyImages[2], 148, 133, 74, 108,null);
				break;
			}
			g.drawImage(readyImages[3], 10, 136, 70, 100, null);
			g.drawImage(readyImages[4], 80, 136, 70, 100, null);
			g.drawImage(readyImages[5], 150, 136, 70, 100, null);
			switch (chooseFire) {
			case 0:
				g.drawImage(readyImages[2], 8, 254, 74, 108,null);
				break;
			case 1:
				g.drawImage(readyImages[2], 78, 254, 74, 108,null);
				break;
			case 2:
				g.drawImage(readyImages[2], 148, 254, 74, 108,null);
				break;
			}

			g.drawImage(readyImages[6], 38, 257, 17, 100, null);
			g.drawImage(readyImages[7], 108, 300, 17, 22, null);
			g.drawImage(readyImages[8], 178, 257, 17, 100, null);
			
			g.drawImage(readyImages[9], 59, 520, null);
			g.drawImage(readyImages[10], 200, 520, null);
			break;
		case RUNNING:
		case PAUSE:
			sky.paintObject(g);
			road.paintObject(g);
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
			g.drawImage(runningImages[0], 334, -5, null);
			//绘制暂停界面
			if (state == PAUSE) {
				g.drawImage(pauseImage[0], 40, 210, 300, 156 , null);
				g.drawImage(pauseImage[1], 50, 330, null);
				g.drawImage(pauseImage[2], 191, 330, null);
			}
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
				//获取鼠标的x轴和y轴的坐标
				int x = e.getX();
				int y = e.getY();
				//点击鼠标之后切换到运行态
				switch (state) {
				case START:
					if (isInBtn(x,y,100,400,190,190)) {
						state = READY;
					}
					break;
				case READY:
					if (isInBtn(x,y,10,136,100,70)) {
						chooseHero = 0;
					}else if (isInBtn(x,y,80,136,100,70)) {
						chooseHero = 1;
					}else if (isInBtn(x,y,150,136,100,70)) {
						chooseHero = 2;
					}else if(isInBtn(x,y,8,254,108,74)){
						chooseFire = 0;
					}else if(isInBtn(x,y,78,254,108,74)){
						chooseFire = 1;
					}else if(isInBtn(x,y,148,254,108,74)){
						chooseFire = 2;
					}else if (isInBtn(x,y,59,520,60,141)) {
						hero = new Hero(chooseHero);
						state = RUNNING;
					}else if (isInBtn(x,y,200,520,62,141)) {
						state = START;
					}
					break;
				case RUNNING:
					if (isInBtn(x,y,334,-5,67,62)) {
						state = PAUSE;
					}
					break;
				case PAUSE:
					if (isInBtn(x,y,50,330,141,60)) {
						state = RUNNING;
					}else if (isInBtn(x,y,191,330,141,60)) {
						state = START;
						//重置游戏参数
						score = 0;
						sky = new Sky();
						road = new Road();
						newFire = 0;
						score = 0;
						chooseHero = 0;
						chooseFire = 0;
						enemys = new FlyingObject[0];
						bullets = new Bullet[0];
						System.gc();
					}
					break;
				case GAME_OVER:
					
					break;
				default:
					break;
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
			Bullet[] bls = hero.shoot(fire, chooseFire);
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
	
//	自定义方法，判断是否在图片范围
	public boolean isInBtn(int ex,int ey,int bx,int by,int bHeight, int bWidth) {
		return ex > bx && ex < bx + bWidth && ey > by && ey < by + bHeight;
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

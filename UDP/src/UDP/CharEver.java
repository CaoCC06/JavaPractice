package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class CharEver {
	public static void main(String[] args) {
		new Thread(new MessgeSender()).start();
		new Thread(new MessgeReceiver()).start();
	}
}

//发送端
class MessgeSender implements Runnable{
	private final InetSocketAddress addr = new InetSocketAddress("255.255.255.255", 8090);

	@Override
	public void run() {
		try {
			//初始化套接字对象
			DatagramSocket ds = new DatagramSocket();
			//初始化Scanner
			Scanner s = new Scanner(System.in);
			//判断是否有后续输入
			while(s.hasNext()) {
				//读取数据
				byte[] data = s.nextLine().getBytes();
				//将数据封包
				DatagramPacket dp = new DatagramPacket(data, data.length, addr);
				//发送数据
				ds.send(dp);
			}
			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//接收端
class MessgeReceiver implements Runnable{

	@Override
	public void run() {
		try {
			//构建套接字对象
			DatagramSocket ds = new DatagramSocket(8090);
			//准备数据包
			DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
			//接收数据
			while (true) {
				ds.receive(dp);
				//查看发送地址
				System.out.println(dp.getAddress());
				//查看接收数据
				System.out.println(new String(dp.getData(),0,dp.getLength()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


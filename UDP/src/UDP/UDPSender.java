package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPSender {
	public static void main(String[] args) throws IOException{
		//初始化套接字对象
		DatagramSocket ds = new DatagramSocket();
		//初始化Scanner
		Scanner s = new Scanner(System.in);
		//判断是否有后续输入
		while(s.hasNext()) {
			//读取数据
			byte[] data = s.nextLine().getBytes();
			//将数据封包
			DatagramPacket dp = new DatagramPacket(data, data.length, new InetSocketAddress("localhost",8090));
			//发送数据
			ds.send(dp);
		}
		ds.close();
	}
}

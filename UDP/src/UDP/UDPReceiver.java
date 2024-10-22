package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver{
	public static void main(String[] args) throws IOException{
		//构建套接字对象
		DatagramSocket ds = new DatagramSocket(8090);
		//准备数据包
		DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
		while (true) {
			ds.receive(dp);
			//查看发送地址
			System.out.println(dp.getAddress());
			//查看接收数据
			System.out.println(new String(dp.getData(),0,dp.getLength()));
		}
	}
}

package com.cancan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String[] args) throws IOException{
		//构建套接字对象
		ServerSocket ss = new ServerSocket();
		//邦绑定监听的端口
		ss.bind(new InetSocketAddress(8088));
		//接收连接请求
		Socket s = ss.accept();
		//获取输入流
		InputStream in = s.getInputStream();
		//准备一个缓冲区存储输入流中的数据
		byte[] bs = new byte[1024];
		//记录每次读取到的字节个数
		int len;
		//读取数据
		while ((len = in.read(bs)) != 1) {
			System.out.println(new String(bs , 0 , len));
			//通知客户端数据读取完毕
			s.shutdownInput();
			//获取输出流
			OutputStream out = s.getOutputStream();
			//写出数据
			out.write("登录成功".getBytes());
			//通知客户端数据已经写完
			s.shutdownOutput();
			
			//关闭流
			s.close();
			ss.close();
		}
	}
}

package com.cancan;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	public static void main(String[] args) throws IOException{
		//构建套接字对象
		Socket s = new Socket();
		//发出链接请求
		s.connect(new InetSocketAddress("localhost",8088));
		//获取输出流
		OutputStream out = s.getOutputStream();
		//写出数据
		out.write("connect......".getBytes());
		//通知服务器以写完
		s.shutdownOutput();
		//获取输入流
		InputStream in = s.getInputStream();
		
		byte[] bs = new byte[1024];
		
		int len;
		while((len = in.read(bs)) != -1) {
			System.out.println(new String(bs , 0 ,len));

			s.shutdownInput();
			s.close();
		}
	}
}

package com.cancan;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FileUpdateClient {
	public static void main(String[] args) throws IOException{
		//构建套接字对象
		Socket s = new Socket();
		//发出链接请求
		s.connect(new InetSocketAddress("localhost",8090));
		//获取输出流
		OutputStream out = s.getOutputStream();
		//构建File对象,指向要上传的文件
		File file = new File("D:\\DownLoad\\browser");
		//获取上传的文件的名字
		byte[] fileName = file.getName().getBytes();
		//写出文件名的长度
		out.write(fileName);
		//创建输入流读取文件
		FileInputStream fin = new FileInputStream(file);
		//构建缓冲区
		byte[] bs = new byte[1024];
		int len;
		while ((len = fin.read(bs)) != -1) {
			//将读取的数据写出
			out.write(bs , 0 , len);
		}
		//通知服务器数据写完
		s.shutdownOutput();
		//获取输入流
		InputStream in = s.getInputStream();
		//读取数据
		while ((len = in.read(bs)) != -1) {
			//打印数据
			System.out.println(new String(bs , 0 ,len));
		}
		s.shutdownInput();
		s.close();
		fin.close();
	}
}

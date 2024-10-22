package com.cancan;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileUpdateServer {
	public static void main(String[] args) throws IOException{
		//构建套接字对象
		ServerSocket ss = new ServerSocket(8090);
		//接收连接请求
		Socket s = ss.accept();
		//获取输入流
		InputStream in = s.getInputStream();
		//获取文件名的长度
		int fileNameLen = in.read();
		//构建数组存放文件名
		byte[] fileName = new byte[fileNameLen];
		//读取文件名
		in.read(fileName);
		//构建文件输出流
		FileOutputStream fout = new FileOutputStream("D:\\DownLoad\\"+new String(fileName));
		//构建字节数组作为缓冲区
		byte[] bs =new byte[1024];
		int len;
		while ((len = in.read(bs)) != -1) {
			//将读取的数据写出
			fout.write(bs , 0 , len);
		}
		//通知客户端数据已经读完
		s.shutdownInput();
		//获取输入流
		OutputStream out = s.getOutputStream();
		//写出数据
		out.write("文件上传成功".getBytes());
		s.shutdownOutput();
		s.close();
		in.close();
	}
}

package com.socket.test;
/* 
  @auther : snowman
  @date : 2022年11月21日 20:26
  */

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        //超时时间
        socket.setSoTimeout(3000);

        //链接本地端，端口2000
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(),2000),3000);

        System.out.println("已发起服务器连接，并进入后续流程");

        try {
            //发送接收数据
            todo(socket);
        }catch (Exception e){
            //释放资源
            System.out.println("异常关闭");
        }
        socket.close();
        System.out.println("客户端已退出");
    }

    private static void todo(Socket client) throws IOException{
        //构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        //得到socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        //得到socket输入,并转化为BufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader =  new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        do {
            //键盘读取一行
            String  str = input.readLine();
            //发送到服务器
            socketPrintStream.println(str);


            //从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if("bye".equalsIgnoreCase(echo)){
                flag = false;
            }else {
                System.out.println(echo);
            }
        } while (true);

    }

}
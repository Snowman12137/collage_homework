package com.socket.test;
/* 
  @auther : snowman
  @date : 2022年11月21日 20:26
  */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class server {
    static int cl ;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(4000);
        System.out.println("服务器准备就绪");
        int cl = 0;

        //等待客户端链接
        Socket client = null;
        while (true) {
            //得到客户端
            client = server.accept();
            //客户端构建异步线程
            ClientHandler clientHandler= new ClientHandler(client);
            //启动线程
            clientHandler.start();
        }
        //客户端消息处理

    }
    /*
        客户端消息处理
     */
    private static class ClientHandler extends Thread{
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            super.run();
            System.out.println("新客户端链接："+socket.getInetAddress()+"P:"+socket.getLocalPort());
        try {
            cl++;
            System.out.println("目前链接的客户端数量为"+cl);
            //得到打印流，用于数据输出；服务器送回数据使用
            PrintStream socketOutout = new PrintStream(socket.getOutputStream());
            //得到输入流，用于接收数据
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            do {
                //客户端拿到一条数据
                String str = socketInput.readLine();
                if("bye".equalsIgnoreCase(str)){
                    flag = false;
                    //回送
                    socketOutout.println("bye");
                }else {
                    //打印一条数据
                    System.out.println(str);
                    //同时回送数据长度
                    socketOutout.println("回送："+str.length());
                }
            }while (flag);

            socketInput.close();
            socketOutout.close();

        }catch (Exception e){
            System.out.println("连接异常断开");
        }finally {
            //连接关闭
            try {
                socket.close();
            }catch (IOException e ){
                e.printStackTrace();
            }
        }

            System.out.println("客户端已关闭");
        }

    }

}
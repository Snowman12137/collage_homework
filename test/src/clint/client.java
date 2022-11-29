package clint;
/* 
  @auther : snowman
  @date : 2022年11月28日 17:13
  */

import example.Chat.ChatRoom_add_4_3;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class client implements Runnable{
    Socket socket = null;
    DataInputStream dis = null;
    InputStream is = null;
    String id = null;
    String sendID = null;
    String message = null;
    String targetID = null;
    public void setChat() {

    }

    client( Socket socket,  String id){
        //this.chat = chat;
        this.socket = socket;
        //this.onLineIDArea = onLineIDArea;
        this.id = id;
        try {
            is = socket.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                int length = dis.readInt();
                byte[] bytes = new byte[length];
                dis.readFully(bytes);
                String out = new String(bytes);
                int flag = 0;
                for(int i=0; i<out.length(); i++){//111*222*222
                    if(out.charAt(i)=='*' && flag==0){
                        flag = i;
                        sendID = out.substring(0, i);
                    } else if(out.charAt(i)=='*' && flag!=0){
                        message = out.substring(flag+1,i);
                        targetID = out.substring(i+1,out.length());
                    }
                }

                if(targetID.equals("DIENILNOLLAC")){
                    if(message.indexOf("|")==-1){

                    }
                    else {

                    }

                }else {
                    if (id.equals(targetID)){
                        //根据姓名找窗口
                    }
                }



            }catch (IOException e ){
                System.out.println("error in client receive message");
                break;
            }
        }
    }
}
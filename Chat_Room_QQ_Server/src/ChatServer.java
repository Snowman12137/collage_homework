/*
  @auther : snowman
  @date : 2022年11月28日 15:05
  */

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer extends JFrame {
    ArrayList<MySocket> mySockets = new ArrayList<>() ;//存放MySocket
    HashMap<String,String> record = new HashMap<String,String>();//根据id查找socket
    static JTextArea showMessageArea = new JTextArea();
    static JTextArea idArea = new JTextArea();
    static JTextArea clientCountArea = new JTextArea();
    static JTextArea idAndPasswordArea = new JTextArea();
    static String id = "";
    static String password = "";
    static final String REG = "(retsiger)";


    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.serverUI();
        server.readRecord(server);
        server.launchServer();
    }

    public void launchServer(){
        showMessageArea.setText("server is going ..." + "\r\n" + "-------------------");
        clientCountArea.setText("now number of client:   0");
        ServerSocket ss = null;
        InputStream is = null;
        DataInputStream newDis = null;
        OutputStream os = null;
        DataOutputStream dos = null;
        ChatServer server = new ChatServer();

        try {
            ss = new ServerSocket(7777);//建立socket端口监听
        } catch (IOException e) {
            System.out.println("error in link to client...");
        }
        while(true){
            clientCountArea.setText("now number of client:   " + server.mySockets.size());

            try {
                //阻塞式接受socket，然后新开启一个线程
                Socket s = ss.accept();
                is = s.getInputStream();
                newDis = new DataInputStream(is);//获取客户端的流
                int length = newDis.readInt();
                byte[] bytes = new byte[length];
                is.read(bytes);
                if(new String(bytes).indexOf(REG)!=-1){
                    register(bytes,s);
                } else{
                    getIDAndPassword(bytes);
                    os = s.getOutputStream();//服务器回写标志，1可以登陆，0不允许登陆
                    dos = new DataOutputStream(os);
                    if(judgeLogin(s)){
                        if(judgeLoginAgain(server.mySockets,s,id)){
                            MySocket myS = new MySocket(id,s);
                            server.mySockets.add(myS);
                            dos.writeInt(1);//允许登陆后，服务器回写标识
                            dos.flush();
                            new Thread(new ChatServerReader(myS,server)).start();
                            clientCountArea.setText("now number of client:   " + server.mySockets.size());
                            idArea.append("client id :"+id+"   is linking....\r\n");
                            id = "";
                            password = "";
                        } else{
                            System.out.println(id + " 重复登陆...");
                            dos.writeInt(2);
                            dos.flush();
                            s.close();
                        }
                    } else{
                        dos.writeInt(0);
                        dos.flush();
                        s.close();
                    }
                }
            } catch (IOException e) {
                System.out.println("error in server socket...");
            }
        }
    }

    private boolean judgeLogin(Socket s) {

    }

    private void register(byte[] bytes, Socket s) {
    }

    static class ChatServerReader implements Runnable{

        InputStream is = null;
        OutputStream os = null;
        DataInputStream dis;
        DataOutputStream dos;
        ChatServer server = null;
        MySocket myS = null;
        String sendID = null;
        String message = null;
        String targetID = null;

        /**
         * 获取当前连接端口的输入输出流
         * @param
         */
        ChatServerReader(MySocket myS , ChatServer server){
            try {
                this.myS = myS;
                is = myS.getSocket().getInputStream();
                dis = new DataInputStream(is);
                this.server = server;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            while(true){
                try {
                    int length = dis.readInt();
                    byte[] bytes = new byte[length];
                    is.read(bytes);
                    String out = new String(bytes);
                    int flag = 0;
                    for(int i=0; i<out.length(); i++){
                        if(out.charAt(i)=='*' && flag==0){
                            flag = i;
                            sendID = out.substring(0, i);
                        } else if(out.charAt(i)=='*' && flag!=0){
                            message = out.substring(flag+1,i);
                            targetID = out.substring(i+1,out.length());
                        }
                    }
                    //System.out.println(sendID+" "+message+" "+targetID);

                    for(int i=0; i<server.mySockets.size(); i++){
                        MySocket myNewS = server.mySockets.get(i);
                        if(myNewS == this.myS){
                            showMessageArea.append("\r\n"+"CLIENT ID:   " +server.mySockets.get(i).getId() );
                            showMessageArea.append("\r\nmessage:   " +message);
                        }

                        if(targetID.equals("TNEILCLLA") ){//请求群发
                            os = myNewS.getSocket().getOutputStream();
                            dos = new DataOutputStream(os);
                            dos.writeInt(out.getBytes().length);
                            os.write(out.getBytes());
                            os.flush();
                        } else if(targetID.equals("DIENILNOLLAC")){//请求刷新在线id
                            if(myNewS == this.myS){
                                String ONLINEID = getONLINEID(server.mySockets,this.myS);
                                os = myNewS.getSocket().getOutputStream();
                                dos = new DataOutputStream(os);
                                String newMessage = sendID+"*"+ONLINEID+"*"+targetID;
                                dos.writeInt(newMessage.getBytes().length);//传都传字节数组的长度
                                os.write(newMessage.getBytes());
                                os.flush();
                            }
                        } else{//请求私聊
                            //else if(!targetID.equals("TNEILCLLA") && !targetID.equals("DIENILNOLLAC") && !targetID.equals("DNEIRFDDALLAC"))
                            if(myNewS.getId().equals(targetID) || myNewS.getId().equals(sendID)){
                                os = myNewS.getSocket().getOutputStream();
                                dos = new DataOutputStream(os);
                                dos.writeInt(out.getBytes().length);
                                os.write(out.getBytes());
                                os.flush();
                            }
                        }
                    }

                } catch (IOException e) {
                    String idList = getONLINEID(server.mySockets, myS);
                    updateIDThen(server.mySockets, myS, os, dos, idList.substring(idList.indexOf("|")+1, idList.length()));
                    server.mySockets.remove(myS);
                    idArea.append("client id :"+myS.getId()+"   is leaving....\r\n ");
                    clientCountArea.setText("now number of client:   " + server.mySockets.size());
                    break;
                }
            }
        }
    }

    private static void updateIDThen(ArrayList<MySocket> mySockets, MySocket myS, OutputStream os, DataOutputStream dos, String ONLINEID){
        //把新的id信息写回到其余客户端中
        try {
            for(int i=0; i<mySockets.size(); i++){
                MySocket myNewS = mySockets.get(i);
                if(myNewS!=myS){
                    os = myNewS.getSocket().getOutputStream();
                    dos = new DataOutputStream(os);
                    String newMessage = "DIENILNOHSULF" + "*" + ONLINEID + "*" + "DIENILNOHSULF";
                    dos.writeInt(newMessage.getBytes().length);
                    os.write(newMessage.getBytes());
                    os.flush();
                }
            }
        }  catch (IOException e1) {
            System.out.println("error is return onlineID ...");
        }
    }


    private static String getONLINEID(ArrayList<MySocket> mySockets, MySocket myS){
        String ONLINEID = myS.getId();
        for(int j=0;j<mySockets.size();j++){
            if(!mySockets.get(j).getId().equals(myS.getId())){
                ONLINEID += "|"+mySockets.get(j).getId();
            }
        }
        return ONLINEID;
    }

    private void getIDAndPassword(byte[] bytes) {
    }

    private boolean judgeLoginAgain(ArrayList<MySocket> mySockets, Socket s, String id) throws IOException{
        for(int i=0; i<mySockets.size(); i++){
            if(id.equals(mySockets.get(i).getId()))
                return false;
        }
        return true;
    }

    public void readRecord(ChatServer server) {
        byte[] fileRecord = null;
        File file = null;
        FileInputStream fis = null;
        try {
            file = new File("src\\record.txt");
            fis = new FileInputStream(file);
            fileRecord = new byte[(int) file.length()];
            fis.read(fileRecord);
        } catch (IOException e) {
            System.out.println("error in load record ...");
        }
        String[] recordStr = new String(fileRecord).split("\\*\\|\\*");
        for(int i=0; i<recordStr.length; i++){
            String[] iap = recordStr[i].split("-");
            record.put(iap[0],iap[1]);
            server.idAndPasswordArea.append("\r\n" + iap[0] + " ---------- " + iap[1]);
        }
        try {
            fis.close();
        } catch (IOException e) {
            System.out.println("error in close readfile ...");
        }
    }

    private void serverUI(){
        this.setTitle("Server");
        this.setSize(400,700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setLayout(null);//把默认的流式布局取消
        //设置不可编辑
        clientCountArea.setEditable(false);
        clientCountArea.setBounds(5,5,380,35);
        idArea.setEnabled(false);
        JScrollPane idPane = new JScrollPane(idArea);
        idPane.setBounds(5, 50, 380, 90);
        showMessageArea.setEnabled(false);
        showMessageArea.setVerifyInputWhenFocusTarget(true);
        JScrollPane showMessagePane = new JScrollPane(showMessageArea);
        showMessagePane.setBounds(5,150,380,400);
        idAndPasswordArea.setEnabled(false);
        JScrollPane idAndPassPane = new JScrollPane(idAndPasswordArea);
        idAndPassPane.setBounds(5,560,380,100);
        idAndPasswordArea.setText(" id ----------  password");//10个-

        this.add(clientCountArea);
        this.add(idPane);
        this.add(showMessagePane);
        this.add(idAndPassPane);
        this.setVisible(true);
    }








}
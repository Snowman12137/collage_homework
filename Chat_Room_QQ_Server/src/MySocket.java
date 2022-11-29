/*
  @auther : snowman
  @date : 2022��11��28�� 15:04
  */

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MySocket {
    private Socket socket = null;//һ���½���Ͳ������ٸı�
    private String id = "";//�������ٸı���
    private ArrayList<String> friends = new ArrayList<String>();//��ź���id
    private Set<String> friendsCopy = new HashSet<String>(); // ������֤�Ƿ�����ظ�id�������ظ���Ӻ���
    private File file = new File("src\\"+id+".txt");//Ӧ��Ϊÿһ���û�������һ���ļ�����ſͻ���ר������Ϣ

    //ֻ�������
    public Socket getSocket() {
        return socket;
    }

    public String getId() {
        return id;
    }

    public void changeFriends(String friend,boolean addOrDel){
        if(addOrDel){
            if(!friendsCopy.contains(friend))//��������ڣ������
                friends.add(friend);//Ӧ��ʹ��hashMap��ʹ�õ������������η���
            friendsCopy.add(friend);
        } else {
            friendsCopy.remove(friend);
            friends.remove(friend);
        }
    }

    public ArrayList<String> getFriends(){
        return friends;
    }

    MySocket(String id,Socket socket){
        this.id = id;
        this.socket = socket;
    }
}
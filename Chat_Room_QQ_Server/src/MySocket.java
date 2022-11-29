/*
  @auther : snowman
  @date : 2022年11月28日 15:04
  */

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MySocket {
    private Socket socket = null;//一旦新建后就不允许再改变
    private String id = "";//不可以再改变了
    private ArrayList<String> friends = new ArrayList<String>();//存放好友id
    private Set<String> friendsCopy = new HashSet<String>(); // 用来验证是否存在重复id，避免重复添加好友
    private File file = new File("src\\"+id+".txt");//应该为每一个用户都创建一个文件来存放客户端专属的信息

    //只允许访问
    public Socket getSocket() {
        return socket;
    }

    public String getId() {
        return id;
    }

    public void changeFriends(String friend,boolean addOrDel){
        if(addOrDel){
            if(!friendsCopy.contains(friend))//如果不存在，就添加
                friends.add(friend);//应该使用hashMap，使用迭代器进行依次访问
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
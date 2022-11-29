/*	�汾4.3
 *	���ݿͻ��˷��͵��������ж��Ƿ��Ǻ��ѣ�д�������������Ӧ
 *	���Ǻ��ѿ�������Ӻ��Ѳ��洢��д����Ӧ���
 *	�����������ͨ�����ǲ�ͨ����д����Ӧ���
 *	
 *	ָ��ͨ��Э�飺
 *	DIENILNOLLAC  �����ȡ����id
 *	TNEILCLLAC	����Ϣ���͸�������
 *	NOITACILPPALLAC		�������
 *	DIENILNOHSULF	ˢ������id
 *	DNESREGNARTS	İ���˷���Ϣ
 *	�յ���Ϣ�����Զ������������ͻ��˿�������
 */

package example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChatServer extends JFrame{
	public StringWriter idAndPasswordArea = new StringWriter();
	ArrayList<MySocket> mySockets = new ArrayList<MySocket>();//���MySocket
	HashMap<String,String> record = new HashMap<String,String>();//����id����socket
	static JTextArea showMessageArea = new JTextArea();
	static JTextArea idArea = new JTextArea();
	static JTextArea clientCountArea = new JTextArea();
	//static JTextArea idAndPasswordArea = new JTextArea();
	static String id = "";
	static String password = "";
	static final String REG = "(retsiger)";
	
	
	public static void main(String[] args){
		ChatServer server = new ChatServer();
		server.serverUI();
		server.readRecord(server);
		server.launchServer();
	}

	
	/**
	 * �����������ʵ������
	 */
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
			ss = new ServerSocket(7777);//����socket�˿ڼ���
		} catch (IOException e) {
			System.out.println("error in link to client...");
		}
		while(true){			
			clientCountArea.setText("now number of client:   " + server.mySockets.size());
			
			try {
				//����ʽ����socket��Ȼ���¿���һ���߳�
				Socket s = ss.accept();
				is = s.getInputStream();
				newDis = new DataInputStream(is);//��ȡ�ͻ��˵���
				int length = newDis.readInt();
				byte[] bytes = new byte[length];
				is.read(bytes);
				if(new String(bytes).indexOf(REG)!=-1){
					register(bytes,s);
				} else{
					getIDAndPassword(bytes);
					os = s.getOutputStream();//��������д��־��1���Ե�½��0�������½
					dos = new DataOutputStream(os);
					if(judgeLogin(s)){
						if(judgeLoginAgain(server.mySockets,s,id)){
							MySocket myS = new MySocket(id,s);
							server.mySockets.add(myS);
							dos.writeInt(1);//�����½�󣬷�������д��ʶ
							dos.flush();
							updateGlobalID(server.mySockets, myS);//�����пͻ��˷���id�б���Ϣ
							//��Ӧ�������пͻ��˶������Լ��ĺ����б�id,   ����ʵ��
							updateFriendID( myS);
							new Thread(new ChatServerReader(myS,server)).start();
							clientCountArea.setText("now number of client:   " + server.mySockets.size());
							idArea.append("client id :"+id+"   is linking....\r\n");
							id = "";
							password = "";
						} else{
							System.out.println(id + " �ظ���½...");
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
	
	private void writeRecord(String id,String password){
		try {
			File file = new File("src\\record.txt");
			FileOutputStream fos = new FileOutputStream(file,true);//true������Դ�ļ���׷��
			String appendRecord = "*|*"+id+"-"+password;
			fos.write(appendRecord.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			System.out.println("error in write record ...");
		}
	}
	
	/*
	 * ˢ�����пͻ��˵�online id �б�
	 */
	private void updateGlobalID(ArrayList<MySocket> mySockets, MySocket myS) throws IOException{
		String temporary = getONLINEID(mySockets, myS);
		OutputStream os = null;
		DataOutputStream dos = null;
		for(int i=0 ; i<mySockets.size(); i++){
			MySocket myNewS = mySockets.get(i);
			os = myNewS.getSocket().getOutputStream();//��Ϊ�Ƕ��̣߳����ԣ������ܾ�ֱ�Ӷ���һ������ͳһʹ�õı����ɣ�����
			dos = new DataOutputStream(os);
			String out = myS.getId()+"*"+temporary+"*"+"DIENILNOHSULF";
			dos.writeInt(out.getBytes().length);
			os.write(out.getBytes());
			os.flush();
		}
	}
	
	/*
	 * Ӧ����ֻ��Ե��÷�����socket���͸ÿͻ��˵����к���id
	 */
	private void updateFriendID(MySocket myS) throws IOException{
		String temporary = "";
		OutputStream os = null;
		DataOutputStream dos = null;
		temporary = getFriendsID(myS);//��ȡ����socket�Լ�ר����friendsID�б�
		System.out.println("socket id:"+myS.getId()+" friends id: "+temporary);
		os = myS.getSocket().getOutputStream();
		dos = new DataOutputStream(os);
		String out = myS.getId()+"*"+temporary+"*"+"DISDNEIRFHSULF";
		dos.writeInt(out.getBytes().length);
		os.write(out.getBytes());
		os.flush();
	}
	
	private String getFriendsID(MySocket myS){
		String friends = myS.getId();
		for(int j=0;j<myS.getFriends().size();j++){
			friends += "|"+myS.getFriends().get(j);//ÿһ��socket�а�����ֻ�к���id��û���Լ���id
		}
		return friends;
	}
	
	private void getIDAndPassword(byte[] bytes){
		for(int i=0; i<bytes.length; i++){
			if(bytes[i]=='*'){
				id = new String(bytes).substring(0, i);
				password = new String(bytes).substring(i+1, bytes.length);
				break;
			}
		}
	}
	
	private boolean judgeLogin(Socket s) throws IOException{
		if(record.get(id)!=null)
			return true; 
		return false;
	}
	
	private boolean judgeLoginAgain(ArrayList<MySocket> mySockets,Socket s,String id) throws IOException{
		for(int i=0; i<mySockets.size(); i++){
			if(id.equals(mySockets.get(i).getId()))
				return false;
		}
		return true;
	}
	
	private void register(byte[] bytes,Socket s) throws IOException{
		for(int i=0; i<bytes.length-REG.length(); i++){//(rersiger)
			if(bytes[i]=='*'){
				id = new String(bytes).substring(0, i);
				password = new String(bytes).substring(i+1, bytes.length-REG.length());
				record.put(id, password);
				idAndPasswordArea.append("\r\n" + id + " ---------- " + password);
				writeRecord(id,password);
				break;
			}
		}
		s.close();
	}
	
	/**
	 * Ϊʲôʵ���˽ӿں���д�ĳ��󷽷����붨���public��
	 * ����ͻ������Ӻ�Ĳ���д��run������ 
	 * @author Suagr
	 *
	 */
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
		 * ��ȡ��ǰ���Ӷ˿ڵ����������
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
						
						if(targetID.equals("TNEILCLLA") ){//����Ⱥ��
							os = myNewS.getSocket().getOutputStream();
							dos = new DataOutputStream(os);
							dos.writeInt(out.getBytes().length);
							os.write(out.getBytes());
							os.flush();
						} else if(targetID.equals("DIENILNOLLAC")){//����ˢ������id
							if(myNewS == this.myS){
								String ONLINEID = getONLINEID(server.mySockets,this.myS);
								os = myNewS.getSocket().getOutputStream();
								dos = new DataOutputStream(os);
								String newMessage = sendID+"*"+ONLINEID+"*"+targetID;
								dos.writeInt(newMessage.getBytes().length);//�������ֽ�����ĳ���
								os.write(newMessage.getBytes());
								os.flush();		
							}
						} else if(targetID.equals("DNEIRFDDALLAC")){//������Ӻ��ѣ�Ŀ��ID��message����
							String addTargetID = message;
							if(myNewS.getId().equals(addTargetID)){
								os = myNewS.getSocket().getOutputStream();
								dos = new DataOutputStream(os);
								String newMessage = sendID+"*"+"DNEIRFDDALLAC"+"*"+"DNEIRFDDALLAC";
								dos.writeInt(newMessage.getBytes().length);//�������ֽ�����ĳ���
								os.write(newMessage.getBytes());
								os.flush();
							}
						} else if(targetID.equals("DNEIRFDDALLACYLPER")){
							String[] reply = message.split("\\|");
							System.out.println(message+" get add friend application ");
							if(myNewS.getId().equals(reply[1])){
								if(reply[0].equals("SEY")){//������Ϻ���
									myS.changeFriends(reply[1], true);
									myNewS.changeFriends(myS.getId(), true);
									//���ͬ����Ӻ��ѣ���Ӧ��ˢ��˫���ĺ����б�  
									System.out.println("add succeed ");
									server.updateFriendID(myNewS);
									server.updateFriendID(myS);
								}
								os = myNewS.getSocket().getOutputStream();
								dos = new DataOutputStream(os);
								String newMessage = sendID+"*"+reply[0]+"*"+"DNEIRFDDALLACYLPER";
								dos.writeInt(newMessage.getBytes().length);//�������ֽ�����ĳ���
								os.write(newMessage.getBytes());
								os.flush();
							}
						} else if(targetID.equals("DISDNEIRFHSULF")){ //��������ˢ�º���id�б�
							server.updateFriendID(myS);
						} else{//����˽��
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
	
	private static String getONLINEID(ArrayList<MySocket> mySockets,MySocket myS){
		String ONLINEID = myS.getId();
		for(int j=0;j<mySockets.size();j++){
			if(!mySockets.get(j).getId().equals(myS.getId())){
				ONLINEID += "|"+mySockets.get(j).getId();
			}
		}
		return ONLINEID;
	}
	
	private static void updateIDThen(ArrayList<MySocket> mySockets, MySocket myS, OutputStream os,DataOutputStream dos,String ONLINEID){
		//���µ�id��Ϣд�ص�����ͻ�����
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
	
	public void serverUI(){
		this.setTitle("Server");
		this.setSize(400,700);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(3);
		this.setLayout(null);//��Ĭ�ϵ���ʽ����ȡ��
		//���ò��ɱ༭
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
		idAndPasswordArea.setText(" id ----------  password");//10��-
		
		this.add(clientCountArea);
		this.add(idPane);
		this.add(showMessagePane);
		this.add(idAndPassPane);
		this.setVisible(true);
	}
}

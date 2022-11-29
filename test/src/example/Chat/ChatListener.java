package example.Chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ChatListener implements ActionListener{
	ChatRoom_add_4_3 chat = null;
	Socket socket = null;
	JLabel idLabel = null;
	RMessage r = null;
	OutputStream os = null;
	DataOutputStream dos = null;
	InputStream is = null;
	DataInputStream dis = null;
	private String id = "";
	private char[] password = null;
	static String targetID = null;
	static final String REG = "(retsiger)";
	
	ChatListener(ChatRoom_add_4_3 chat,JLabel idLabel){
		this.chat = chat;
		this.idLabel = idLabel;
	}
	
	public String getID(){
		return id;
	}
	
	public void actionPerformed(ActionEvent e){//��Ҫ��ǰ�ֶ�����������
		if(e.getActionCommand().equals("ע��")){
			chat.warn.setText("");
			chat.launchFrame.setVisible(false);
			chat.registerFrame();
		} else if(e.getActionCommand().equals("ȷ��")){
			id = chat.regText.getText();
			password = chat.regPassword.getPassword();
			startSocket();
			try{
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				dos.writeInt(id.length()+password.length+REG.length()+1);
				os.write((id+"*"+new String(password)+REG).getBytes());
				os.flush();
			} catch(IOException error){
				System.out.println("error in register...");
			}
			id = "";
			password = null;
			chat.loginText.setText("");
			chat.loginPassword.setText("");
			chat.regFrame.setVisible(false);
			chat.launchFrame.setVisible(true);

		} else if(e.getActionCommand().equals("��½")){//��½֮��id�����붼�б���
			id = chat.loginText.getText();//��ȡ��½��id
			password = chat.loginPassword.getPassword();//��ȡ��½��id
			if(id.length()==0){
				chat.warn.setText("�˺Ų���Ϊ��");
				return ;
			}
			if(password.length==0){
				chat.warn.setText("���벻��Ϊ��");
				return ;
			}
			//�����Ƿ�������Ϸ�����������Ҫ�Ƚ�������
			startSocket();
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				dos.writeInt(id.length()+1+password.length);
				os.write(new String(id+"*"+new String(password)).getBytes());//��id������ ת��Ϊbyte���鴫��ȥ
				os.flush();
				is = socket.getInputStream();
				dis = new DataInputStream(is);
				int mark = dis.readInt();
				if(mark==1){//�����½֮��Ĳ���					
					chat.idLabel.setText("ID : " + id);
					chat.launchFrame.setVisible(false);
					chat.loginFrame();
					showMessage();
				} 
				else if(mark==2){
					chat.warn.setText("�˺��ѵ�½");
				} else if(mark==0){
					chat.warn.setText("�˺����벻����");
				}
				
			} catch (IOException e1) {
				System.out.println("error in login...");
			}
			
		} else if(e.getActionCommand().equals("����Ⱥ����Ϣ")){//������;Ϳ�ʼ������д������
			if(targetID==null)
				targetID = "TNEILCLLA";
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				String out = new String(id+"*"+chat.sendGroupMessageArea.getText()+"*"+targetID);
				if(out.length()!=0){
					dos.writeInt(out.getBytes().length);
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in send group Message...");
			}
			chat.sendGroupMessageArea.setText(null);
			targetID = null;
		} else if(e.getActionCommand().equals("����˽����Ϣ")){
			targetID = chat.friendListListen.getTargetID();
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				String out = new String(id+"*"+chat.sendPersonalMessageArea.getText()+"*"+targetID);
				if(out.length()!=0){
					dos.writeInt(out.getBytes().length);
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in send personal Message...");
			}
			chat.sendPersonalMessageArea.setText(null);
			targetID = null;
		} else if(e.getActionCommand().equals("ˢ��ID�б�")){
			chat.onLineIDArea.setText("");
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				//String out = new String(id+"*"+"DIENILNOLLAC"+"*"+"DIENILNOLLAC");
				String out = new String(id+"*"+"DISDNEIRFHSULF"+"*"+"DISDNEIRFHSULF");
				if(out.length()!=0){
					dos.writeInt(out.length());
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in get onine id...");
			}
		} else if(e.getActionCommand().equals("Ⱥ��")){
			chat.startGroupChat();
		} else if(e.getActionCommand().equals("��Ӻ���")){
			chat.addFriend();
		} else if(e.getActionCommand().equals("���")){
			String addTargetID = (String) chat.onlineList.getSelectedValue();//��ȡѡ�е�id
			if(addTargetID.equals(id))
				return ;
			//��ӵ�������ʾ�����ѷ���
			JOptionPane.showMessageDialog(null, "�����ѷ���");
			chat.addFriendFrame.setVisible(false);
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				String out = new String(id+"*"+addTargetID+"*"+"DNEIRFDDALLAC");
				if(out.length()!=0){
					dos.writeInt(out.length());
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in add friend...");
			}
		} else if(e.getActionCommand().equals("ȷ�����")){
			chat.applyFriendFrame.setVisible(false);
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				String out = new String(id+"*"+"SEY|"+chat.applyID+"*"+"DNEIRFDDALLACYLPER");
				if(out.length()!=0){
					dos.writeInt(out.length());
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in reply apply YES...");
			}
		} else if(e.getActionCommand().equals("�ܾ����")){
			chat.applyFriendFrame.setVisible(false);
			try {
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				String out = new String(id+"*"+"TSUJER|"+chat.applyID+"*"+"DNEIRFDDALLACYLPER");
				if(out.length()!=0){
					dos.writeInt(out.length());
					os.write(out.getBytes());
					os.flush();
				}
			} catch (IOException e1) {
				System.out.println("error in reply apply NO...");
			}
		}
	}
	
	private void startSocket(){
		try {
			socket = new Socket("127.0.0.1",7777);
		} catch (IOException e) {
			System.out.println("error in socket...");
		}
	}
	
	/**
	 * ����������Ϣ���߳�
	 */
	private void showMessage(){
		r = new RMessage(chat,socket,chat.onLineIDArea,id);
		Thread t = new Thread(r);
		t.start();
	}
	
}

/**

 */
package example.Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * ������Ϣ���߳���
 * @author Suagr
 *
 */
public class RMessage implements Runnable{
	ChatRoom_add_4_3 chat = null;
	Socket socket = null;
	DataInputStream dis = null;
	InputStream is = null;
	JTextArea onLineIDArea = null;
	String id = null;
	String sendID = null;
	String message = null;
	String targetID = null;

	public void setChat() {
		
	}
	
	RMessage(ChatRoom_add_4_3 chat,Socket socket,JTextArea onLineIDArea,String id){
		this.chat = chat;
		this.socket = socket;
		this.onLineIDArea = onLineIDArea;
		this.id = id;
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ص��Ǵ�����յ�����Ϣ���ѽ��յ����ַ���д��JTextField�ı�����
	 */
	public void run(){
		while(true){
			try{
				int length = dis.readInt();
				byte[] bytes = new byte[length];
				dis.readFully(bytes);//����ʽ
				//��ȡ id message targetID
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
				
				if(targetID.equals("DIENILNOLLAC") || targetID.equals("DIENILNOHSULF")){//����/����ˢ��online id�б�
					if(targetID==null)
						System.out.println("target is null...");
					if(message.indexOf("|")==-1){
						chat.globalID.clear();
						chat.globalID.add(message);
					}
					else {
						chat.globalID.clear();
						String onlineIDS[] = message.split("\\|");
						for(int i=0;i<onlineIDS.length;i++){
							chat.globalID.add(onlineIDS[i]);
						}
					}
					chat.friendList.updateUI();
					chat.onlineList.updateUI();
				
				} else if(targetID.equals("DISDNEIRFHSULF")){// ����/����ˢ��online id�б�
					System.out.println("my id :"+id + "  receive friends id :"+message);// 
					if(targetID==null)
						System.out.println("target is null...");
					if(message.length()==0)
						System.out.println(out);//����ʹ��
					if(message.indexOf("|")==-1){
						chat.friendsID.clear();
						chat.friendsID.add(message);
					}
					else {
						chat.friendsID.clear();
						String friendsIDS[] = message.split("\\|");//�����б�ĵ�һ���϶����Լ�
						for(int i=0;i<friendsIDS.length;i++){
							chat.friendsID.add(friendsIDS[i]);
						}
						System.out.println(friendsIDS.length);
					}
					chat.friendList.updateUI();
					chat.onlineList.updateUI();
					
				} else if(targetID.equals("DNEIRFDDALLAC")){
					chat.applyID = sendID;//�洢�����ߵ�ID
					chat.applyLabel.setText("����ID: "+sendID+" �ĺ�������");
					chat.applyFriend();
				} else if(targetID.equals("DNEIRFDDALLACYLPER")){
					String replyResult = message;
					if(replyResult.equals("SEY")){
						JOptionPane.showMessageDialog(null, "ID: "+sendID+" ����������ͨ��");
					}else if(replyResult.equals("TSUJER")){
						JOptionPane.showMessageDialog(null, "ID: "+sendID+" ��������δͨ��");
					}
				}else if(id.equals(sendID) ){//�ͻ����Լ������Լ���
					//&& !targetID.equals("DIENILNOLLAC") && !targetID.equals("DIENILNOHSULF") && !targetID.equals("DNEIRFDDALLAC")
					if( !targetID.equals("TNEILCLLA"))
						chat.showPersonalMessageArea.append("\r\n" + message );
					else
						chat.showGroupMessageArea.append("\r\n" + message );
				} else if(targetID.equals("TNEILCLLA") ){//Ⱥ��ʱ�ǿͻ����Լ�
					//&& !id.equals(sendID) &&  !targetID.equals("DIENILNOHSULF") && !targetID.equals("DNEIRFDDALLAC")
					chat.showGroupMessageArea.append("\r\n"+sendID+" : " + message );
				} else{//˽��ʱ�ǿͻ����Լ�
					//(!id.equals(sendID) && !targetID.equals("TNEILCLLA") && !targetID.equals("DIENILNOHSULF") && !targetID.equals("DNEIRFDDALLAC"))
					chat.showPersonalMessageArea.append("\r\n [ from"+sendID+" ]: " + message );
				} 
			} catch(IOException e){
				System.out.println("error in client receive message...");
				break;
			}
		}	
	}
	
}

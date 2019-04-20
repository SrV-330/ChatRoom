package chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.smartcardio.CommandAPDU;

import chat.server.entity.Command;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.FriendInfo;
import chat.server.entity.LoginInfo;
import chat.server.entity.RegisterInfo;
import chat.server.entity.SendMessageInfo;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserGroupModifyInfo;
import chat.server.entity.UserInfo;
import chat.server.util.DBHelper;
import chat.server.util.json.JsonParser;

public class ChatServer {
	
	private List<Socket> sockets=new ArrayList<>();
	private HashMap<UserInfo,Socket> users=new HashMap<>();
	private ServerSocket server;
	private ThreadPoolExecutor executor=new ThreadPoolExecutor(16, 64, 5, 
			TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024));
	
	
	private static ChatServer chatServer;
	private JsonParser jp;
	public static ChatServer getInstance(){
		
		
		if(chatServer==null){
			synchronized (ChatServer.class) {
				if(chatServer==null){
					chatServer=new ChatServer();
				}
			}
		}
		
		return chatServer;
	}
	private ChatServer(){
		init();
	}
	
	private void init(){
		
		try {
			System.out.println("server starup");
			server=new ServerSocket(8088);
			jp=new JsonParser();
			System.out.println("server lauch ");
		} catch (Exception e) {
			
			e.printStackTrace();
			try {
				if(server!=null){
					server.close();
				}
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
	}
	
	private void start(){
		Socket socket=null;
		while(true){
			
			try {
				 socket= server.accept();
				
				
				
				executor.execute(new ClientHandler(socket));
				
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
				try {
					if(socket!=null){
						
						socket.close();
					}
					
				} catch (IOException e1) {
				
					e.printStackTrace();
				}
			}
			finally{
				
			}
			
		}
			
		
		
		
	}
	
	class ClientHandler implements Runnable{

		private Socket socket;
		
		private InputStream in;
		private OutputStream out;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		public  ClientHandler(Socket socket) {
			
			
			try {
				this.socket=socket;
				this.in=this.socket.getInputStream();
				this.out=this.socket.getOutputStream();
				ois=new ObjectInputStream(in);
				oos=new ObjectOutputStream(out);
	
			} catch (IOException e) {
				
				e.printStackTrace(); 
			}
			
		}
		
		
		


		@Override
		public void run() {
			
			try {
				synchronized (sockets) {
					sockets.add(socket);
				}
				
				
				
				
				Command cmd=(Command)ois.readObject();
				
				
				
				switch (cmd.getCmd()) {
				case Command.LOGIN:
					doLogin(cmd);
					break;
				case Command.REGIST:
					doRegist(cmd);
					break;
				case Command.ADD_FRIEND:
					doAddFriend(cmd);
					break;
				case Command.REMOVE_FRIEND:
					doRemoveFriend(cmd);
					break;
				case Command.SEND_MSG:
					doSendMessage(cmd);
					break;
				case Command.NEW_GROUP:
					doNewGroup(cmd);
					break;
				case Command.REMOVE_GROUP:
					doDeleteGroup(cmd);
					break;
				case Command.MODIFY_GROUP_NAME:
					doModifyGroupName(cmd);
					break;
				case Command.MOVE_TO_GROUP:
					doMoveToGroup(cmd);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					out.write(Command.FAIL);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}finally{
				try {
					if(ois!=null)
						ois.close();
					
					if(out!=null)
						out.close();
					
					if(in!=null)
						in.close();
					synchronized (sockets) {
						synchronized (users) {
							sockets.remove(socket);
							if(users.containsValue(socket)){
								users.remove(socket);
							}
						}
						
						
					}
					
					
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
			
			
		}
		private void doRemoveFriend(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof FriendInfo){
				FriendInfo f=(FriendInfo)cmd;
				if(DBHelper.getUser(f.getFriendName())==null){
					c.setCmd(UserInfo.NO_THE_USER);
					oos.writeObject(c);
					return;
				}
				if(DBHelper.addFriend(f)){
					c.setCmd(Command.SUCCESS);
					oos.writeObject(c);
					return;
				}
			}
			c.setCmd(Command.FAIL);
			oos.writeObject(c);
			
			
		}





		private void doAddFriend(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof FriendInfo){
				FriendInfo f=(FriendInfo)cmd;
				if(DBHelper.getUser(f.getFriendName())==null){
					c.setCmd(UserInfo.NO_THE_USER);
					oos.writeObject(c);
					return;
				}
				if(DBHelper.addFriend(f)){
					f.setGroupName("Default");
					String t=f.getUserName();
					f.setUserName(f.getFriendName());
					f.setFriendName(t);
					
					if(DBHelper.addFriend(f)){
						
					
						c.setCmd(UserInfo.SUCCESS);
						oos.writeObject(c);
						return;
					}
				}
			}
			c.setCmd(UserInfo.FAIL);
			oos.writeObject(c);
			
		}





		private void doSendMessage(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof SendMessageInfo){
				SendMessageInfo msg=(SendMessageInfo)cmd;
				UserInfo sendUser=DBHelper.getUser(msg.getSendUserName());
				UserInfo receiveUser=DBHelper.getUser(msg.getReceiveUserName());
				if(DBHelper.getFriend(new FriendInfo(sendUser.getUserName(),
						null,receiveUser.getUserName()))==null){
					c.setCmd(Command.NOT_FRIEND);
					oos.writeObject(c);
					return;
				}
				synchronized(users){
					if(users.containsKey(sendUser)&&users.containsKey(receiveUser)){
						Socket socket=users.get(receiveUser);
						ObjectOutputStream rcv=
								new ObjectOutputStream(socket.getOutputStream());
						msg.setCmd(Command.RECEIVE_MSG);
						rcv.writeObject(msg);
						c.setCmd(Command.SUCCESS);
						oos.writeObject(c);
						return;
					}
				}
				c.setCmd(Command.NOT_ON_LINE);
				oos.writeObject(c);
				return;
			}
			c.setCmd(Command.FAIL);
			oos.writeObject(c);
			
		}





		private void doRegist(Command cmd) throws IOException {
			Command c=new Command();
			
			if(cmd instanceof UserInfo){
				UserInfo registerInfo=(UserInfo)cmd;
				if(DBHelper.getUser(registerInfo.getUserName())!=null){
					c.setCmd(RegisterInfo.EXISTS_USER);
					oos.writeObject(c);
					
					return;
				}
				if(DBHelper.addUser(registerInfo)){
					
					UserGroupInfo userGroupInfo=new UserGroupInfo(registerInfo.getUserName(), "Default");
					if(DBHelper.addUserGroup(userGroupInfo)){
						c.setCmd(Command.SUCCESS);
						oos.writeObject(c);
						return;
					}
					
					
					
				}
				
			}
			c.setCmd(Command.FAIL);
			oos.writeObject(c);
		}





		private void doMoveToGroup(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof UserGroupModifyInfo){
				UserGroupModifyInfo ugmi=(UserGroupModifyInfo)cmd;
				if(ugmi.getSrc().equals(ugmi.getTar())){
					c.setCmd(UserGroupInfo.GROUP_REPEATE);
					oos.writeObject(c);
					return;
				}
				if(DBHelper.getGroup(ugmi.getTar())==null){
					c.setCmd(UserGroupInfo.NO_THE_GROUP);
					oos.writeObject(c);
					return;
				}
				UserGroupInfo src=DBHelper.getGroup(ugmi.getSrc());
				UserGroupInfo tar=new UserGroupInfo();
				if(DBHelper.moveToGroup(src,tar)){
					c.setCmd(UserGroupInfo.SUCCESS);
					oos.writeObject(c);
					return;
				}
				
			}
			c.setCmd(UserGroupInfo.FAIL);
			oos.writeObject(c);
			
		}





		private void doModifyGroupName(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof UserGroupModifyInfo){
				UserGroupModifyInfo ugmi=(UserGroupModifyInfo)cmd;
				if(ugmi.getSrc().equals(ugmi.getTar())){
					c.setCmd(UserGroupInfo.GROUP_REPEATE);
					
					oos.writeObject(c);
					return;
				}
				if(DBHelper.getGroup(ugmi.getTar())!=null){
					c.setCmd(UserGroupInfo.GROUP_REPEATE);
					
					oos.writeObject(c);
					return;
				}
				UserGroupInfo src=DBHelper.getGroup(ugmi.getSrc());
				UserGroupInfo tar=new UserGroupInfo();
				tar.setGroupName(ugmi.getTar());
				if(DBHelper.modifyGroupName(src,tar)){
					c.setCmd(Command.SUCCESS);
					
					oos.writeObject(c);
					return;
				}
				
			}
			c.setCmd(Command.FAIL);
			
			oos.writeObject(c);
			
		}





		private void doDeleteGroup(Command cmd) throws IOException {
			Command c=new Command();
			if(cmd instanceof UserGroupInfo){
				UserGroupInfo userGroupInfo=(UserGroupInfo)cmd;
				if(DBHelper.deleteFriends(userGroupInfo)){
					if(DBHelper.deleteUserGroup(userGroupInfo)){
						c.setCmd(Command.SUCCESS);
						oos.writeObject(c);
						return;
					}
				}
			}
			c.setCmd(Command.SUCCESS);
			oos.writeObject(c);
			
			
		}





		private void doLogin(Command cmd) throws Exception {
			
			
			Command c=new Command();
			if(cmd instanceof UserInfo){
				UserInfo loginInfo=(UserInfo)cmd;
				UserInfo userInfo=DBHelper.getUser(loginInfo.getUserName());
				if(userInfo==null){
					c.setCmd(UserInfo.NO_THE_USER);
					
					oos.writeObject(c);
					
					return;
				}
				if(userInfo.getUserPwd().equals(loginInfo.getUserPwd())){
					
					
					
					List<FriendInfo> l=DBHelper.getFriendList(userInfo);
					//oos.writeObject(l);
					
					List<UserGroupInfo> l1=DBHelper.getGroups(userInfo.getUserName());
					FriendGroupInfo fg=new FriendGroupInfo(Command.SUCCESS,l1,l);
					
					synchronized (users) {
						users.put(userInfo,socket);
					}
					oos.writeObject(fg);
					
				}else{
					c.setCmd(Command.FAIL);
					oos.writeObject(c);
				}
					
					
				
			}
			
		}

		private void doNewGroup(Command cmd) throws Exception {
			Command c=new Command();
				
			if(cmd instanceof UserGroupInfo){
				
				UserGroupInfo userGroupInfo=(UserGroupInfo)cmd;
				if(DBHelper.getGroup(userGroupInfo.getGroupName())!=null){
					c.setCmd(UserGroupInfo.GROUP_REPEATE);
					oos.writeObject(c);
					return;
				}
				if(DBHelper.addUserGroup(userGroupInfo)){
					c.setCmd(UserGroupInfo.SUCCESS);
					oos.writeObject(c);
					return;
				}else{
					c.setCmd(UserGroupInfo.FAIL);
					oos.writeObject(c);
				}
				
			}
			
			
		}
		
	}
	
	public static void main(String[] args) {
		ChatServer server=ChatServer.getInstance();
		server.start();
	}
	
	
	

}

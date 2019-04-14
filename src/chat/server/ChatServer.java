package chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import chat.server.entity.Command;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserInfo;
import chat.server.util.DBHelper;

public class ChatServer {
	
	private HashMap<UserInfo,Socket> users=new HashMap<>();
	
	private ServerSocket server;
	private ThreadPoolExecutor executor=new ThreadPoolExecutor(16, 64, 5, 
			TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024));
	
	
	private static ChatServer chatServer;
	public ChatServer getInstance(){
		
		
		if(chatServer==null){
			synchronized (this.getClass()) {
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
		DBHelper.loadUsers();
		DBHelper.loadUserGroups();
		DBHelper.loadFriendList();
		try {
			server=new ServerSocket(8088);
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
		
			try {
				if(server!=null){
					server.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void start(){
		
		while(true){
			Socket socket = null;
			try {
				socket = server.accept();
				
				
				executor.execute(new ClientHandler(socket));
			} catch (IOException e) {
				
				e.printStackTrace();
			}finally{
				try {
					if(socket!=null){
						socket.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
			
		
		
		
	}
	
	class ClientHandler implements Runnable{

		private Socket socket;
		
		private InputStream in;
		private OutputStream out;
		public  ClientHandler(Socket socket) {
			
			
			try {
				this.socket=socket;
				this.in=this.socket.getInputStream();
				this.out=this.socket.getOutputStream();
				
	
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
		
		


		@Override
		public void run() {
			
			try {
				ObjectInputStream ois=new ObjectInputStream(in);
				
				Command cmd=(Command)ois.readObject();
				switch (cmd.getCmd()) {
				case Command.LOGIN:
					doLogin(cmd);
					break;
				case Command.NEW_GROUP:
					doNewGroup(cmd);
					break;
				case Command.REMOVE_GROUP:
					doDeleteGroup(cmd);
				default:
					break;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		private void doDeleteGroup(Command cmd) {
			
			
			
		}





		private void doLogin(Command cmd) throws IOException {
			if(cmd instanceof LoginInfo){
				LoginInfo loginInfo=(LoginInfo)cmd;
				UserInfo userInfo=DBHelper.getUser(loginInfo.getUserInfo().getUserName());
				if(userInfo==null){
					out.write(LoginInfo.LOGIN_NO_THE_USER);
				}
				if(userInfo.getUserPwd().equals(loginInfo.getUserInfo().getUserPwd())){
					out.write(LoginInfo.SUCCESS);
				}else{
					out.write(LoginInfo.FAIL);
				}
					
					
				
			}
			
		}

		private void doNewGroup(Command cmd) {
			try {
				if(cmd instanceof UserGroupInfo){
					UserGroupInfo userGroupInfo=(UserGroupInfo)cmd;
					if(DBHelper.addUserGroup(userGroupInfo)){
						out.write(Command.SUCCESS);
					}else{
						out.write(UserGroupInfo.GROUP_REPEATE);
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					out.write(Command.FAIL);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		
	}
	
	
	

}
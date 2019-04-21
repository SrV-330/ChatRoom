package chat.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import chat.server.entity.Command;
import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserGroupModifyInfo;
import chat.server.util.json.JsonParser;

public class ChatClient {

	private static final Queue<Command> cmdQueue=new LinkedBlockingQueue<>();
	private static final Queue<Command> reqQueue=new LinkedBlockingQueue<>();
	private Socket socket;
	private JsonParser jp;
	private OutputStream out;
	private InputStream in;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	public ChatClient(){
		init();
	}
	private void init(){
		try {
			System.out.println("connecting...");
			socket=new Socket("127.0.0.1",8088);
			jp=new JsonParser();
			out=socket.getOutputStream();
			in=socket.getInputStream();
			oos=new ObjectOutputStream(out);
			ois=new ObjectInputStream(in);
			System.out.println("connected");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	}
	private void start(){
		try {
//			UserInfo cmd=new UserInfo("A","a");
//			cmd.setCmd(Command.REGIST);
//			UserInfo cmd=new UserInfo("A","a");
//			cmd.setCmd(Command.LOGIN);
//			UserGroupInfo cmd=new UserGroupInfo("A", "Friend");
//			cmd.setCmd(Command.NEW_GROUP);
//			UserInfo cmd=new UserInfo("B","b");
//			cmd.setCmd(Command.REGIST);
//			UserGroupModifyInfo cmd=new UserGroupModifyInfo("A","Friend", "Friends");
//			cmd.setCmd(Command.MODIFY_GROUP_NAME);
//			UserGroupInfo cmd=new UserGroupInfo("A","Friends");
//			cmd.setCmd(Command.MODIFY_GROUP_NAME);			
//			FriendInfo cmd=new FriendInfo("A", "B","Default");
//			cmd.setCmd(Command.REMOVE_FRIEND);
//			FriendInfo cmd=new FriendInfo("A", "B","Default");
//			cmd.setCmd(Command.ADD_FRIEND);		
//			UserGroupModifyInfo cmd=new UserGroupModifyInfo("A","Default", "Friends");
//			cmd.setCmd(Command.MOVE_TO_GROUP);
//			UserGroupInfo cmd=new UserGroupInfo("A","Friends");
//			cmd.setCmd(Command.REMOVE_GROUP);	
//				
			try{
				
				Command cmd=null;
				
				
				while(true){
					if(cmdQueue.isEmpty()) continue;
					cmd=cmdQueue.poll();
					if(cmd.getCmd()==Command.LOGOFF) break;
					switch (cmd.getCmd()) {
					case Command.LOGIN:
							
						
						break;

					default:
						break;
					}
					oos.writeObject(cmd);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					if(in!=null){
						in.close();
					}
					if(out!=null){
						out.close();
					}
					if(socket!=null){
						socket.close();
					}
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
			}
			
			
			
			
			Thread read=new Thread(){
				@Override
				public void run(){
					try {
						while(true){
							Command o=(Command) ois.readObject();
							reqQueue.offer(o);
							
								
							System.out.println(o);
								
							
						}
						
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}finally{
						try {
							if(in!=null){
								in.close();
							}
							if(out!=null){
								out.close();
							}
							if(socket!=null){
								socket.close();
							}
						} catch (Exception e1) {
							
							e1.printStackTrace();
						}
					}
				}
			};
			read.setDaemon(true);
			read.start();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		ChatClient client=new ChatClient();
		
		client.start();
			
		
		
		
	}
}

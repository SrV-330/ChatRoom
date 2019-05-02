package chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import chat.client.form.SingleForm;
import chat.server.entity.Command;
import chat.server.entity.SendMessageInfo;

public class ChatClient {

	private static final Queue<Command> cmdQueue=new LinkedBlockingQueue<>();
	private static final Queue<Command> cmdRespQueue=new LinkedBlockingQueue<>();
	private static final Queue<Command> msgQueue=new LinkedBlockingQueue<>();
	
	private static ChatClient client;
	private ClientContext context;
	
	private Thread receive;
	private Thread read;
	private Thread readMsg;
	
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean connecting=false;
	private boolean start=false;
	
	public static ChatClient getInstance(){
		if(client==null){
			synchronized (ChatClient.class) {
				if(client==null){
					client=new ChatClient();
					return client;
				}
			}
		}
		return client;
	}
	private ChatClient(){
		
	}
	public void init(){
		try {
			if(connecting) return;
			synchronized (client) {
				if(connecting) return;
				System.out.println("connecting...");
				socket=new Socket("127.0.0.1",9099);
				out=socket.getOutputStream();
				in=socket.getInputStream();
				oos=new ObjectOutputStream(out);
				ois=new ObjectInputStream(in);
				connecting=true;
				System.out.println("connected");
			}
			
		} catch (Exception e) {
			
			try {
				if(socket!=null){
					socket.close();
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			connecting=false;
			System.out.println("connect time out");
			JOptionPane.showMessageDialog(null,"connect time out","tip", JOptionPane.ERROR_MESSAGE);
			//System.exit(0);
		} 
		
	}
	public void start(){
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
			if(!connecting) return;
			if(start) return;
			receive=new Thread(){
				@Override
				public void run(){
					try{
						
						Command cmd=null;
						while(true){
							
							if(cmdQueue.isEmpty()) {
								synchronized (cmdQueue) {
									if(cmdQueue.isEmpty()){
										cmdQueue.wait();
									}
								}
								
							}
							cmd=cmdQueue.poll();
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
							connecting=false;
						} catch (Exception e1) {
							
							e1.printStackTrace();
						}
					}
				}
			};
			receive.start();
			
			
			read=new Thread(){
				@Override
				public void run(){
					try {
						while(true){
							
							Command cmd=(Command) ois.readObject();
							if(cmd.getCmd()==Command.RECEIVE_MSG){
								msgQueue.offer(cmd);
								synchronized (msgQueue) {
									msgQueue.notify();
								}
								
								
							}else{
								cmdRespQueue.offer(cmd);
								
								
								
								
							}
							
							
								
							System.out.println(cmd);
								
							
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
							connecting=false;
						} catch (Exception e1) {
							
							e1.printStackTrace();
						}
					}
				}
			};
			read.setDaemon(true);
			read.start();
			readMsg=new Thread(){
				@Override
				public void run(){
					try {
						while(true){
							if(msgQueue.isEmpty()) {
								synchronized (msgQueue) {
									if(msgQueue.isEmpty()){
										msgQueue.wait();
									}
								}
								
							}
							Command cmd=msgQueue.peek();
							
							SendMessageInfo msg=(SendMessageInfo)cmd;
							
							SingleForm form=context.getSingleForm(msg.getSendUserName());
							if(form!=null&&form.isVisible()){
								msgQueue.poll();
								form.receive(msg);
							}
								
							
								
							
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
							connecting=false;
						} catch (Exception e1) {
							
							e1.printStackTrace();
						}
					}
				}
			};
			readMsg.setDaemon(true);
			readMsg.start();
			start=true;
		} catch (Exception e) {
			connecting=false;
			start=false;
			e.printStackTrace();
		}
		
	}
	
	public boolean addCommand(Command cmd){
		synchronized (cmdQueue) {
			cmdQueue.notify();
		}
		
		return cmdQueue.offer(cmd);
	}
	
	public Command getCmdResp(){
		Command cmd=null;
		while(cmdRespQueue.isEmpty());
		while((cmd=cmdRespQueue.poll())==null);
		
		return cmd;
		
	}
	public synchronized List<Command> getMessages(String key){
		Iterator<Command> iterator=msgQueue.iterator();
		List<Command> cmds=new ArrayList<>();
		while(iterator.hasNext()){
			SendMessageInfo cmd=(SendMessageInfo)iterator.next();
			System.out.println("SendMessageInfo: "+cmd);
			if(key.equals(cmd.getReceiveUserName())){
				cmds.add(cmd);
				iterator.remove();
			}
		}
		return cmds.size()==0?null:cmds;
	}
	
	public boolean isConnecting() {
		return connecting;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public ClientContext getContext() {
		return context;
	}
	public void setContext(ClientContext context) {
		this.context = context;
	}
	public static void main(String[] args) throws Exception {
		ChatClient client=ChatClient.getInstance();
		client.start();
		
	}
}

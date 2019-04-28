package chat.client;

import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import java.awt.Component;

import chat.client.form.ChatForm;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.UserInfo;

public class ClientContext {
	

	public static final void init(){
		try {
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			BeautyEyeLNFHelper.frameBorderStyle=BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			UIManager.put("RootPane.setupButtonVisible", false);
			BeautyEyeLNFHelper.debug=false;
			BeautyEyeLNFHelper.translucencyAtFrameInactive=false;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			
		} catch (Exception e) {
			
		}
	}
	
	public static final void showMsgBox(Component p,String text,String title,int type){
		JOptionPane.showMessageDialog(p,text,title, type);
	}
	
	private Stack<JFrame> forms=new Stack<>();
	private ChatForm chatForm;
	private UserInfo user;
	private FriendGroupInfo friendGroupInfo;
	
	
	
	public FriendGroupInfo getFriendGroupInfo() {
		return friendGroupInfo;
	}

	public void setFriendGroupInfo(FriendGroupInfo friendGroupInfo) {
		this.friendGroupInfo = friendGroupInfo;
		System.out.println(friendGroupInfo);
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		if(user==null) return;
		if(user.getUserName()==null) return;
		if(user.getUserPwd()==null) return;
		
		
		this.user = user;
	}
	
	public JFrame formsPop(){
		if(forms.isEmpty()) return null;
		if(forms.peek() instanceof ChatForm){
			this.chatForm=null;
		}
		return forms.pop();
	}
	public JFrame formsPeek(){
		if(forms.isEmpty()) return null;
		return forms.peek();
	}
	public JFrame formsPush(JFrame item){
		if(item instanceof ChatForm){
			this.chatForm=(ChatForm)item;
		}
		return forms.push(item);
	}
	private ChatClient client;
	
	
	{
		client=ChatClient.getInstance();
	}
	public ChatClient getClient(){
		
		
		return client;
	}
	public boolean isConnecting(){
		return client.isConnecting();
		
	}
	public boolean isStart(){
		
		
		return client.isStart();
		
	}
	public ChatForm getChatForm(){
		return this.chatForm;
	}

	public void setChatForm(ChatForm chatForm) {
		this.chatForm = chatForm;
	}
	

}

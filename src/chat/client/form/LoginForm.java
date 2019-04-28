package chat.client.form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chat.client.ChatClient;
import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.UserInfo;


@SuppressWarnings("serial")
public class LoginForm extends AbstractForm {

	
	private static LoginForm loginForm;
	
	public static LoginForm getInstance(ClientContext context){
		if(loginForm==null){
			synchronized (LoginForm.class) {
				
				if(loginForm==null){
					loginForm=new LoginForm(context);
					return loginForm;
				}
			}
		}
		
		return loginForm;
	}
	private LoginForm(ClientContext context){
		super(context);
		
	}

	@Override
	public void addTitle(JLabel lbl) {
		lbl.setText("User Login");
		
	}
	@Override
	public void addGotoText(JLabel lbl) {
		
		lbl.setText("Register");
	}
	@Override
	public void addGotoListener(JLabel lbl) {
		lbl.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginForm.this.hideForm();
				RegForm regForm=RegForm.getInstance(getContext());
				regForm.showForm();
			}
			
		});
	}
	@Override
	public void addOkText(JButton btn) {
		
		btn.setText("Login");
	}
	@Override
	public void addOKListener(JButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName=getNameText();
				System.out.println(userName);
				if(!Vaild.vaild(userName)){
					JOptionPane.showMessageDialog(null, "A-Z,a-z,0-9 {3,16}","tip",JOptionPane.ERROR_MESSAGE);
					
					return;
				}
				
				
				String userPwd=getPassword();
				System.out.println(userPwd);
				if(!Vaild.vaild(userPwd)){
					JOptionPane.showMessageDialog(null,"A-Z,a-z,0-9 {3,16}","tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Command cmd=new UserInfo(userName,userPwd);
				cmd.setCmd(Command.LOGIN);
				//TODO
				
				ChatClient client=getContext().getClient();
				client.init();
				client.start();
				client.addCommand(cmd);
				System.out.println(cmd);
				cmd=client.getCmdResp();
				System.out.println(cmd);
				if(cmd.getCmd()==Command.SUCCESS){
					System.out.println(cmd);
					getContext().setFriendGroupInfo((FriendGroupInfo)cmd);
					getContext().setUser(new UserInfo(userName,userPwd));
					LoginForm.this.hideForm();
					ChatForm chatForm=ChatForm.getInstance(getContext());
					chatForm.showForm();
				}else if(cmd.getCmd()==Command.NO_THE_USER){
					JOptionPane.showMessageDialog(null,"name or pwd error","tip", JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"Login Fail","tip", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
			
			
		});
		
	}

	public static void main(String[] args) {

		LoginForm loginForm=LoginForm.getInstance(new ClientContext());
		loginForm.showForm();

	}

}

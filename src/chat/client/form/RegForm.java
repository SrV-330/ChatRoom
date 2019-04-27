package chat.client.form;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import chat.client.ChatClient;
import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.UserInfo;


public class RegForm extends AbstractForm {
	
	
	private static RegForm regForm;
	public static RegForm getInstance(ClientContext context){
		if(regForm==null){
			synchronized (RegForm.class) {
				
				if(regForm==null){
					regForm=new RegForm(context);
					return regForm;
				}
			}
		}
		return regForm;
	}
	private RegForm(ClientContext context){
		super(context);
		
	}
	

	
	
	@Override
	public void addTitle(JLabel lbl) {
		
		lbl.setText("User Register");
	}
	@Override
	public void addGotoText(JLabel lbl) {
		lbl.setText("Login");
	}
	@Override
	public void addOkText(JButton btn) {
		btn.setText("Register");
	}
	@Override
	public void addOKListener(JButton btn) {
		btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String userName=getNameText();
				System.out.println(userName);
				if(!Vaild.vaild(userName)){
					JOptionPane.showMessageDialog(null, "A-Z,a-z,0-9 {6,16}","tip",JOptionPane.ERROR_MESSAGE);
					
					return;
				}
				
				
				String userPwd=getPassword();
				System.out.println(userPwd);
				if(!Vaild.vaild(userPwd)){
					JOptionPane.showMessageDialog(null,"A-Z,a-z,0-9 {6,16}","tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Command cmd=new UserInfo(userName,userPwd);
				cmd.setCmd(Command.REGIST);
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
					JOptionPane.showMessageDialog(null,"Register Success","tip", JOptionPane.OK_CANCEL_OPTION);
				}else if(cmd.getCmd()==Command.NO_THE_USER){
					JOptionPane.showMessageDialog(null,"name or pwd error","tip", JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null,"Login Fail","tip", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
			
			
		});
	}
	public static void main(String[] args) {
		RegForm regForm=RegForm.getInstance(null);
		regForm.showForm();
	}
	
	

}

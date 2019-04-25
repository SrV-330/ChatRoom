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

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import chat.client.ChatClient;
import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.UserInfo;


public class LoginForm extends JFrame {
	
	
	
	static {
		ClientContext.init();
	}
	private JButton btn_login;
	private JButton btn_cancel;
	private JTextField txt_name;
	private JPasswordField txt_pwd;
	private JLabel lbl_name;
	private JLabel lbl_pwd;
	private JLabel lbl_title;
	private JLabel lbl_reg;
	private static RegForm regForm;
	private ChatClient client;
	private static LoginForm loginForm;
	public static LoginForm getInstance(){
		if(loginForm==null){
			synchronized (LoginForm.class) {
				
				if(loginForm==null){
					loginForm=new LoginForm();
					return loginForm;
				}
			}
		}
		return loginForm;
	}
	private LoginForm() {
		super();
		client=ChatClient.getInstance();
		this.setTitle("Login");
		this.setSize(430,250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		
		
		
		
		btn_login=new JButton("Login");
		btn_login.setSize(100,30);
		btn_login.setLocation(70, 160);
		btn_login.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		
		btn_login.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String userName=txt_name.getText();
				System.out.println(userName);
				if(!Vaild.vaild(userName)){
					JOptionPane.showMessageDialog(null, "A-Z,a-z,0-9 {0,16}","tip",JOptionPane.ERROR_MESSAGE);
					
					return;
				}
				
				
				String userPwd=String.copyValueOf(txt_pwd.getPassword());
				System.out.println(userPwd);
				if(!Vaild.vaild(userPwd)){
					JOptionPane.showMessageDialog(null,"A-Z,a-z,0-9 {0,16}","tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Command cmd=new UserInfo(userName,userPwd);
				cmd.setCmd(Command.LOGIN);
				
				
				
				
			}
			
			
		});
		
		btn_cancel=new JButton("Cancel");
		btn_cancel.setSize(100, 30);
		btn_cancel.setLocation(70+60+100, 160);
		
		btn_cancel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				System.exit(NORMAL);
			}
			
			
		});
		
		
		lbl_reg=new JLabel();
		lbl_reg.setText("Register");
		lbl_reg.setSize(100,20);
		lbl_reg.setLocation(70+60+100+100+10,165);
		lbl_reg.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lbl_reg.setForeground(Color.BLUE);
		
		lbl_reg.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				LoginForm.this.setVisible(false);
				LoginForm.regForm=RegForm.getInstance(client, loginForm);
				LoginForm.regForm.showForm();
			}
			
		});
		
		txt_name=new JTextField();
		txt_name.setSize(160,30);
		txt_name.setLocation(170, 60);
		
		txt_pwd=new JPasswordField();
		txt_pwd.setSize(160,30);
		txt_pwd.setLocation(170, 110);
		
		
		lbl_name=new JLabel("UserName: ");
		lbl_name.setSize(90,30);
		lbl_name.setLocation(80, 60);
		
		
		lbl_pwd=new JLabel("Password: ");
		lbl_pwd.setSize(90,30);
		lbl_pwd.setLocation(80, 110);
		
		lbl_title=new JLabel("User Login");
		lbl_title.setFont(new Font("黑体", Font.BOLD, 20));
		lbl_title.setSize(200,60);
		lbl_title.setLocation(150, 0);
		
		this.add(btn_login);
		this.add(btn_cancel);
		this.add(lbl_reg);
		this.add(txt_name);
		this.add(txt_pwd);
		this.add(lbl_name);
		this.add(lbl_pwd);
		this.add(lbl_title);
		
		
		
		
	}
	
	public void showForm(){
		loginForm.setVisible(true);
	}
	public static void main(String[] args) {
		
		
		
		LoginForm loginForm=LoginForm.getInstance();
		loginForm.showForm();
		
		//System.out.println(Vaild.vaild("a"));
	
		
	}
	

}

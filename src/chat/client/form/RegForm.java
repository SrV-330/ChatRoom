package chat.client.form;


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


public class RegForm extends JFrame {
	static {
		ClientContext.init();
	}
	private JButton btn_reg;
	private JButton btn_cancel;
	private JTextField txt_name;
	private JPasswordField txt_pwd;
	private JLabel lbl_name;
	private JLabel lbl_pwd;
	
	private JLabel lbl_title;
	
	private ChatClient client;
	private static RegForm regForm;
	public static RegForm getInstance(ChatClient client){
		if(regForm==null){
			synchronized (RegForm.class) {
				
				if(regForm==null){
					regForm=new RegForm(client);
					return regForm;
				}
			}
		}
		return regForm;
	}
	private RegForm(ChatClient client) {
		super();
		this.client=client;
		this.setTitle("Register");
		this.setSize(400,250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		
		
		
		
		btn_reg=new JButton("Register");
		btn_reg.setSize(100,30);
		btn_reg.setLocation(70, 160);
		btn_reg.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		
		btn_reg.addMouseListener(new MouseAdapter() {

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
		
		txt_name=new JTextField();
		txt_name.setSize(180,30);
		txt_name.setLocation(150, 60);
		
		txt_pwd=new JPasswordField();
		txt_pwd.setSize(180,30);
		txt_pwd.setLocation(150, 110);
		
		
		lbl_name=new JLabel("UserName: ");
		lbl_name.setSize(90,30);
		lbl_name.setLocation(80, 60);
		
		
		lbl_pwd=new JLabel("Password: ");
		lbl_pwd.setSize(90,30);
		lbl_pwd.setLocation(80, 110);
		
		lbl_title=new JLabel("User Register");
		lbl_title.setFont(new Font("黑体", Font.BOLD, 20));
		lbl_title.setSize(200,60);
		lbl_title.setLocation(150, 0);
		
		this.add(btn_reg);
		this.add(btn_cancel);
		this.add(txt_name);
		this.add(txt_pwd);
		this.add(lbl_name);
		this.add(lbl_pwd);
		this.add(lbl_title);
		
		
		
		
	}
	
	public void showForm(){
		regForm.setVisible(true);
	}
	public static void main(String[] args) {
		RegForm regForm=RegForm.getInstance(null);
		regForm.showForm();
	}
	
	

}

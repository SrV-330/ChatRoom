package chat.client.form;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import chat.client.ClientContext;

@SuppressWarnings("serial")
public class AbstractForm extends EmptyForm{
	
	private JButton btn_ok;
	private JButton btn_cancel;
	private JTextField txt_name;
	private JPasswordField txt_pwd;
	private JLabel lbl_name;
	private JLabel lbl_pwd;
	private JLabel lbl_title;
	private JLabel lbl_goto;
	
	

	public AbstractForm(ClientContext context){
		super(context);
		this.setTitle("AbstrctForm");
		this.setSize(430,250);
		
		
		btn_ok=new JButton();
		addOkText(btn_ok);
		btn_ok.setSize(100,30);
		btn_ok.setLocation(70, 160);
		btn_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		addOKListener(btn_ok);
		
		
		btn_cancel=new JButton();
		addCancelText(btn_cancel);
		btn_cancel.setSize(100, 30);
		btn_cancel.setLocation(70+60+100, 160);
		addCancelListener(btn_cancel);
		
		
		lbl_goto=new JLabel();
		addGotoText(lbl_goto);
		lbl_goto.setSize(100,20);
		lbl_goto.setLocation(70+60+100+100+10,165);
		lbl_goto.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lbl_goto.setForeground(Color.BLUE);
		addGotoListener(lbl_goto);
		
		txt_name=new JTextField();
		txt_name.setSize(160,30);
		txt_name.setLocation(170, 60);
		
		txt_pwd=new JPasswordField();
		txt_pwd.setSize(160,30);
		txt_pwd.setLocation(170, 110);
		
		
		
		lbl_name=new JLabel();
		addNameText(lbl_name);
		lbl_name.setFont(new Font("黑体", Font.BOLD, 16));
		lbl_name.setSize(90,30);
		lbl_name.setLocation(80, 60);
		
		
		lbl_pwd=new JLabel();
		addPasswordText(lbl_pwd);
		lbl_pwd.setFont(new Font("黑体", Font.BOLD, 16));
		lbl_pwd.setSize(90,30);
		lbl_pwd.setLocation(80, 110);
		
		lbl_title=new JLabel();
		addTitle(lbl_title);
		lbl_title.setFont(new Font("黑体", Font.BOLD, 20));
		lbl_title.setSize(lbl_title.getFont().getSize()*lbl_title.getText().length(),60);
		lbl_title.setLocation(this.getWidth()/2-lbl_title.getFont().getSize()*lbl_title.getText().length()/2+20, 0);
		
		
		
		JPanel jp_content=getContent();

		jp_content.add(btn_ok);
		jp_content.add(btn_cancel);
		jp_content.add(lbl_goto);
		jp_content.add(txt_name);
		jp_content.add(txt_pwd);
		jp_content.add(lbl_name);
		jp_content.add(lbl_pwd);
		jp_content.add(lbl_title);
		
		
		
	}
	
	public void addPasswordText(JLabel lbl) {
		
		lbl.setText("Password: ");
	}

	public void addNameText(JLabel lbl) {
		
		lbl.setText("UserName: ");
	}

	public void addTitle(JLabel lbl){
		lbl.setText("Title");
		
	}
	public void addGotoText(JLabel lbl){
		lbl.setText("Goto");
	}
	public void addGotoListener(JLabel lbl){
		lbl.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				AbstractForm.this.dispose();
				System.out.println("dispose");
			}
			
			
		});
	}
	public void addOkText(JButton btn){
		btn.setText("OK");
	}
	public void addCancelText(JButton btn){
		btn.setText("Cancel");
	}
	public void addOKListener(JButton btn){
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				
			}
			
		});
	}
	public void addCancelListener(JButton btn){
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				System.exit(0);
				
			}
			
		});
	}
	
	
	

	
	protected String getNameText(){
		return txt_name.getText();
	}
	
	protected String getPassword(){
		return String.valueOf(txt_pwd.getPassword());
	}


	public static void main(String[] args) {
		AbstractForm form=new AbstractForm(new ClientContext());
		form.showForm();
	}
	

}

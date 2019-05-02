package chat.client.form;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import chat.client.ClientContext;

@SuppressWarnings("serial")
public class SearchForm extends EmptyForm{


	private JButton btn_ok;
	private JButton btn_cancel;
	private JLabel lbl_kw;
	private JTextField txt_kw;
	private JLabel lbl_title;
	
	public SearchForm(ClientContext context){
		super(context);
		init();
	}
	
	private void init(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(430, 170);
		btn_ok=new JButton();
		addOkText(btn_ok);
		btn_ok.setSize(100,30);
		btn_ok.setLocation(70, this.getHeight()-btn_ok.getHeight()-50);
		btn_ok.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		addOKListener(btn_ok);
		
		
		btn_cancel=new JButton();
		addCancelText(btn_cancel);
		btn_cancel.setSize(100, 30);
		btn_cancel.setLocation(70+60+btn_cancel.getWidth(), this.getHeight()-btn_ok.getHeight()-50);
		addCancelListener(btn_cancel);
		
		
		lbl_title=new JLabel();
		addTitle(lbl_title);
		lbl_title.setFont(new Font("黑体", Font.BOLD, 20));
		lbl_title.setSize(lbl_title.getFont().getSize()*lbl_title.getText().length(),60);
		lbl_title.setLocation(this.getWidth()/2-lbl_title.getFont().getSize()*lbl_title.getText().length()/2+20, 0);
		
		
		txt_kw=new JTextField();
		txt_kw.setSize(160,30);
		txt_kw.setLocation(170, 50);
		
		lbl_kw=new JLabel();
		addNameText(lbl_kw);
		lbl_kw.setFont(new Font("黑体", Font.BOLD, 14));
		lbl_kw.setSize(90,30);
		lbl_kw.setLocation(80, 50);
		
		JPanel jp_content=getContent();
		jp_content.add(btn_ok);
		jp_content.add(btn_cancel);
		jp_content.add(lbl_title);
		jp_content.add(txt_kw);
		jp_content.add(lbl_kw);
	}
	protected void reLayout(){
		btn_ok.setLocation(70, this.getHeight()-btn_ok.getHeight()-30);
		btn_cancel.setLocation(70+60+btn_cancel.getWidth(), this.getHeight()-btn_ok.getHeight()-30);
		lbl_title.setLocation(150, 0);
		lbl_title.setSize(lbl_title.getFont().getSize()*lbl_title.getText().length(),60);
		lbl_title.setLocation(this.getWidth()/2-lbl_title.getFont().getSize()*lbl_title.getText().length()/2+20, 0);
		txt_kw.setLocation(170, 50);
	}
	public void addNameText(JLabel lbl) {
		
		lbl.setText("KeyWord: ");
	}
	public void addTitle(JLabel lbl){
		lbl.setText("Title");
		
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
				SearchForm.this.dispose();
				
			}
			
		});
	}
	
	
	protected String getKeyWord(){
		return this.txt_kw.getText();
	}
	public static void main(String[] args) {
		new SearchForm(new ClientContext()).showForm();
	}
	
	
	

}

package chat.client.form;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import chat.client.ClientContext;
import chat.server.entity.Command;
import chat.server.entity.SendMessageInfo;

@SuppressWarnings("serial")
public class SingleForm extends EmptyForm{

	
	
	private JTextPane txt_area;
	private JTextField txt_input;
	private JScrollPane sp;
	private JButton btn_send;
	private String friendName="";
	
	private Document document;
	
	public static SingleForm singleForm;
	
	private SingleForm(ClientContext context,String friendName) {
		super(context);
		this.friendName=friendName;
		init();
		
	}
	private void init(){
		
		this.setTitle(friendName);
		this.setSize(600,610);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		txt_area=new JTextPane();
		txt_area.setSize(590,500);
		txt_area.setLocation(0, 0);
		txt_area.setEditable(false);
		document=txt_area.getDocument();
		
		txt_input=new JTextField();
		txt_input.setSize(400,50);
		txt_input.setLocation(0, 510);
		
		btn_send=new JButton("Send");
		btn_send.setSize(100,50);
		btn_send.setLocation(400, 510);
		
		btn_send.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				send();
			}
			
			
			
		});
		
		sp=new JScrollPane(txt_area);
		sp.setSize(590,500);
		sp.setLocation(0, 0);
		
		
		getContent().setSize(600, 610);
		getContent().add(sp);
		getContent().add(txt_input);
		getContent().add(btn_send);
	}
	public static SingleForm getInstance(ClientContext context,String friendName){
		if(singleForm==null){
			synchronized (SingleForm.class) {
				if(singleForm==null){
					singleForm=new SingleForm(context,friendName);
				}
				
			}
		}
		return singleForm;
		
	}
	
	
	
	private void send(){
		
		Command cmd=new SendMessageInfo(Command.SEND_MSG, 
				getContext().getUser().getUserName(), friendName,getInput());
		getContext().getClient().addCommand(cmd);
		cmd=getContext().getClient().getCmdResp();
		if(cmd.getCmd()==Command.SUCCESS){
			showMessage(getInput(), true);
			this.txt_input.setText("");
		}else if(cmd.getCmd()==Command.NOT_FRIEND){
			ClientContext.showMsgBox(singleForm,"Not Friend", "tip", JOptionPane.ERROR_MESSAGE);
		}else if(cmd.getCmd()==Command.NOT_ON_LINE){
			ClientContext.showMsgBox(singleForm,"Not Online", "tip", JOptionPane.ERROR_MESSAGE);
		}else{
			ClientContext.showMsgBox(singleForm,"Send error", "tip", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	public void showMessage(String msg,boolean send){
		SimpleAttributeSet attr=new SimpleAttributeSet();
		 StyleConstants.setForeground(attr, Color.green);
		 StyleConstants.setBold(attr, true);
		 StyleConstants.setFontSize(attr, 18);
		 Calendar calendar=Calendar.getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-dd hh:mm:ss E");
		 	 
		try {
			
			
			String m="";
			if(send){
				StyleConstants.setForeground(attr, Color.blue);
				m+=sdf.format(calendar.getTime())+"\t";
				m+="I say:\n";
			}else{
				StyleConstants.setForeground(attr, Color.black);
				m+=sdf.format(calendar.getTime())+"\t";
				m+=friendName+" say: \n" ;
			}
			document.insertString(document.getLength(),m,attr);
			StyleConstants.setFontSize(attr, 22);
			document.insertString(document.getLength(),msg +"\n",attr);
			txt_area.setCaretPosition(document.getLength());
			
			
		} catch (BadLocationException e) {
			
		}
	}
	public String getInput(){
		return this.txt_input.getText();
	}
	public void receive(){
		System.out.println("FriendName: "+friendName);
		List<Command> cmds=getContext().getClient().getMessages(friendName);
		if(cmds!=null){
			for(Command cmd:cmds){
				System.out.println(cmd);
				SendMessageInfo msg=(SendMessageInfo)cmd;
				showMessage(msg.getMessage(), false);
			}
		}
	}
	public synchronized void receive(SendMessageInfo msg){
		showMessage(msg.getMessage(), false);
	}
	@Override
	public void showForm() {
		this.setVisible(true);
		receive();
	}
	@Override
	public void addClosedListener(EmptyForm form) {
		form.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				getContext().removeSingleForm(friendName);
			}
			
			
			
		});
		
	}
	public static void main(String[] args) {
		getInstance(new ClientContext(),null).showForm();
	}
	
	

}

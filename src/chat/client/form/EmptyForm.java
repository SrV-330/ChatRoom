package chat.client.form;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chat.client.ClientContext;

@SuppressWarnings("serial")
public class EmptyForm extends JFrame {
	
	static {
		ClientContext.init();
	}
	private ClientContext context;
	private JPanel jp_content;
	public EmptyForm(){
		super();
		this.setSize(430,250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.addClosedListener(this);
		
		jp_content=new JPanel();
		jp_content.setLayout(null);
		jp_content.setSize(430,250);
		jp_content.setLocation(0, 0);
		
		this.add(jp_content);
		
	}
	public EmptyForm(ClientContext context) throws HeadlessException {
		this();
		this.context = context;
	}
	protected JPanel getContent(){
		return jp_content;
	}
	public ClientContext getContext() {
		return context;
	}
	public void showForm(){
		getContext().formsPush(this);
		this.setVisible(true);
	}
	public void hideForm(){
		this.setVisible(false);
	}
	public void addClosedListener(EmptyForm form){
		
		form.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				
				super.windowClosed(e);
				System.out.println("close");
				getContext().formsPop();
				if(getContext().formsPeek()!=null)
					getContext().formsPeek().setVisible(true);
				EmptyForm.this.dispose();
			}
			
		});
	}
	public static void main(String[] args) {
		EmptyForm form=new EmptyForm(new ClientContext());
		form.showForm();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmptyForm other = (EmptyForm) obj;
		
		
		if(other.getTitle().equals(this.getTitle())){
			
			
			return true;
			
			
		}else{
			return false;
		}
	}

}

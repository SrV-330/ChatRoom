package chat.client.form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.UserGroupInfo;

@SuppressWarnings("serial")
public class NewGroupForm extends SearchForm {

	
	private static NewGroupForm newGroupForm;
	
	public static NewGroupForm getInstance(ClientContext context){
		if(newGroupForm==null){
			synchronized (NewGroupForm.class) {
				if(newGroupForm==null){
					newGroupForm=new NewGroupForm(context);
					return newGroupForm;
				}
			}
		}
		return newGroupForm;
	}
	private NewGroupForm(ClientContext context) {
		super(context);
		
	}
	
	@Override
	public void addNameText(JLabel lbl) {
		lbl.setText("GroupName: ");
	}
	@Override
	public void addTitle(JLabel lbl) {
		lbl.setText("New Group");
	}
	@Override
	public void addOkText(JButton btn) {
		btn.setText("New Group");
	}
	@Override
	public void addOKListener(JButton btn) {
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String groupName=getKeyWord();
				if(!Vaild.vaild(groupName)){ 
					JOptionPane.showMessageDialog(NewGroupForm.this, "A-Z,a-z,0-9 {3,16}","tip",JOptionPane.ERROR_MESSAGE);
					return;
				}
				UserGroupInfo group=new UserGroupInfo();
				group.setCmd(Command.NEW_GROUP);
				group.setUserName(getContext().getUser().getUserName());
				group.setGroupName(groupName);
				getContext().getClient().addCommand(group);
				Command cmd=getContext().getClient().getCmdResp();
				if(cmd.getCmd()==Command.SUCCESS){
					getContext().getChatForm().addGroup(group);
					System.out.println(group);
					NewGroupForm.this.dispose();
					return;
				}else if(cmd.getCmd()==Command.GROUP_REPEATE){
					ClientContext.showMsgBox(NewGroupForm.this, "Group Repeate", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					ClientContext.showMsgBox(NewGroupForm.this, "New Group Fail", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
					
				
			}
			
		});
	}
	public static void main(String[] args) {
		new NewGroupForm(new ClientContext()).showForm();
	}
	
	
	

}

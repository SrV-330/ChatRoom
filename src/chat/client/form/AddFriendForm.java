package chat.client.form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.FriendInfo;

@SuppressWarnings("serial")
public class AddFriendForm extends SearchForm {

	
	private static AddFriendForm addFriendForm;
	
	public static AddFriendForm getInstance(ClientContext context){
		if(addFriendForm==null){
			synchronized (AddFriendForm.class) {
				if(addFriendForm==null){
					addFriendForm=new AddFriendForm(context);
					return addFriendForm;
				}
				
			}
		}
		return addFriendForm;
	}
	
	public AddFriendForm(ClientContext context) {
		super(context);
		
	}

	@Override
	public void addNameText(JLabel lbl) {
		lbl.setText("FriendName: ");
	}

	@Override
	public void addTitle(JLabel lbl) {
		lbl.setText("Add Friend");
	}

	@Override
	public void addOkText(JButton btn) {
		btn.setText("Search");
	}

	@Override
	public void addOKListener(JButton btn) {
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String friendName=getKeyWord();
				if(!Vaild.vaild(friendName)){ 
					JOptionPane.showMessageDialog(AddFriendForm.this, "A-Z,a-z,0-9 {3,16}","tip",JOptionPane.ERROR_MESSAGE);
					return;
				}
				FriendInfo friend=new FriendInfo();
				friend.setCmd(Command.ADD_FRIEND);
				friend.setUserName(getContext().getUser().getUserName());
				friend.setGroupName("Default");
				friend.setFriendName(friendName);
				getContext().getClient().addCommand(friend);
				Command cmd=getContext().getClient().getCmdResp();
				if(cmd.getCmd()==Command.SUCCESS){
					getContext().getChatForm().addFriend(friend);
					AddFriendForm.this.dispose();
					return;
				}else if(cmd.getCmd()==Command.NO_THE_USER){
					ClientContext.showMsgBox(AddFriendForm.this, "No the User", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}else if(cmd.getCmd()==Command.ALREADY_IS_FRIEND){
					ClientContext.showMsgBox(AddFriendForm.this, "Already is friend", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					ClientContext.showMsgBox(AddFriendForm.this, "Add friend Fail", "tip", JOptionPane.ERROR_MESSAGE);
				}
				
					
				
			}
			
		});
	}
	

}

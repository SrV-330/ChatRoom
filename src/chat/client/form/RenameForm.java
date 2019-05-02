package chat.client.form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import chat.client.ClientContext;
import chat.client.util.Vaild;
import chat.server.entity.Command;
import chat.server.entity.UserGroupModifyInfo;

@SuppressWarnings("serial")
public class RenameForm extends SearchForm{

	public static RenameForm renameForm;
	private DefaultMutableTreeNode tar;
	private DefaultTreeModel model;
	private RenameForm(ClientContext context) {
		super(context);
		
	}
	
	private RenameForm(ClientContext context,DefaultMutableTreeNode tar,DefaultTreeModel model) {
		super(context);
		this.tar=tar;
		this.model=model;
		
	}
	
	public static RenameForm getInstance(ClientContext context,DefaultMutableTreeNode tar,DefaultTreeModel model){
		if(renameForm==null){
			synchronized (RenameForm.class) {
				if(renameForm==null){
					renameForm=new RenameForm(context,tar,model);
				}
				
			}
		}
		return renameForm;
	}

	@Override
	public void addNameText(JLabel lbl) {
		
		lbl.setText("New Name: ");
	}

	@Override
	public void addTitle(JLabel lbl) {
		lbl.setText("Group Rename");
	}

	@Override
	public void addOkText(JButton btn) {
		btn.setText("Rename");
	}

	@Override
	public void addOKListener(JButton btn) {
		
		btn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				String groupName=getKeyWord();
				if(!Vaild.vaild(groupName)){ 
					JOptionPane.showMessageDialog(renameForm, "A-Z,a-z,0-9 {3,16}","tip",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				UserGroupModifyInfo gm=new UserGroupModifyInfo(Command.MODIFY_GROUP_NAME,
						getContext().getUser().getUserName(),tar.toString() ,groupName);
				
				getContext().getClient().addCommand(gm);
				
				Command cmd=getContext().getClient().getCmdResp();
				if(cmd.getCmd()==Command.SUCCESS){
					
					tar.setUserObject(groupName);
					model.reload();
					renameForm.dispose();
				}else if(cmd.getCmd()==Command.GROUP_REPEATE){
					ClientContext.showMsgBox(renameForm, "GROUP REPEATE", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}else {
					ClientContext.showMsgBox(renameForm, "Rename Fail", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				
			}
			
			
			
		});
	}

	
	
	
	
	

}

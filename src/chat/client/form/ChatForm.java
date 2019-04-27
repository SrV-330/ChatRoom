package chat.client.form;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import chat.client.ClientContext;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;

@SuppressWarnings("serial")
public class ChatForm extends EmptyForm{
	
	private static ChatForm chatForm;
	private JTree tree;
	private DefaultMutableTreeNode root;
	
	private JButton btn_new_group;
	private JButton btn_add_friend;
	
	public static  ChatForm getInstance(ClientContext context){
		if(chatForm==null){
			synchronized (ChatForm.class) {
				if(chatForm==null){
					chatForm=new ChatForm(context);
					return chatForm;
				}
			}
		}
		return chatForm;
	}
	
	private ChatForm(ClientContext context){
		super(context);
		
		init();
		
		
	}
	private void init(){
		
		this.setTitle("ChatForm");
		this.setSize(350,700);
		JPanel jp_content=getContent();
		jp_content.setSize(350,610);
		
		JPanel jp_btn=new JPanel();
		jp_btn.setLayout(null);
		jp_btn.setSize(350,50);
		jp_btn.setLocation(0, 610);
		btn_add_friend=new JButton("Add Friend");
		btn_add_friend.setSize(100,30);
		btn_add_friend.setLocation(10,10);
		
		
		btn_new_group=new JButton("New Group");
		btn_new_group.setSize(100,30);
		btn_new_group.setLocation(10+100+10, 10);
		
		
		jp_btn.add(btn_add_friend);
		jp_btn.add(btn_new_group);
		
		this.add(jp_btn);
		
		initTree();
	}
	private void initTree(){
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(new RootNode());
		//DefaultMutableTreeNode def=new DefaultMutableTreeNode(new DefaultNode());
		//DefaultMutableTreeNode emptyNode=new DefaultMutableTreeNode(new EmptyNode());
		
		//def.add(emptyNode);
		//root.add(def);
		this.root=root;
		
		inflactTree();
		
		
		CustomerRenderer render=new CustomerRenderer();
		
		
		tree=new JTree(root,false);
		tree.setCellRenderer(render);
		tree.setSize(350,700);
		tree.setLocation(0, 0);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		
		
		
		JPanel jp_content=getContent();
		
		jp_content.add(tree);
		
		
		
		
	}
	private void inflactTree(){
		
		FriendGroupInfo friendGroupInfo=getContext().getFriendGroupInfo();
		for(UserGroupInfo g:friendGroupInfo.getGroups()){
			
				DefaultMutableTreeNode node=new DefaultMutableTreeNode(g.getGroupName());
				root.add(node);

				DefaultMutableTreeNode empty=new DefaultMutableTreeNode(new EmptyNode());
				node.add(empty);
			
		}
		for(FriendInfo f:friendGroupInfo.getFriends()){
			
			@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> enums=
					root.breadthFirstEnumeration();
			
			while(enums.hasMoreElements()){
				DefaultMutableTreeNode childNode=enums.nextElement();
				String s=childNode.getUserObject().toString();
				
				if(childNode.getLevel()==1&&s.equals(f.getGroupName())){
					DefaultMutableTreeNode node=
							new DefaultMutableTreeNode(f.getFriendName());
					DefaultMutableTreeNode firstNode=childNode.getFirstLeaf();
					if(firstNode.getUserObject().toString().equals(new EmptyNode().toString())){
						childNode.remove(firstNode);
					}
					
					childNode.add(node);
				}
			}
			
			
		}
	}
	public void showForm(){
		this.setVisible(true);
	}
	
	class  CustomerRenderer extends DefaultTreeCellRenderer{

		public CustomerRenderer() {
			super();
			
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			
			setText(value.toString());
			
			if(sel){
				setForeground(getTextSelectionColor());
			}else{
				setForeground(getTextNonSelectionColor());
			}
			
			
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
			if(node.toString().equals(EmptyNode.empty)){
				this.setIcon(null);
				return this;
			}
			if(node.getLevel()==1){
				this.setIcon(new ImageIcon("icon/group.jpg"));
			}else if(node.getLevel()==2){
				this.setIcon(new ImageIcon("icon/friend.jpg"));
			}
			
			return this;
			
		}
		
		
	}
	
	
	class EmptyNode {
		public final static String empty="(empty)";

		@Override
		public String toString() {
			return empty;
		}
		
	}
	class RootNode{
		public final static String root="Root";

		@Override
		public String toString() {
			return root;
		}
	}
	class DefaultNode{
		public final static String _default="Default";

		@Override
		public String toString() {
			return _default;
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		FriendGroupInfo fg=new FriendGroupInfo();
		List<UserGroupInfo> l=new ArrayList<>();
		l.add(new UserGroupInfo("A", "Friends"));
		List<FriendInfo> l1=new ArrayList<>();
		l1.add(new FriendInfo("A","B","Friends"));
		fg.setGroups(l);
		fg.setFriends(l1);
		ClientContext context=new ClientContext();
		context.setFriendGroupInfo(fg);
		ChatForm.getInstance(context).showForm();
	}

	
	
	
	

}

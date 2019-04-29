package chat.client.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import chat.client.ClientContext;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;

@SuppressWarnings("serial")
public class ChatForm extends EmptyForm{
	
	private static ChatForm chatForm;
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode _default;
	private DefaultTreeModel model; 
	private JButton btn_new_group;
	private JButton btn_add_friend;
	private JPopupMenu menu;
	
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
		
		
		btn_add_friend.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				AddFriendForm addFriendForm=AddFriendForm.getInstance(getContext());
				addFriendForm.showForm();
			}
			
			
		});
		
		btn_new_group=new JButton("New Group");
		btn_new_group.setSize(100,30);
		btn_new_group.setLocation(10+100+10, 10);
		
		
		btn_new_group.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				
				
				NewGroupForm newGroupForm=NewGroupForm.getInstance(getContext());
				newGroupForm.showForm();
				
			}
			
		});
		
		jp_btn.add(btn_add_friend);
		jp_btn.add(btn_new_group);
		
		this.add(jp_btn);
		
		initTree();
	}
	private void initTree(){
		
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(new RootNode());
		this.root=root;
		tree=new JTree();
		
		
		inflactTree();
		
		CustomerRenderer render=new CustomerRenderer();
		tree.setModel(model);
		tree.setCellRenderer(render);
		tree.setSize(350,700);
		tree.setLocation(0, 0);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		
		
		addMenu();
		
		JScrollPane jsp=new JScrollPane();
		jsp.setLayout(null);
		jsp.setSize(350,700);
		jsp.setLocation(0, 0);
		jsp.add(tree);
		
		
		JPanel jp_content=getContent();
		
		jp_content.add(jsp);
		
	}
	private void inflactTree(){
		model=new DefaultTreeModel(root);
		FriendGroupInfo friendGroupInfo=getContext().getFriendGroupInfo();
		for(UserGroupInfo g:friendGroupInfo.getGroups()){
			addGroup(g);
		}
		for(FriendInfo f:friendGroupInfo.getFriends()){
			addFriend(f);
		}
	}
	public void addGroup(UserGroupInfo group){
		
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(group.getGroupName());
		
		if(node.toString().equals(DefaultNode._default)){
			_default=node;
			
		}
		
		model.insertNodeInto(node, root, 0);

//		DefaultMutableTreeNode empty=new DefaultMutableTreeNode(new EmptyNode());
//		node.add(empty);
		model.reload();
	}
	public void addFriend(FriendInfo friend){
		
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> enums=
				root.breadthFirstEnumeration();
		
		while(enums.hasMoreElements()){
			DefaultMutableTreeNode childNode=enums.nextElement();
			String s=childNode.getUserObject().toString();

			if(childNode.getLevel()==1&&s.equals(friend.getGroupName())){
				DefaultMutableTreeNode node=
						new DefaultMutableTreeNode(friend.getFriendName());
//				DefaultMutableTreeNode firstNode=childNode.getFirstLeaf();
//				if(firstNode.getUserObject().toString().equals(EmptyNode.empty)){
//					
//					model.removeNodeFromParent(firstNode);
//				}
				
				
				model.insertNodeInto(node, childNode, 0);
			}
		}
		model.reload();
	}
	
	
	private void addMenu(){
		JPopupMenu menu=new JPopupMenu();
		this.menu=menu;
		addItem(menu,"remove",new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				if(selNode.toString().equals(DefaultNode._default)){
					ClientContext.showMsgBox(tree, "Default Group can not remove", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				//TODO
				
				model.removeNodeFromParent(selNode);
				model.reload();
				
			}
		});
		
		addItem(menu, "rename", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
				if(selNode.toString().equals(DefaultNode._default)){
					ClientContext.showMsgBox(tree, "Default Group can not rename", "tip", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				//TODO
				
				
				model.reload();
				
			}
		});
		
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selNode=
						(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				if(selNode==null) return;
				System.out.println("SelectNode: "+selNode);
				
				if(selNode.toString().equals(DefaultNode._default)){
					ChatForm.this.menu.getComponent(0).setEnabled(false);
					ChatForm.this.menu.getComponent(1).setEnabled(false);
				}else{
					ChatForm.this.menu.getComponent(0).setEnabled(true);
					ChatForm.this.menu.getComponent(1).setEnabled(true);
				}
				
			}
		});
		
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				super.mouseClicked(e);
				
				int x=e.getX();
				int y=e.getY();
				if(e.getButton()==MouseEvent.BUTTON3){
					TreePath path=tree.getPathForLocation(x, y);
					tree.setSelectionPath(path);
					menu.show(tree, x, y);
				}
			}
			
			
			
		});
		
	}
	private void addItem(JPopupMenu menu,String itemName,ActionListener listener){
		JMenuItem item=new JMenuItem(itemName);
		menu.add(item);
		item.addActionListener(listener);
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
		l.add(new UserGroupInfo("A","Default"));
		List<FriendInfo> l1=new ArrayList<>();
		l1.add(new FriendInfo("A","B","Friends"));
		fg.setGroups(l);
		fg.setFriends(l1);
		ClientContext context=new ClientContext();
		context.setFriendGroupInfo(fg);
		ChatForm.getInstance(context).showForm();
	}

	
	
	
	

}

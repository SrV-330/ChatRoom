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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import chat.client.ClientContext;
import chat.server.entity.Command;
import chat.server.entity.FriendGroupInfo;
import chat.server.entity.FriendInfo;
import chat.server.entity.UserGroupInfo;
import chat.server.entity.UserGroupModifyInfo;
import chat.server.entity.UserInfo;
import chat.server.util.json.User;

@SuppressWarnings("serial")
public class ChatForm extends EmptyForm{
	
	private static ChatForm chatForm;
	private JTree tree;
	private DefaultMutableTreeNode root;
	@SuppressWarnings("unused")
	private DefaultMutableTreeNode _default;
	private DefaultTreeModel model; 
	private JButton btn_new_group;
	private JButton btn_add_friend;
	private JButton btn_exit;
	private JPopupMenu popMenu; 
	
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
		
		btn_exit=new JButton("Exit");
		btn_exit.setSize(100,30);
		btn_exit.setLocation(10+100+10+100+10, 10);
		btn_exit.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				chatForm.dispose();
			}
			
		});
		
		
		jp_btn.add(btn_add_friend);
		jp_btn.add(btn_new_group);
		jp_btn.add(btn_exit);
		
		this.add(jp_btn);
		
		initTree();
		
		
		JScrollPane jsp=new JScrollPane();
		jsp.setLayout(null);
		jsp.setSize(350,700);
		jsp.setLocation(0, 0);
		jsp.add(tree);

		jp_content.add(jsp);
	}
	private void initTree(){
		
		root=new DefaultMutableTreeNode(new RootNode());
		tree=new JTree();
		
		
		inflactTree();
		
		CustomerRenderer render=new CustomerRenderer();
		tree.setModel(model);
		tree.setCellRenderer(render);
		tree.setSize(350,700);
		tree.setLocation(0, 0);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);

		addPopMenu();
		
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
	
	interface NodeHandler {
		void execute(DefaultMutableTreeNode childNode);
	}
	
	@SuppressWarnings("unchecked")
	private void forEachNode(NodeHandler handler){
		
		Enumeration<DefaultMutableTreeNode> enums=
				root.breadthFirstEnumeration();
		
		while(enums.hasMoreElements()){
			DefaultMutableTreeNode childNode=enums.nextElement();
			handler.execute(childNode);
		}
		
		
	}
	public void addGroup(UserGroupInfo group){
		
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(group.getGroupName());
		
		if(node.toString().equals(DefaultNode._default)){
			_default=node;
			
		}
		
		model.insertNodeInto(node, root, 0);

		model.reload();
	}
	public void addFriend(FriendInfo friend){
		
		forEachNode(new NodeHandler() {
			
			@Override
			public void execute(DefaultMutableTreeNode childNode) {
				String s=childNode.getUserObject().toString();
				if(childNode.getLevel()==1&&s.equals(friend.getGroupName())){
					DefaultMutableTreeNode node=
							new DefaultMutableTreeNode(friend.getFriendName());
					
					
					model.insertNodeInto(node, childNode, 0);
				}
				
			}
		});
		
		
		model.reload();
	}
	
	
	private void addPopMenu(){
		popMenu=new JPopupMenu();
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				
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
					System.out.println("mouse");
					DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)path.getPathComponent(path.getPathCount()-1);
					
					addPopMenuItem(selNode);
					popMenu.show(tree, x, y);
				}else if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==2){
					TreePath path=tree.getPathForLocation(x, y);
					tree.setSelectionPath(path);
					System.out.println("mouse");
					DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)path.getPathComponent(path.getPathCount()-1);
					if(selNode.getLevel()==2){
						SingleForm form=SingleForm.getInstance(getContext(),selNode.toString());
						getContext().putSingleForm(selNode.toString(), form);
						form.showForm();
					}
					
				}
			}
			
			
			
		});
		
	}
	
	
	private void addPopMenuItem(DefaultMutableTreeNode selNode){
		popMenu.removeAll();
		boolean enable=!(selNode.getLevel()==1&&selNode.toString().equals(DefaultNode._default));
		if(selNode.getLevel()==1){
			
			
			addGroupPopItem(popMenu, "remove", new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
					
					
					Command cmd=new UserGroupInfo(Command.REMOVE_GROUP,
							getContext().getUser().getUserName(), selNode.toString());
					
					getContext().getClient().addCommand(cmd);
					
					cmd=getContext().getClient().getCmdResp();
					if(cmd.getCmd()==Command.SUCCESS){
						model.removeNodeFromParent(selNode);
					}else if(cmd.getCmd()==Command.GROUP_REPEATE){
						ClientContext.showMsgBox(chatForm, "GROUP REPEATE","tip", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(cmd.getCmd()==Command.FAIL){
						ClientContext.showMsgBox(chatForm, "NEW GROUP FAIL","tip", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					
					model.reload();
					
				}
			}).setEnabled(enable);
			
			
			
			addGroupPopItem(popMenu, "rename", new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
					RenameForm.getInstance(getContext(), selNode, model).showForm();
					
					
				}
			}).setEnabled(enable);
			
			
		}else if(selNode.getLevel()==2){
			addFriendPopItem(popMenu, "remove", new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
					
					
					Command cmd=new FriendInfo(Command.REMOVE_FRIEND,
							getContext().getUser().getUserName(),
							selNode.toString());
					
					getContext().getClient().addCommand(cmd);
					cmd=getContext().getClient().getCmdResp();
					if(cmd.getCmd()==Command.SUCCESS){
						model.removeNodeFromParent(selNode);
						
					}else if(cmd.getCmd()==Command.NOT_FRIEND){
						ClientContext.showMsgBox(chatForm, "Not Friend", "tip", JOptionPane.ERROR_MESSAGE);
						model.removeNodeFromParent(selNode);
						
					}else if(cmd.getCmd()==Command.NO_THE_USER){
						ClientContext.showMsgBox(chatForm, "No the User", "tip", JOptionPane.ERROR_MESSAGE);
						model.removeNodeFromParent(selNode);
					}else{
						ClientContext.showMsgBox(chatForm, "Remove fail", "tip", JOptionPane.ERROR_MESSAGE);
					}
					model.reload();
					
					
				}
			});
			
			
			JMenu moveTo=addChildMenu(popMenu, "moveTo", new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			List<DefaultMutableTreeNode> nodes=new ArrayList<>();
			forEachNode(new ChatForm.NodeHandler() {
				
				@Override
				public void execute(DefaultMutableTreeNode childNode) {
					
					if(childNode.getLevel()==1&&!childNode.toString().equals(selNode.getParent().toString())){
						
						nodes.add(childNode);
					}
					
				}
			});
			
			
			for(DefaultMutableTreeNode node:nodes){
				
				addChildPopItem(moveTo, node.toString(), new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JMenuItem it=(JMenuItem)e.getSource();
						DefaultMutableTreeNode selNode=(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
						DefaultMutableTreeNode pNode =(DefaultMutableTreeNode)selNode.getParent();
						
						System.out.println(selNode);
						for(DefaultMutableTreeNode n:nodes){
							if(it.getText().equals(n.toString())){
								
								Command cmd=new UserGroupModifyInfo(Command.MOVE_TO_GROUP, getContext().getUser().getUserName(),
										pNode.toString(), n.toString());
								
								getContext().getClient().addCommand(cmd);
								cmd=getContext().getClient().getCmdResp();
								if(cmd.getCmd()==Command.SUCCESS){
									pNode.remove(selNode);
									n.add(selNode);
									model.reload();
								}else{
									ClientContext.showMsgBox(chatForm, "Move Fail", "tip", JOptionPane.ERROR_MESSAGE);
								}
								return;
								
							}
						}
					}
				});
			}
			
		}else{
			return;
		}
	}
	private JMenuItem addChildPopItem(JMenuItem menu,String itemName,ActionListener listener){
		JMenuItem item=new JMenuItem(itemName);
		menu.add(item);
		item.addActionListener(listener);
		return item;
	}
	private JMenu addChildMenu(JPopupMenu menu,String itemName,ActionListener listener){
		JMenu item=new JMenu(itemName);
		menu.add(item);
		item.addActionListener(listener);
		return item;
	}
	private JMenuItem addFriendPopItem(JPopupMenu menu,String itemName,ActionListener listener){
		JMenuItem item=new JMenuItem(itemName);
		menu.add(item);
		item.addActionListener(listener);
		return item;
	}
	private JMenuItem addGroupPopItem(JPopupMenu menu,String itemName,ActionListener listener){
		JMenuItem item=new JMenuItem(itemName);
		menu.add(item);
		item.addActionListener(listener);
		return item;
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

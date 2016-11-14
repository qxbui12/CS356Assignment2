import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ControlPanel implements ActionListener, KeyListener {

	private static ControlPanel adminInstance = null;
	private HashMap<String, TwitterUser> userMap;
	private HashMap<String, UserGroup> groupMap;
	private JTree tree;
	private DefaultTreeModel modelTree;
	private DefaultMutableTreeNode root, selectedNode, temp;
	private JFrame frame;
	private JPanel treePanel, buttonPanel,  inTop, inMid, inBottom;
	private JScrollPane treePane;
	private JButton addUser, addGroup,userTotal, groupTotal, messageTotal, positiveMessage,userView;
	private JTextField userName, groupName;
	private JLabel helpTxt;
	private GridBagConstraints c;

	private ControlPanel() {
		userMap = new HashMap<String, TwitterUser>();
		groupMap = new HashMap<String, UserGroup>();
		generateUI();
	}

	public static ControlPanel run() {
		if (adminInstance == null)
			adminInstance = new ControlPanel();
		return adminInstance;
	}

	public HashMap<String, TwitterUser> getUserMap() {
		return userMap;
	}		

	public void addUser(String name) {
		if (name.length() < 2) {
			helpTxt.setText("Please input more than 2 characters.");
		} else if (userMap.get(name)==null) {
			userMap.put(name, new TwitterUser(name));
			temp = new DefaultMutableTreeNode(userMap.get(name));
			temp.setAllowsChildren(false);
			try {
				selectedNode.add(temp);
				((UserGroup)selectedNode.getUserObject()).add(userMap.get(name));;
			} catch (NullPointerException e) {
				root.add(temp);
				((UserGroup)root.getUserObject()).add(userMap.get(name));;
				modelTree.reload(root);
			}
			modelTree.reload(selectedNode);
			helpTxt.setText("Welcome " + name + " to mini Twitter");
		} else
			helpTxt.setText(name + " is taken.");
	}
 
	public void addGroup(String name) {
		if (name.length() < 2) {
			helpTxt.setText("Please input more than 2 characters.");
		} else if (groupMap.get(name)==null) {
			groupMap.put(name, new UserGroup(name));
			temp = new DefaultMutableTreeNode(groupMap.get(name));
			try {
				selectedNode.add(temp);
				((UserGroup)selectedNode.getUserObject()).add(groupMap.get(name));;
			} catch (NullPointerException e){
				root.add(temp);
				((UserGroup)root.getUserObject()).add(groupMap.get(name));;
				modelTree.reload(root);
			}
			modelTree.reload(selectedNode);
			helpTxt.setText("Welcome group " + name + " to mini Twitter");
		} else
			helpTxt.setText("Group " + name + " is taken.");
	}
	
	public void actionPerformed(ActionEvent a) {
		switch (a.getActionCommand()) {

		case "Add User":
			if (selectedNode == null || !(selectedNode.getUserObject() instanceof TwitterUser))
				addUser(userName.getText().toLowerCase());
			break;

		case "Add Group":
			if (selectedNode == null || !(selectedNode.getUserObject() instanceof TwitterUser))
				addGroup(groupName.getText().toLowerCase());
			break;  

		case "Open user view":
			if (selectedNode != null && (selectedNode.getUserObject() instanceof TwitterUser))
				openView(selectedNode);
			else
				helpTxt.setText("Please click on a user name.");
			break;

		case "Show user total":
			UserCounter uv = new UserCounter();
			((Element)root.getUserObject()).accept(uv);
			helpTxt.setText("Total # of users: " + Integer.toString(uv.getCounter()));
			break;

		case "Show group total":
			GroupMsgCounter gv = new GroupMsgCounter();
			((Element)root.getUserObject()).accept(gv);
			helpTxt.setText("Total # of groups: " + Integer.toString(gv.getCounter()));
			break;

		case "Show message total":
			UserMsgCounter mv = new UserMsgCounter();
			((Element)root.getUserObject()).accept(mv);
			helpTxt.setText("Total # of messages: " + Integer.toString(mv.getMessageCounter()));
			break;

		case "Show positive messages":
			PositiveCounter pv = new PositiveCounter();
			((Element)root.getUserObject()).accept(pv);
			helpTxt.setText("Number of positive messages: " + Double.toString(pv.getGoodCounter()));
			break;

		default:
			break;
		}
	}

	public void openView(DefaultMutableTreeNode n) {
		((TwitterUser) n.getUserObject()).buildGUI();
		helpTxt.setText(n.toString() + "'s user view opened.");
	}
	
	//Admin UI
	public void generateUI() {
		createJFrame();
		createJPanels();
		createJButtons();
		createJTree();
		createJTextField();
		createJLabels();
		createGridBagLayout();
		displayJFrame();
	}
	public void createJFrame(){
		frame = new JFrame("Control Panel");
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridBagLayout());
	}
	public void createJPanels(){
		treePanel = new JPanel();
		buttonPanel = new JPanel();
		inTop = new JPanel();
		inMid = new JPanel();
		inBottom = new JPanel();
		treePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		treePanel.setLayout(new GridLayout());
		buttonPanel.setLayout(new GridBagLayout());
		inTop.setLayout(new GridBagLayout());
		inMid.setLayout(new GridBagLayout());
		inBottom.setLayout(new GridBagLayout());
	}
	public void createJButtons(){
		addUser = new JButton("Add User");
		addUser.addActionListener(this);
		addGroup= new JButton("Add Group");
		addGroup.addActionListener(this);
		userTotal = new JButton("Show user total");
		userTotal.addActionListener(this);
		groupTotal = new JButton("Show group total");
		groupTotal.addActionListener(this);
		messageTotal = new JButton("Show message total");
		messageTotal.addActionListener(this);
		positiveMessage = new JButton("Show positive messages");
		positiveMessage.addActionListener(this);
		userView = new JButton("Open user view");
		userView.addActionListener(this);
	}
	public void createJTree(){
		root = new DefaultMutableTreeNode(new UserGroup("root"));
		modelTree = new DefaultTreeModel(root);
		tree = new JTree(modelTree);
		tree.setCellRenderer(new CustomRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			}
		});
		
		treePane = new JScrollPane(tree);
		treePanel.add(treePane);
	}
	public void createJTextField(){
		userName = new JTextField("default");
		userName.addKeyListener(this);
		groupName = new JTextField("default");
		groupName.addKeyListener(this);
	}
	
	public void createJLabels(){
		helpTxt = new JLabel("Hello.");
		helpTxt.setHorizontalAlignment(SwingConstants.CENTER);
	}
	public void createGridBagLayout(){
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(treePanel, c);

		c.gridx = 1;
		c.weightx = 0.6;
		frame.getContentPane().add(buttonPanel, c);

		c.weighty = 0.2;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		buttonPanel.add(inTop, c);

		c.weighty = 0.1;
		c.gridy = 2;
		buttonPanel.add(inBottom, c);

		c.weighty = 0.7;
		c.gridy = 1;
		buttonPanel.add(inMid, c);

		c.insets = new Insets(1, 1, 1, 2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.6;
		c.weighty = 0.5;
		inTop.add(userName, c);
		inBottom.add(userTotal, c);

		c.gridx = 1;
		c.weightx = 0.4;
		inTop.add(addUser, c);
		inBottom.add(groupTotal, c);

		c.gridy = 1;
		c.weighty = 0.5;
		inTop.add(addGroup, c);
		inBottom.add(positiveMessage, c);

		c.gridx = 0;
		c.weightx = 0.5;
		inTop.add(groupName, c);
		inBottom.add(messageTotal, c);

		c.gridy = 0;
		c.weightx = 0.5;
		c.weighty = 0.1;
		inMid.add(userView, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.3;
		c.weightx = 0.6;
		inMid.add(helpTxt, c);
	}
	public void displayJFrame(){
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	private static class CustomRenderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
				if (n.getUserObject() instanceof UserGroup) {
					setIcon(UIManager.getIcon("Tree.closedIcon"));
				}
			}
			return this;
		}
	}

	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			if(userName.hasFocus()){
				if (selectedNode == null || !(selectedNode.getUserObject() instanceof TwitterUser)){
					addUser(userName.getText().toLowerCase());
				}
			}
			if(groupName.hasFocus()){
				if (selectedNode == null || !(selectedNode.getUserObject() instanceof TwitterUser))
					addGroup(groupName.getText().toLowerCase());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

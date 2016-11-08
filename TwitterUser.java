import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class TwitterUser implements User, Element,ActionListener{

	private String id, tweetMsg;
	private long startTime , updateTime;
	private ArrayList<User> subscribers= new ArrayList<User>();
	private DefaultListModel<String> subscriptions = new DefaultListModel<String>();
	private DefaultListModel<String> newsFeed= new DefaultListModel<String>();
	private JList<String> userList, messageList;
	private JFrame frame;
	private JPanel topPanel, bottomPanel;
	private JTextField userID, messageField;
	private JButton follow, tweet;
	private JScrollPane userPane, newsFeedPane;
	private GridBagConstraints c;

	//constructor
	public TwitterUser(String name) {
		this.setID(name);
	}

	@Override
	public ArrayList<User> getSubscribers() {
		return subscribers;
	}

	@Override
	public void subscribe(User target) {
		target.getSubscribers().add(this);
		this.getSubscriptions().addElement(target.getID());
		this.update(target);
		if (userList != null)
			userList.setModel(subscriptions);
	}

	@Override
	public void tweet(String msg) {
		setTweetMsg(msg);
		update(this);
		notifySubscribers();
	}

	@Override
	public void notifySubscribers() {
		for (User subscriber: subscribers) {
			subscriber.update(this);
		}
	}

	@Override
	public void update(User user) {
		if (messageList != null)
			messageList.setModel(newsFeed);
		setLastUpdateTime(System.currentTimeMillis());
		newsFeed.add(0,"-   "+ user.getID() + ": " + user.getTweetMsg());

	}

	@Override
	public String getTweetMsg() {
		return tweetMsg;
	}

	@Override
	public void accept(VisitorElement v) {
		v.visitTwitterUser(this);
	}

	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public void setTweetMsg(String tweetMsg) {
		this.tweetMsg = tweetMsg;
	}

	public DefaultListModel<String> getNewsFeed() {
		return newsFeed;
	}

	public DefaultListModel<String> getSubscriptions() {
		return subscriptions;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.updateTime = lastUpdateTime;
	}

	public String toString() {
		return getID();
	}

	//Creating the GUI
	public JFrame buildGUI() {
		frame = new JFrame(getID() + " 's User View");
		frame.setPreferredSize(new Dimension(400, 450));
		frame.getContentPane().setLayout(new GridLayout(2, 1, 3, 4));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//JPanels
		topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());

		//create 2 user input fields
		userID = new JTextField(10);
		messageField = new JTextField(10);

		//Buttons
		follow = new JButton("Follow user");
		follow.addActionListener(this);
		tweet = new JButton("Tweet");
		tweet.addActionListener(this);

		//Follow list and newsFeed list
		userList = new JList<String>(subscriptions);
		messageList = new JList<String>(newsFeed);

		//List Scroll
		userPane = new JScrollPane(userList);
		newsFeedPane = new JScrollPane(messageList);

		//GridBagLayout
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		topPanel.add(userID, c);
		bottomPanel.add(messageField, c);

		c.gridx = 1;
		topPanel.add(follow, c);
		bottomPanel.add(tweet, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		topPanel.add(userPane, c);
		bottomPanel.add(newsFeedPane, c);

		frame.getContentPane().add(topPanel);
		frame.getContentPane().add(bottomPanel);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Follow user")) {
			if (!subscriptions.contains(userID.getText()) && ControlPanel.run().getUserMap().get(userID.getText().toLowerCase()) != null) {
				subscribe(ControlPanel.run().getUserMap().get(userID.getText().toLowerCase()));
			}
		} else if (e.getActionCommand().equals("Tweet")) {
			tweet(messageField.getText());
		} else {
			return;
		}
	}


}
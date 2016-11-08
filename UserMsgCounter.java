public class UserMsgCounter implements VisitorElement {

	private int messageCounter = 0;

	public UserMsgCounter() {
		setMessageCounter(0);
	}
	
	public void setMessageCounter(int messageCounter) {
		this.messageCounter = messageCounter;
	}
	public int getMessageCounter() {
		return messageCounter;
	}

	public void visitTwitterUser(TwitterUser u) {
		this.setMessageCounter(getMessageCounter()+u.getNewsFeed().size());
	}

	public void visitGroup(UserGroup g) {
		return;
	}

}
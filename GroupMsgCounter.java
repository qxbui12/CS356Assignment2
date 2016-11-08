
public class GroupMsgCounter implements VisitorElement {

	private int counter;

	public GroupMsgCounter() {
		setCounter(0);
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter() {
		return counter;
	}

	@Override
	public void visitTwitterUser(TwitterUser u) {
		return;
	}

	public void visitGroup(UserGroup g) {
		this.setCounter(getCounter()+1);
	}

}

public class UserCounter implements VisitorElement {

	private int counter = 0;

	public UserCounter() {
		setCounter(0);
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public void visitTwitterUser(TwitterUser u) {
		this.setCounter(getCounter()+1);
	}

	@Override
	public void visitGroup(UserGroup g) {
		return;
	}

}
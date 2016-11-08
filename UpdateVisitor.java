public class UpdateVisitor implements VisitorElement {

	String id;
	long updateTime;
	
	public UpdateVisitor() {
		setId(null);
		setUpdateTime(0);
	}
	
	@Override
	public void visitTwitterUser(TwitterUser u) {
		if(u.getUpdateTime() > getUpdateTime()) {
			setUpdateTime(u.getUpdateTime());
			setId(u.getID());
		}
	}

	@Override
	public void visitGroup(UserGroup g) {
		// TODO Auto-generated method stub

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
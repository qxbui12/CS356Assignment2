public class PositiveCounter implements VisitorElement {

	private int goodCounter;
	private static String[] positiveWords = {"ok", "good", "great", "excellent", "amazing", "cool", "nice", "awesome"};
	public PositiveCounter() {
		setGoodCounter(0);
	}
	public void setGoodCounter(int goodCounter) {
		this.goodCounter = goodCounter;
	}

	public int getGoodCounter() {
		return goodCounter;
	}

	public void visitTwitterUser(TwitterUser u) {
		for(Object o: u.getNewsFeed().toArray()) {
			for(String posW: positiveWords){
				if(o.toString().toLowerCase().contains(posW)){
					setGoodCounter(getGoodCounter()+1);
				}
			}
		}
	}

	public void visitGroup(UserGroup g) {
		return;
	}

}
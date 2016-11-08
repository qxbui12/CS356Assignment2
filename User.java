import java.util.List;

public interface User {

	public String getID();

	public String getTweetMsg();

	public void subscribe(User u);

	public void tweet(String msg);

	public void notifySubscribers();

	public void update(User u);

	public List<User> getSubscribers();

}
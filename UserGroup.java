import java.util.Vector;

public class UserGroup implements Element{
	private String groupID;
	private Vector<Element> children;

	public UserGroup(String groupID) {
		this.setGroupID(groupID);
		children = new Vector<Element>();
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public void add(Element element) {
		if (!children.contains(element))
			children.add(element);
	}

	public String toString() {
		return this.groupID;
	}

	@Override
	public void accept(VisitorElement v) {
		v.visitGroup(this);
		for(Element element: children) {
			element.accept(v);
		}
	}
}
package module1_navigation;

import java.awt.Point;
import java.util.ArrayList;

import aStar.Node;
import aStar.State;

public class NavState extends State {

	private Point location;
	
	public NavState(Point location) {
		this.location = location;
	}
	
	public NavState(NavState parent, Point location) {
		this.location = location;
	}
	
	public Object generateId() {
		this.setId(Integer.parseInt((location.x+1000)+""+(location.y+1000)));
		return getId();
	}
	
	public Point getLocation() {
		return location;
	}
}

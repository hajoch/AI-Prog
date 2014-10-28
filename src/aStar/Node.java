package aStar;

import java.util.ArrayList;

public class Node implements Comparable<Node>{
	
	public enum Status {
		OPEN, CLOSED, NONE
	}
	
	//An object describing a state of the search process
	private State state;
	//Cost of getting to this node
	private float g;
	//Estimated cost to goal
	private float h;
	//Estimated total cost of a solution path going through this node f = g + h
	private float f;	
	//Open or closed
	private Status status = Status.NONE;
	//Best parent node
	private Node parent;
	// list of all successor nodes, whether or not this node is currently their best parent
	private ArrayList<Node> successors = new ArrayList<Node>();
	
	public Node(State state, float g) {
		this.state = state;
	}
		
	
	public ArrayList<Node> getSuccessors() {
		return successors;
	}
	
	
	public void addSuccessor(Node successor) {
		if(successors.contains(successor)) {
			return;
		}
		successors.add(successor);
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}
	
	public void setH(float h) {
		this.h = h;
	}

	public float getH() {
		return h;
	}

	public void setF(float f) {
		this.f = f;
	}
	public void setF() {
		setF(g+h);
	}
	
	public float getF() {
		return f;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Node getBestParent() {
		return parent;
	}

	public void setBestParent(Node parent) {
		this.parent = parent;
	}
	
	@Override
	public int compareTo(Node otherNode) {
		final int WORSE = -1;
		final int EQUAL = 0;
		final int BETTER = 1;
		
		if(this.getF() == otherNode.getF()) return EQUAL;
		if(this.getF() < otherNode.getF()) 	return WORSE;
		else 								return BETTER;
	}
}

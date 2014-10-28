package aStar;

import java.util.ArrayList;

public interface Problem {
	
	public float getArchCost(Node parent, Node successors);
	
	public ArrayList<Node> generateSuccessorNodes(Node node);
	
	public Node getStartNode();
	
	public boolean isSolution(Node node);
	
	public float heuristics(Node node);

}

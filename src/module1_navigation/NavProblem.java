package module1_navigation;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import aStar.Node;
import aStar.Problem;

public class NavProblem implements Problem {
	
	private final float ARCHCOST = 5;
	
	private boolean[][] obstacle;
	private Node[][] grid;
	
	public final Point start;
	public final Point goal;
	public final Dimension size;
	
	public NavProblem(Dimension size, Point start, Point goal, ArrayList<int[]> obstacles) {
		this.size = size;
		this.start = start;
		this.goal = goal;
		
		obstacle = new boolean[size.height][size.width];
		grid = new Node[size.height][size.width];
		
		grid[start.x][start.y] = new Node(new NavState(start), 0);
		
		createObstacles(obstacles);
	}
	
	public NavProblem(Dimension size, Point start, Point goal, boolean[][] obstacles) {
		this.size = size;
		this.start = start;
		this.goal = goal;
		
		this.obstacle = obstacles;
		
		grid = new Node[size.height][size.width];
		grid[start.x][start.y] = new Node(new NavState(start), 0);
		
	}
	
	private void createObstacles(ArrayList<int[]> obstacles) {
		for(int[] o : obstacles) {
			if(o==null)
				return;
			
			int relX = o[0];
			int relY = o[1];
			int width = o[2];
			int height = o[3];
			
			for(int y=relY; y<relY+height; y++) {
				for(int x=relX; x<relX+width; x++) {
					obstacle[y][x] = true;
				}
			}
		}
	}
	
	public boolean[][] testGrid() {
		return obstacle;
	}

	@Override
	public float getArchCost(Node parent, Node successor) {
		return ARCHCOST;
	}

	@Override
	public ArrayList<Node> generateSuccessorNodes(Node node) {
		
		ArrayList<Node> successors = new ArrayList<Node>();
		
		NavState state = (NavState)node.getState();
		Point p = state.getLocation();
		
		for(int i=-1; i<2; i+=2) {
			if(p.x+i <size.width && p.x+i >= 0) {
				if(!obstacle[p.y][p.x+i]) {
					Node xNode = grid[p.y][p.x+i];
					if(xNode == null)
						xNode = new Node(new NavState(state, new Point(p.x+i, p.y)), node.getG()+getArchCost(node, xNode));
					xNode.setH(heuristics(xNode));
					xNode.setF(xNode.getG()+xNode.getH());
					successors.add(xNode);
				}
			}
			
			if(p.y+i <size.height && p.y+i >= 0) {
				if(!obstacle[p.y+i][p.x]) {
					Node yNode = grid[p.y+i][p.x];
					if(yNode == null)
						yNode = new Node(new NavState(state, new Point(p.x, p.y+i)), node.getG()+getArchCost(node, yNode));
					yNode.setH(heuristics(yNode));
					yNode.setF(yNode.getG()+yNode.getH());
					successors.add(yNode);
				}
			}
		}
		return successors;
	}

	@Override
	public Node getStartNode() {
		return grid[start.x][start.y];
	}

	@Override
	public boolean isSolution(Node node) {
		Point loc = ((NavState) node.getState()).getLocation();
		return loc.equals(goal);
	}

	@Override
	public float heuristics(Node node) {
		NavState ns = (NavState)node.getState();
		Point loc = ns.getLocation();
		return ( Math.abs(goal.x - loc.x) + Math.abs(goal.y - loc.y) );
	}

	public boolean[][] getObstacles() {
		return obstacle;
	}
}

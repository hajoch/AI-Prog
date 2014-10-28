package module2_vertexColoring;

import java.awt.Color;

public class Edge {
	
	public int id1;
	public int id2;
	
	public int id;
	
	public double cost;
	public Color color = Color.BLACK;
	
	public Edge(int id1, int id2) {
		this.id1 = id1;
		this.id2 = id2;
		id = Integer.parseInt((id1+1000)+""+(id2+1000));
	}
	
	@Override
	public String toString() {
		return "Edge "+id1+" : "+id2;
	}
}

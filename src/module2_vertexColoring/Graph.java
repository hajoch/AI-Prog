package module2_vertexColoring;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

public class Graph extends JPanel{
	
	HashMap<Integer, Graphics> guiVertices;
	HashMap<Integer, Graphics> guiEdges;
	HashMap<Integer, Vertex> test = new HashMap<Integer, Vertex>();
	
	VCProblem problem;
	VCState state;
	Dimension d;
	
	public boolean done = false;
	
	private final int SIZE = 13;
	private final double DIM;
	
	public Graph(VCProblem problem, Dimension d) {
		this.problem = problem;
		this.d = d;
		setSize(800, 800);
		
		double big = 0;
		for(Vertex v: problem.vertices) {
			if(v.x > big)
				big = v.x;
			if(v.y > big)
				big = v.y;
			
		}
		DIM = 800/big;
	}
	
	public void reDraw(VCState state) {
		this.state = state;
		update(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		if(done)
			return;
		
		Graphics2D gr = (Graphics2D)g;
		gr.setBackground(Color.WHITE);
		
		//reset
		g.clearRect(0, 0, getWidth(), getHeight());
		
		gr.setColor(Color.GRAY);
		
		HashSet<Vertex> vertss;
		if(state == null)
			vertss = problem.vertices;
		else
			vertss = (HashSet<Vertex>)state.getVariables();
		
		for(Vertex v : vertss) {
			test.put(v.ID, v);
			int x = (int)(v.x*DIM);
			int y = (int)(v.y*DIM);
			gr.drawOval(x,y, SIZE, SIZE);
/*			gr.setColor(Color.GRAY);
			if(v.isDomainSingleton())
				gr.setColor((Color)v.getDomain().get(0));
*/			gr.setColor(v.color);
			gr.fillOval(x, y, SIZE, SIZE);
		}
		for(Edge e : problem.edges) {
			int x1 = (int)(test.get(e.id1).x	*DIM)+SIZE/2;
			int y1 = (int)(test.get(e.id1).y	*DIM)+SIZE/2;
			int x2 = (int)(test.get(e.id2).x	*DIM)+SIZE/2;
			int y2 = (int)(test.get(e.id2).y	*DIM)+SIZE/2;
			gr.setColor(e.color);
			gr.drawLine(x1,y1,x2,y2);
		}
		gr.setColor(Color.GRAY);
	}


}

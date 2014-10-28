package module2_vertexColoring;

import generalArcConsistency.AstarGAC;

import javax.swing.JFrame;

import aStar.Result;

public class GUI extends JFrame {
	
	VCProblem problem;
	
	private boolean solutionFound = false;
	
	private Graph graph;
		
	public GUI(VCProblem problem) {
		super();

		this.problem = problem;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		graph = new Graph(problem, null);
		add(graph);
		setBounds(100, 100, 900, 900);
		setVisible(true);
		redrawGraph();
				
	}
	
	public void redrawGraph() {
		setBounds(100, 100, 900, 900);
		graph.update(getGraphics());
	}
	
	public void solution(VCState state) {
		graph.reDraw(state);
		solutionFound = true;
		graph.done = true;
	}
	
	public void redrawGraph(VCState state) {
		if(!solutionFound)
			graph.reDraw(state);
	}

}

/*		for(Vertex v : problem.vertices)
			guiVertices.put(v.id, v);
		for(Edge e : problem.edges)
			guiEdges.put(e.id, e);
 */
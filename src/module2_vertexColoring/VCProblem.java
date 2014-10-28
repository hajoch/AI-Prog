package module2_vertexColoring;

import generalArcConsistency.Constraint;
import generalArcConsistency.GACProblem;
import generalArcConsistency.GACState;
import generalArcConsistency.Revise;
import generalArcConsistency.Variable;
import interpreter.Interpreter;

import java.awt.Color;
import java.awt.IllegalComponentStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import aStar.Node;

public class VCProblem extends GACProblem {
		
	public final int DOMAINSIZE;
	public final int MAX_ASSUMPTIONS = 5;
	public final Color DEFAULT_COLOR = Color.GRAY;
	public final int ARCCOST = 1;
	
	public HashSet<Vertex> vertices;
	public ArrayList<Edge> edges;
		
	public GUI gui;
	
	public VCProblem(List<Constraint> constraint, HashSet<Vertex> vertices, ArrayList<Edge> edges, int domain, GUI gui) {
		super(constraint, vertices);
		this.vertices = vertices;
		for(Vertex v : vertices) {
			v.setDomain(getDomain());
		}
		this.edges = edges;
		DOMAINSIZE = domain;
		this.gui = gui;
	}
	
	//---------------------------------
	//	General methods
	//---------------------------------
	
	@Override
	public float getArchCost(Node parent, Node successors) {
		return ARCCOST;
	}
	
	
	@Override
	public Node getStartNode() {
		VCState s0 = new VCState(vertices);
		
		for(Variable v : s0.getVariables())
			v.setDomain(getDomain());
		
		for(Edge e : edges) {
			Variable var1 = s0.getVariableByID(e.id1);
			Variable var2 = s0.getVariableByID(e.id2);
			var1.addNeighbor(var2);
			var2.addNeighbor(var1);
		}
		for(Constraint c : CONSTRAINTS)
			s0.addContraint(c);
		
		Node n0 = new Node(s0, 0);
		return n0;
	}


	@Override
	public float heuristics(Node node) {
		int allDomains = 0;
		VCState state = (VCState)node.getState();
		for(Variable var : state.getVariables())
			allDomains += var.getDomainSize()-1;
		return allDomains;
	}
	
		
	//---------------------------------
	//	Problem specific methods
	//---------------------------------
	
	public List<Color> getDomain() {
		ArrayList<Color> domain = new ArrayList<Color>();
		domain.add(Color.RED);
		domain.add(Color.BLUE);
		domain.add(Color.GREEN);
		domain.add(Color.YELLOW);
		domain.add(Color.MAGENTA);
		domain.add(Color.ORANGE);
		domain.add(Color.CYAN);
		domain.add(Color.WHITE);
		domain.add(Color.BLACK);
		return domain.subList(0, DOMAINSIZE);
	}

	@Override
	public void updateUI(Node node) {
		gui.redrawGraph((VCState)node.getState());
	}

}

/*	
	@Override
	public ArrayList<Node> generateSuccessorNodes(Node node) {
		
		VCState parent = (VCState)node.getState();
		
		ArrayList<Vertex> vertexDomains = new ArrayList<Vertex>();
		//Copying vertexes from parent node
		for(Vertex vertex : (Vertex)parent.getVariables()) {
			if(vertex.domain.size() <= 1)
				continue;
			//If Vertex already have been given color.
			if(vertex.color != Color.GRAY) {
				vertexDomains.add(vertexDomains.size(), vertex);
				continue;
			}
			vertexDomains.add(vertex);
		}
		
	//	Collections.rotate(vertexDomains, new Random().nextInt(vertexDomains.size()));
		Collections.sort(vertexDomains);
		
		ArrayList<Node> successors = new ArrayList<Node>();
		
		int assumptions = 0;
		for(Vertex vertex : vertexDomains) {
			
			VCState succState = new VCState(makeAssumption(parent.getVariables().toArray(), vertex.ID));
			succState.assumedVertex = vertex.ID;
			succState.generateId();
			
			Node successor = new Node(succState, 0); 							//TODO generate g ?

			successors.add(successor);
			assumptions++;
			//Setting the roof for number of assumptions
			if(assumptions>=MAX_ASSUMPTIONS)
				break;
		}
		return successors;
	}
	
	public ArrayList<Vertex> makeAssumption(ArrayList<Vertex> verticesToCopy, int vertexToMakeAssumptionOn) {
		ArrayList<Vertex> copies = new ArrayList<Vertex>();
		
		//Create copies of the vertices.
		for(Vertex vertex : verticesToCopy) {
			Vertex copy = vertex.deepCopy();
			
			if(copy.ID == vertexToMakeAssumptionOn) {
				copy.color = (Color) vertex.getDomain().get(new Random().nextInt(vertex.getDomainSize()));	//The Assumption
				copy.domain = new ArrayList<Color>();
				copy.domain.add(copy.color); 												//Singelize domain
			} else {
				if(copy.domain.size() == 1)
					copy.color = copy.domain.get(0);
			}
			//Copy the neighbors
			copy.neighbors = new HashSet<Vertex>();
			
			copies.add(copy);
		}
		for(int i=0; i<verticesToCopy.size(); i++) {
			for(Vertex g : verticesToCopy.get(i).neighbors) {
				copies.get(i).neighbors.add(copies.get(g.id));
			}
		}
		return copies;
	}
 */
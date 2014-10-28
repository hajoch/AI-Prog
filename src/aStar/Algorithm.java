package aStar;

import generalArcConsistency.GACProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import aStar.Node.Status;
import aStar.Result.Report;

/**	
 * A*-Algorithm 
 * 
 * @author Hallvard Jore Christensen
 *
 *
 */
public class Algorithm {
	
	public enum SearchAlg {
		BEST, DEPTH, BREADTH
	}
	
	public final int SLEEPTIME = 0;
	
	//States
	protected HashMap<Object, Node> states = new HashMap<Object, Node>();
	
	//Agenda
	protected ArrayList<Node> open = new ArrayList<Node>();
	//Fully expanded nodes
	protected ArrayList<Node> closed = new ArrayList<Node>();
	
	private Problem problem;
	
	protected SearchAlg searchType = SearchAlg.BEST;
	
	public Algorithm(Problem problem) {
		this.problem = problem;
	}
	
	public Result depthFirstSearch() {
		searchType = SearchAlg.DEPTH;
		return base();
	}
	
	public Result breadthFirstSearch() {
		searchType = SearchAlg.BREADTH;
		return base();
	}
	
	public Result bestFirstSearch() {
		searchType = SearchAlg.BEST;
		return base();
	}
	
	protected Result base() {
		
		Result result = new Result();
		
		// Clean out open and closed
		open.clear();
		closed.clear();
		
		//Generate the initial node, n0 for the state.
		Node n0 = problem.getStartNode();
		
		//put startState in State hashmap
		states.put(n0.getState().getId(), n0);
		
		n0.setG(0);
		n0.setH(problem.heuristics(n0));
		calculateF(n0);
		
		if(problem.isSolution(n0))
			return result.end(n0, Report.SUCCESS, searchType);
		
		addToOpen(n0);
		
		//Agenda loop
		while(!open.isEmpty()) {
			
			//Pop node from OPEN
			Node x = open.remove(0);
			addToClosed(x);
						
			//Check if solution
			if(problem.isSolution(x)) {
				return result.end(x, Report.SUCCESS, searchType);
			}
			
			ArrayList<Node> succ = problem.generateSuccessorNodes(x);
			for(Node s : succ) {
				
				//If node S* has previously been created, and if state(S*) = state(S), then S <- S*.
				if(states.containsKey(s.getState().getId())) {
					s = states.get(s.getState().getId());
				} else {
					states.put(s.getState().getId(), s);
				}
				
				x.addSuccessor(s);
				if(!open.contains(s) && !closed.contains(s)) {
					attachAndEval(s, x);
					addToOpen(s);
				} else if((x.getG() + problem.getArchCost(x, s)) < s.getG()) {//g(X) + arc-cost(X,S) < g(S) then (found cheaper path to S)
					attachAndEval(s, x);
					if(closed.contains(s))
						propagatePathImprovements(s);
				}
			}
			//Increase count of iterations used.
			result.increment(); 
			
		}
		
		Collections.sort(closed);
		return result.end(closed.get(0), Report.FAILED, searchType);
	}
	
	
	/**
	 * 	Propagate Path Improvements
	 * 
	 * <p> The propagation of path improvements recurses through the children and 
	 * possibly many other descendants to update the cost of getting to the respective 
	 * node</p>
	 * 
	 * @param n	Node to propagate
	 */
	protected void propagatePathImprovements(Node n) {
		for(Node s : n.getSuccessors()) {
			if(n.getG() + problem.getArchCost(n,s) < s.getG()) {
				s.setBestParent(n);
				s.setG(n.getG() + problem.getArchCost(n, s));
				calculateF(s);
				propagatePathImprovements(s);
			}
		}
	}
	
	/**
	 *  Attach and Evaluate
	 *  
	 * <p>The attach-and-eval routine simply attaches a child node to a node 
	 * that is now considered its best parent (so far).</p>
	 * 
	 * @param successor Successor node
	 * @param parent	Parent node
	 */
	protected void attachAndEval(Node successor, Node parent) {
		successor.setBestParent(parent);
		successor.setG(parent.getG()+problem.getArchCost(parent, successor));
		problem.heuristics(successor);
		calculateF(successor);
	}
	
	/**
	 * Calculates the F value of the given node
	 * @param n	The node to perform the calculation on.
	 */
	protected void calculateF(Node n) {
		n.setF(n.getG()+n.getH());
	}
	
	/**
	 * Adds node to the Open list, updates the status and sorts it
	 * @param n The Node
	 */
	protected void addToOpen(Node n) {

		n.setStatus(Status.OPEN);
		
		switch (searchType) {
		case BREADTH:
			open.add(open.size(), n); 		//Add to end
			break;
		case DEPTH:
			open.add(0, n); 				//Add to beginning
			break;
		default: //BEST
			open.add(n);					//Add and sort
			Collections.sort(open);
			break;
		}
	}
	
	/**
	 * Adds node to the closed list and updates its status
	 * @param n The Node
	 */
	protected void addToClosed(Node n) {
		closed.add(n);
		n.setStatus(Status.CLOSED);
	}
}

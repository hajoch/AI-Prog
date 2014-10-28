package aStar;

import java.util.LinkedList;

import javax.swing.GroupLayout.Alignment;

import aStar.Algorithm.SearchAlg;

public class Result {
	
	public enum Report {
		SUCCESS, FAILED, ERROR, RUNNING
	}
	
	private Report report;
	
	private int iterations = 0;
	private LinkedList<Node> path;
	private State solution;
	
	private SearchAlg algorithmUsed = SearchAlg.BEST;
	
	public Result() {
		report = Report.RUNNING;
	}
	
	/**
	 * 
	 * @param report
	 * @return
	 */
	public Result end(Report report) {
		this.report = report;
		return this;
	}
	
	public Result end(Node n, Report rep, SearchAlg algorithmUsed) {
		this.algorithmUsed = algorithmUsed;
		setSolution(n.getState());
		createPath(n);
		return end(rep);
	}
	
	public void createPath(Node n) {
		path = new LinkedList<Node>();
		path.add(n);
		Node temp = n;
		while(!(temp.getBestParent() == null)) {
			path.add(temp.getBestParent());
			temp = temp.getBestParent();
		}
	}
	
	
	/**
	 * Get Report
	 * @return
	 */
	public Report report() {
		return report;
	}
	
	/**
	 * Increment the count of iterations used.
	 */
	public void increment() {
		iterations++;
	}
	
	/**
	 * Get the number of iterations the process used.
	 * @return
	 */
	public int iterations() {
		return iterations;
	}
	
	/**
	 * Get Path
	 * @return List of Nodes describing the path to the goal state
	 */
	public LinkedList<Node> path() {
		return path;
	}
	
	/**
	 * Set Path
	 * @param path List of Nodes describing the path to the goal state
	 */
	public void setPath(LinkedList<Node> path) {
		this.path = path;
	}
	
	/**
	 * Set Solution
	 * @param solution goal State
	 */
	public void setSolution(State solution) {
		this.solution = solution;
	}
	
	/**
	 * Get Solution
	 * @return Goal state
	 */
	public State solution() {
		return solution;
	}
	
	public SearchAlg AlgorithmUsed() {
		return algorithmUsed;
	}

}

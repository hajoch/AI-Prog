package module3_FlowPuzzle;

import interpreter.Interpreter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import module1_navigation.GUI;
import module1_navigation.NavProblem;
import aStar.Algorithm;
import aStar.Node;
import aStar.Result;
import aStar.Result.Report;
import generalArcConsistency.Constraint;
import generalArcConsistency.GACProblem;
import generalArcConsistency.GACState;
import generalArcConsistency.Revise;
import generalArcConsistency.Variable;

public class FlowPuzzle extends GACProblem {

	private final int ARCHCOST = 3;
	private final int COVERAGEBONUS = 1; // Coverage-bonus to reward longer
											// journeys

	public final int SIZE;
	public final int FLOWS;

	// public final int[][] DATA;

	private final PuzzlePiece[][] puzzle;

	public FlowPuzzle(int size, int flows, PuzzlePiece[][] puzzle,
			HashSet<? extends Variable> vars) {
		super(null, vars);

		this.puzzle = puzzle;
		SIZE = size;
		FLOWS = flows;
	}

	@Override
	public void updateUI(Node node) {

	}

	@Override
	public float getArchCost(Node parent, Node successors) {
		// TODO Auto-generated method stub
		return ARCHCOST;
	}

	@Override
	public boolean isSolution(Node node) {
		FlowState state = (FlowState) node.getState();

		// int coverage = 0;

		boolean[][] covered = new boolean[SIZE][SIZE];

		// All flowHeads are connected
		for (Variable var : state.getVariables()) {
			FlowHead fh = (FlowHead) var;
			if (!fh.isConnected())
				return false;
			// coverage += fh.getPath().size();
			for (PuzzlePiece pp : fh.getPath()) {
				covered[pp.Y][pp.X] = true;
			}
		}
		for (boolean[] bb : covered)
			for (boolean b : bb)
				if (!b)
					return false;
		// if(heuristics(node) != 0)
		// return false;

		System.out.println("SOLUTION!");
		return true;
		/*
		 * //Checks if all spaces in the grid are filled with flows if(coverage
		 * != SIZE*SIZE) return false; System.out.println("SOLUTION FOUND");
		 * return true;
		 */
	}

	@Override
	public Node getStartNode() {

		FlowState s0 = new FlowState(VARIABLES);
		for (Variable var : s0.getVariables()) {
			for (Variable var2 : s0.getVariables()) {
				HashMap<String, Integer> varIDs = new HashMap<String, Integer>();
				varIDs.put("x", var.ID);
				varIDs.put("y", var2.ID);
				s0.addContraint(new Constraint(varIDs, "x != y"));
			}
		}
		return new Node(s0, 0);
	}

	@Override
	public float heuristics(Node node) {
		FlowState state = (FlowState) node.getState();

		// Combined Manhattan distance between the two variables of the same
		// color.
		int total = 0;
		int coverage = 0;

		int notConnected = FLOWS * 2;

		for (Variable var : state.getVariables()) {
			FlowHead fh1 = (FlowHead) var;
			if (fh1.isConnected())
				notConnected--;
			int manhattenDistance = 0;
			coverage += fh1.getPath().size();
			for (Variable var2 : state.getVariables()) {
				FlowHead fh2 = (FlowHead) var2;
				if (fh1.equals(fh2))
					continue;
				if (fh1.color.equals(fh2.color)) {
					manhattenDistance += Math.abs(fh1.getCurrent().X
							- fh2.getCurrent().X);
					manhattenDistance += Math.abs(fh1.getCurrent().Y
							- fh2.getCurrent().Y);
					break;
				}
			}
			total += manhattenDistance;
		}

		// Number of squares left.
		// int maxCoverage = SIZE*SIZE;
		// total += (maxCoverage-coverage);
		// total += notConnected*10;
		return total;
	}

	@Override
	public boolean isContradictary(Node n) {

		FlowState state = (FlowState) n.getState();

		boolean[][] obstacles = new boolean[SIZE][SIZE];

		for (FlowHead fh : (Set<FlowHead>) state.getVariables()) {
			if (!fh.isConnected() && fh.getDomain().isEmpty())
				return true;
			for (PuzzlePiece pp : fh.getPath()) {
				obstacles[pp.Y][pp.X] = true;
			}
		}
		
		/*					

		Algorithm algorithm;
		
		ArrayList<Color> tested = new ArrayList<Color>();
		for (FlowHead flowHead : (Set<FlowHead>) state.getVariables()) {
			if (!tested.contains(flowHead.color)) {
				if (flowHead.isConnected()) {
					tested.add(flowHead.color);
					continue;
				}

				Dimension dim = new Dimension(SIZE, SIZE);
				Point start = new Point(flowHead.getCurrent().X,
						flowHead.getCurrent().Y);
				Point goal = new Point(flowHead.getGoal().getCurrent().X,
						flowHead.getGoal().getCurrent().Y);

				obstacles[start.y][start.x] = false;
				obstacles[goal.y][goal.x] = false;

				NavProblem problem = new NavProblem(dim, start, goal, obstacles);
				NavProblem problem2 = new NavProblem(dim, goal, start, obstacles);

				algorithm = new Algorithm(problem);
				Result result = algorithm.bestFirstSearch();
				algorithm = new Algorithm(problem2);
				Result result2 = algorithm.bestFirstSearch();
				if (result.report() != Report.SUCCESS && result2.report() != Report.SUCCESS) {
					System.out.println("Denne faila" + flowHead.color);
					notifyListeners(state);
					
 					GUI navGui = new GUI(problem, result);
					GUI navGui2 = new GUI(problem2, result2); 
					return true;
				}

				tested.add(flowHead.color);
			}
		}
 */
		/*	
*/
		return false;
	}

	// GAC

	@Override
	public void rerun(Node node) {
		FlowState state = (FlowState) node.getState();
		notifyListeners(state);
		queue.clear();
		addVarsToRevise(state);
		domainFiltering(node);
	}

	@Override
	public void init(Node node) {
		rerun(node);
	}

	@Override
	public void domainFiltering(Node node) {

		FlowState state = (FlowState) node.getState();

		/*
		 * for(FlowHead fh : ((Set<FlowHead>)state.getVariables())) {
		 * if(fh.isConnected()) continue; fh.generateDomain(); }
		 */
		int infinity = 200;

		while (!queue.isEmpty()) {

			Revise revise = queue.remove();

			boolean createdSingleton = revise(revise);

			if (infinity < 0)
				return;

			if (createdSingleton) {
				infinity--;
				// Move
				((FlowHead) revise.FOCAL).move((PuzzlePiece) revise.FOCAL
						.getDomain().get(0));
				try {
					Thread.sleep(50);
					// System.out.println("LOOOP");
					notifyListeners(state);
				} catch (Exception e) {
				}

				if (isSolution(node))
					return;
				if (isContradictary(node))
					return;

				queue.clear();
				handleMoveVariable(state, revise.FOCAL, true);
				addVarsToRevise(state);
			}
		}
		/*
		 * if(moveSingletons(state)) { if(isSolution(node)) return;
		 * addVarsToRevise(state); domainFiltering(node); }
		 */

		/*
		 * for(Variable var : state.getVariables()){ if(var.isDomainSingleton())
		 * { addVarsToRevise(state); domainFiltering(node); } }
		 */
	}

	private void handleMoveVariable(FlowState state, Variable focalO,
			boolean both) {

		if (both) {
			for (Constraint cNon : state.getConstraints()) {
				if (cNon.containsNonFocal(focalO.ID)) {
					Variable focal = state.getVariableByID((int) cNon
							.getVariableIDs().get("x"));
					Variable nonFocal = state.getVariableByID((int) cNon
							.getVariableIDs().get("y"));
					queue.addFirst(new Revise(focal, nonFocal, cNon));
				}
			}
		}
		for (Constraint c : state.getConstraints()) {
			if (c.containsFocal(focalO.ID)) {
				Variable focal = state.getVariableByID((int) c.getVariableIDs()
						.get("x"));
				Variable nonFocal = state.getVariableByID((int) c
						.getVariableIDs().get("y"));
				queue.addFirst(new Revise(focal, nonFocal, c));
			}
		}
	}

	private void addVarsToRevise(FlowState state) {

		// queue.clear();

		for (Constraint c : state.getConstraints()) {

			Variable focal = state.getVariableByID((int) c.getVariableIDs()
					.get("x"));
			Variable nonFocal = state.getVariableByID((int) c.getVariableIDs()
					.get("y"));

			if (((FlowHead) focal).isConnected())
				continue;
			queue.add(new Revise(focal, nonFocal, c));
		}
	}

	@Override
	public ArrayList<Node> generateSuccessorNodes(Node n) {
		FlowState state = (FlowState) n.getState();

		// rerun(n);

		if (isContradictary(n)) {
			return new ArrayList<Node>();
		}
		if (isSolution(n)) {
			return new ArrayList<Node>();
		}

		int smallDist = 0;

		FlowHead assumedVariable = null;
		int smallestDistance = 9999999;
		int smallestDomain = 999999;

		for (FlowHead var : (Set<FlowHead>) state.getVariables()) {
			if (var.getConnected() || !var.GUESSING)
				continue;
			// if(var.getDomainSize() <= smallestDomain) {
			// if(var.getDomainSize() == smallestDomain) {
			int manhattan = Math.abs(var.getCurrent().X
					- var.getGoal().getCurrent().X);
			manhattan += Math.abs(var.getCurrent().Y
					- var.getGoal().getCurrent().Y);
			if (manhattan <= 3)
				smallDist++;

			if (manhattan > smallestDistance) {
				continue;
			}
			smallestDistance = manhattan;
			// }
			// Best
			assumedVariable = var;
			// }
		}

		/*
		 * if(smallDist > 2) { for(FlowHead var :
		 * (Set<FlowHead>)state.getVariables()) { if(var.getConnected() ||
		 * !var.GUESSING) continue; if(var.getDomainSize() < smallestDomain) {
		 * smallestDomain = var.getDomainSize(); assumedVariable = var; } } }
		 */

		if (assumedVariable == null) {
			System.out.println("No more children can be generated");
			return new ArrayList<Node>();
		}

		int assumedID = assumedVariable.ID;

		ArrayList<Node> successors = new ArrayList<Node>();
		for (Object domainElement : assumedVariable.getDomain()) {
			FlowState succState = (FlowState) state.deepCopy();
			succState.assumedVariable = assumedVariable;

			ArrayList<Object> succDomain = new ArrayList<Object>();
			// succDomain.add(domainElement);

			Variable newAssumed = succState.getVariableByID(assumedID);

			newAssumed.getDomain().clear();
			((ArrayList<Object>) newAssumed.getDomain()).add(domainElement);
			// ((ArrayList<Object>)succState.getVariableByID(assumedID).getDomain()).add(domainElement);

			((FlowHead) newAssumed).move((PuzzlePiece) newAssumed.getDomain()
					.get(0));
			// ((FlowHead)succState.getVariableByID(assumedID)).move((PuzzlePiece)
			// succState.getVariableByID(assumedID).getDomain().get(0));

			Node succNode = new Node(succState, n.getG());

			notifyListeners(succState);
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
			}

			queue.clear();
			handleMoveVariable(succState, newAssumed, true);
			addVarsToRevise(succState);
			domainFiltering(succNode);

			// addVarsToRevise(succState);
			// domainFiltering(succNode);

			if (isContradictary(succNode))
				continue;
			if (isSolution(succNode)) {
				ArrayList<Node> sol = new ArrayList<Node>();
				sol.add(succNode);
				return sol;
			}

			successors.add(succNode);
		}
		/*
		 * for(Node succNodede : successors) { FlowState sta =
		 * (FlowState)succNodede.getState(); for(FlowHead fh :
		 * (Set<FlowHead>)sta.getVariables()) { // fh.move((PuzzlePiece)
		 * fh.getDomain().get(0)); queue.clear(); handleMoveVariable(sta, fh,
		 * false); notifyListeners(sta); addVarsToRevise(sta);
		 * domainFiltering(succNodede); } }
		 */
		return successors;

		/*
		 * PriorityQueue<Variable> priQue = new PriorityQueue<Variable>();
		 * for(Variable v : state.getVariables()) { if(v.getDomainSize() > 1 &&
		 * !((FlowHead)v).isConnected()) { priQue.add(v); } }
		 * while(!priQue.isEmpty()) { Variable assumed = priQue.poll(); int id =
		 * assumed.ID;
		 * 
		 * ArrayList<Node> successors = new ArrayList<Node>(); for(Object
		 * domainElement : assumed.getDomain()) { FlowState succ =
		 * (FlowState)state.deepCopy(); ArrayList<Object> succDomain = new
		 * ArrayList<Object>(); succDomain.add(domainElement);
		 * succ.getVariableByID(id).setDomain(succDomain);
		 * succ.getVariableByID(id).isDomainSingleton(); succ.assumedVariable =
		 * succ.getVariableByID(id); Node succNode = new Node(succ, n.getG());
		 * queue.clear(); addVarsToRevise(succ); rerun(succNode);
		 * if(isContradictary(succNode)) continue; successors.add(succNode); }
		 * if(!successors.isEmpty()) return successors; }
		 * System.out.println("---------- NO NEW CHILDREN-----------"); try {
		 * Thread.sleep(0); } catch (Exception e) { // TODO: handle exception }
		 * return new ArrayList<Node>();
		 */

	}

	@Override
	protected boolean revise(Revise revise) {

		if (((FlowHead) revise.FOCAL).isConnected()) {
			return false;
		}

		// ((FlowHead)revise.FOCAL).generateDomain();

		ArrayList<Object> toBeRemoved = new ArrayList<Object>();
		for (Object focalDomObj : revise.FOCAL.getDomain()) {
			for (Object nonFocalPathObj : ((FlowHead) revise.NONFOCAL)
					.getPath()) {
				if (((FlowHead) revise.FOCAL).getGoal().getCurrent() == (PuzzlePiece) nonFocalPathObj)
					continue;
				if (!Interpreter.interpret(revise.CONSTRAINT.RULE,
						new Object[] { focalDomObj, nonFocalPathObj })) {
					toBeRemoved.add(focalDomObj);
				}
			}
		}
		if (!toBeRemoved.isEmpty()) {
			revise.FOCAL.getDomain().removeAll(toBeRemoved);
			return revise.FOCAL.isDomainSingleton();
		}
		return false;
		/*
		 * ArrayList<Object> toBeRemoved = new ArrayList<Object>(); for(Object
		 * focal : revise.FOCAL.getDomain()) { for(Variable v :
		 * revise.FOCAL.getNeighbors()) { for(Object nonFocal :
		 * ((FlowHead)v).getPath()) { if(((PuzzlePiece)focal).ID ==
		 * ((PuzzlePiece)nonFocal).ID) { toBeRemoved.add(focal); } } } }
		 * if(!toBeRemoved.isEmpty()) {
		 * revise.FOCAL.getDomain().removeAll(toBeRemoved);
		 * revise.FOCAL.notifyDomainChanged(); return true; } return false;
		 */
		/*
		 * ArrayList<Object> toBeRemoved = new ArrayList<Object>(); for(Object
		 * focal : revise.FOCAL.getDomain()) { for(Object nonFocal :
		 * ((FlowHead)revise.NONFOCAL).getPath()) { Object[] objs = new
		 * Object[]{focal, nonFocal};
		 * if(Interpreter.interpret(revise.CONSTRAINT.RULE, objs)) {
		 * toBeRemoved.add(focal); break; } } } if(!toBeRemoved.isEmpty()) {
		 * revise.FOCAL.getDomain().removeAll(toBeRemoved);
		 * revise.FOCAL.notifyDomainChanged(); return true; } return false;
		 */
	}
}

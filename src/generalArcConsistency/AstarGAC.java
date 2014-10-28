package generalArcConsistency;

import java.util.ArrayList;
import java.util.Collections;

import aStar.Algorithm;
import aStar.Node;
import aStar.Problem;
import aStar.Result;
import aStar.Result.Report;

public class AstarGAC extends Algorithm{

	protected GACProblem gacProblem;
	
	public AstarGAC(Problem problem) {
		super(problem);
		gacProblem = (GACProblem) problem;
	}
	
	@Override
	public Result base() {
		Result result = new Result();
		
		open.clear();
		closed.clear();
		
		Node n0 = gacProblem.getStartNode();
		
		states.put(n0.getState().getId(), n0);
		
		gacProblem.init(n0);
		gacProblem.domainFiltering(n0);		
		
		n0.setG(0);
		n0.setH(gacProblem.heuristics(n0));
		calculateF(n0);
//		n0.setF(gacProblem.heuristics(n0));
		
/*		for(Variable v : ((GACState)n0.getState()).getVariables())
			System.out.print(" "+v.domain.size());
		System.out.println();										
*/
		
		if(gacProblem.isSolution(n0))
			return result.end(n0, Report.SUCCESS, searchType);
		if(gacProblem.isContradictary(n0))
			return result.end(n0, Report.FAILED, searchType);
		
		addToOpen(n0);
		
		while(!open.isEmpty()) {
			
			//Pop from agenda
			Node current = open.remove(0);
			addToClosed(current);
			
			
			//break for GUI

//
			gacProblem.notifyListeners((GACState) current.getState());

			if(gacProblem.isSolution(current))
				return result.end(current, Report.SUCCESS, searchType);
			if(gacProblem.isContradictary(current)) {
				System.out.println("State unsolvable");
				continue;
			}
			System.out.println("State solvable");
						
			gacProblem.rerun(current);
			System.out.println("No more deductions available");
			try {
				Thread.sleep(100);
				gacProblem.updateUI(current);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			ArrayList<Node> successors = gacProblem.generateSuccessorNodes(current);
			for(Node successor : successors) {
				
				if(states.containsKey(successor.getState().generateId())) {
					successor = states.get(successor.getState().generateId());
				} else {
					states.put(successor.getState().generateId(), successor);
				}
				
				//Compute node values
//				gacProblem.rerun(successor);
				
				computeValues(successor, current);
				
				
				for(Variable v : ((GACState)successor.getState()).getVariables())
					System.out.print(" "+v.getDomainSize());
				System.out.println();
	 			
				if(!open.contains(successor) && !closed.contains(successor)) {
					attachAndEval(successor, current);
					addToOpen(successor);
				}

//				addToOpen(successor);
/*				//Not neccessary?
				else if((current.getG() + gacProblem.getArchCost(current, successor)) < successor.getG()) {
					attachAndEval(successor, current);
					if(closed.contains(successor))
						propagatePathImprovements(successor);
				}
				*/
			}
			result.increment();
			
		}
		Collections.sort(closed);
		return result.end(closed.get(0), Report.FAILED, searchType);
	}
	
	
	protected void computeValues(Node n, Node p) {
		n.setG(p.getG()+gacProblem.getArchCost(p, n));
		n.setH(gacProblem.heuristics(n));
		n.setF();
	}

}

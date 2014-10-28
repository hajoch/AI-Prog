package generalArcConsistency;

import interpreter.Interpreter;

import java.awt.Color;
import java.awt.IllegalComponentStateException;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import module2_vertexColoring.VCState;
import module2_vertexColoring.Vertex;
import module3_FlowPuzzle.FlowHead;
import aStar.Node;
import aStar.Problem;

public abstract class GACProblem implements Problem{
	
	private ArrayList<StateChangeListener> listeners = new ArrayList<StateChangeListener>();
	
	protected LinkedList<Revise> queue = new LinkedList<Revise>();
	
	public final List<? extends Constraint> CONSTRAINTS;
	public final HashSet<? extends Variable> VARIABLES;
	
	protected GACProblem(List<? extends Constraint> constraints, HashSet<? extends Variable> variables) {
		this.CONSTRAINTS = constraints;
		this.VARIABLES = variables;
	}
	
	public boolean isContradictary(Node n) {
		GACState state = (GACState)n.getState();
		for(Variable var : state.getVariables())
			if(var.isDomainEmpty())
				return true;
		return false;
	}
	
	@Override
	public boolean isSolution(Node node) {
		GACState state = (GACState)node.getState();
		for(Variable var : state.getVariables())
			if(!var.isDomainSingleton())
				return false;
		return true;
	}
	
	public void init(Node node) {
		GACState state = (GACState)node.getState();
		for(Variable var : state.getVariables())
//			if(var.color != Color.GRAY)
				addNeighborsToQueue(var, state.getConstraints());
	}

	public void domainFiltering(Node node) {
				
		GACState state = (GACState)node.getState();
		
		while(!queue.isEmpty()) {
			Revise revise = queue.remove();
			
			boolean reduced = revise(revise);
			
			if(reduced) {
				for(Variable neighbor : revise.FOCAL.getNeighbors()) {
					if(neighbor.ID != revise.FOCAL.ID) {
						for(Constraint c : state.getConstraints()) {
							Variable[] vars = new Variable[2];
							int i=0;
							for(Variable v : state.getVariables()) {
								if(c.contains(v.ID)) {
									vars[i++] = v;
								}
							}
							if(vars[0].equals(revise.FOCAL)) {
								queue.add(new Revise(vars[1], vars[0], c));
							} else {
								queue.add(new Revise(vars[0], vars[1], c));
							}
//							queue.add(new Revise(neighbor, revise.FOCAL, c));							
						}
					}
				}
			}
		}
	}

	public void rerun(Node node) {
		notifyListeners((GACState)node.getState());
		GACState state = (GACState)node.getState();
		
		addNeighborsToQueue(state.getVariableByID(state.assumedVariable.ID), state.getConstraints());
		domainFiltering(node);
	}

	//---------------------------
	
	private void addNeighborsToQueue(Variable var, List<Constraint> constraints) {
		for(Variable neighbor : var.getNeighbors()) {
			for(Constraint c : constraints) {
				queue.add(new Revise(neighbor, var, c));
			}
		}
	}
	
	protected boolean revise(Revise revise) {
        ArrayList<Object> toBeRemoved = new ArrayList<Object>();
        
        for(Object focal: revise.FOCAL.getDomain()){
            boolean valid = false;
            for(Object nonFocal: revise.NONFOCAL.getDomain()){
                Object[] objs = new Object[]{focal, nonFocal};
                if(Interpreter.interpret(revise.CONSTRAINT.RULE, objs)){
                    valid = true;
                    break;
                }
            }
            if(!valid){
                toBeRemoved.add(focal);
            }
        }
        if(!toBeRemoved.isEmpty()){
        	for(Object v: toBeRemoved) {
        		revise.FOCAL.getDomain().remove(v);
        	}
        	revise.FOCAL.domain.removeAll(toBeRemoved); //TODO
        	revise.FOCAL.notifyDomainChanged();
            return true;
        }
        return false;
    }
	
	public abstract void updateUI(Node node);
	
	
	@Override
	public abstract float getArchCost(Node parent, Node successors);
	
	@Override
	public ArrayList<Node> generateSuccessorNodes(Node n) {
		GACState state = (GACState) n.getState();
		
		if(isContradictary(n)) {
			return new ArrayList<Node>();
		}
		if(isSolution(n)) {
			return new ArrayList<Node>();
		}
		
		PriorityQueue<Variable> priQue = new PriorityQueue<Variable>();
		for(Variable v : state.getVariables()) {
			if(v.getDomainSize() > 1) {
				priQue.add(v);
			}
		}
		while(!priQue.isEmpty()) {
			Variable assumed = priQue.poll();
			int id = assumed.ID;
			
			ArrayList<Node> successors = new ArrayList<Node>();
			for(Object domainElement : assumed.getDomain()) {
				GACState succ = (GACState)state.deepCopy();
				ArrayList<Object> succDomain = new ArrayList<Object>();
				succDomain.add(domainElement);
//TODO				succ.getVariableByID(id).setDomain(succDomain);
				succ.assumedVariable = succ.getVariableByID(id);
				Node succNode = new Node(succ, n.getG());
				rerun(succNode);
				if(isContradictary(succNode))
					continue;
				successors.add(succNode);
			}
			if(!successors.isEmpty())
				return successors;
		}
		return new ArrayList<Node>();
	}
	@Override
	public abstract Node getStartNode();
	
	@Override
	public abstract float heuristics(Node node);	
	
	
	public void addListener(StateChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners(GACState state) {
		for(StateChangeListener scl : listeners) {
			scl.onStateChanged(state);
		}
	}
}


/*
 * 
 * 	public boolean revise(Revise revise) {
		
		String constraint = revise.constraints;
		
		Interpreter interpreter = new Interpreter(constraint);
		
		boolean domainReduced = false;
		
		Color x = revise.v2.color; //TODO
		for(Color y : revise.v1.domain) {
			if(!interpreter.interpret("x", x, "y", y)) {
				domainReduced = true;
			}
		}
		if(domainReduced)
			revise.v1.domain.remove(x);

		if(revise.v1.domain.size() == 1) {
			revise.v1.color = revise.v1.domain.get(0);
		}
		
		return domainReduced;
	}	
*/

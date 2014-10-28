package module3_FlowPuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import generalArcConsistency.Constraint;
import generalArcConsistency.GACState;
import generalArcConsistency.Variable;

public class FlowState extends GACState{

	public FlowState(HashSet<? extends Variable> variables) {
		super(variables);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object generateId() {
		String id = "";
		for(Variable var : variables) {
			for(PuzzlePiece pp : ((FlowHead)var).getPath()) {
				id = id + pp.ID;
			}
		}
		return id;//Math.floor((Math.random()*100000));
	}

	@Override
	public GACState deepCopy() {
		HashSet<FlowHead> varCopy = new HashSet<FlowHead>();
		for(Variable v : getVariables()) {
			varCopy.add((FlowHead)v.deepCopy());
		}
		FlowState copy = new FlowState(varCopy);
		
		for(FlowHead var : (Set<FlowHead>)copy.getVariables()) {
			FlowHead goal = (FlowHead) copy.getVariableByID(var.getGoal().ID);
			var.setGoal(goal);
		}
		
		for(Constraint c : getConstraints())
			copy.addContraint(c.deepCopy());
		
/*		Variable assumedVariableCopy = null;
		if(assumedVariable != null) {
			assumedVariableCopy = assumedVariable;
		}
		copy.assumedVariable = assumedVariableCopy;*/
		return copy;
	}
	
	@Override
	public HashSet<? extends Variable> getVariables() {
		return variables;
	}
	
/*	@Override
	public List<Constraint> getConstraints() {
		ArrayList<Constraint> newConstraints = new ArrayList<Constraint>();
		ArrayList<PuzzlePiece> usedSpaces = new ArrayList<PuzzlePiece>();
		for(Variable var : getVariables()) {
			for(PuzzlePiece pp : ((FlowHead)var).getPath()) {
				usedSpaces.add(pp);
			}
		}
		FlowHead nonFocal = new FlowHead(18291337, usedSpaces, null, null, null);
		for(Variable focal : variables) {
			newConstraints.add(new Constraint(variableIDs, rule))
		}
	}
*/
	@Override 
	public Object getId() {
		return generateId();
	}
}

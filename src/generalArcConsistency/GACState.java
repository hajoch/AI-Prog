package generalArcConsistency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aStar.State;

public abstract class GACState extends State {
	
	public Variable assumedVariable;

	protected HashSet<? extends Variable> variables;

	protected List<Constraint> constraints;

	
	public GACState(HashSet<? extends Variable> variables) {
		this.variables = variables;
		constraints = new ArrayList<Constraint>();
	}
	
	public HashSet<? extends Variable> getVariables() {
		return variables;
	}
	
	public Variable getVariableByID(int id) {
		for(Variable var : variables)
			if(var.ID == id)
				return var;
		return null;
	}
	
	public void addContraint(Constraint c) {
		constraints.add(c);
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}
	
	public abstract Object generateId();
	public abstract GACState deepCopy();

}

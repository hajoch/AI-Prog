package module2_vertexColoring;

import generalArcConsistency.Constraint;
import generalArcConsistency.GACState;
import generalArcConsistency.Variable;

import java.awt.Color;
import java.security.Key;
import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import aStar.State;

public class VCState extends GACState {
		
	public VCState(HashSet<? extends Variable> vertices) {
		super(vertices);
		generateId();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object generateId() {
		int[] vals = new int[variables.size()];
		for(int i=0; i<variables.size(); i++)
			vals[i] = Integer.parseInt(getVariableByID(i).toString());
		int s = Arrays.hashCode(vals);
		int y = (int)Math.floor(Math.random()*100000000);
		return y;
	}

	@Override
	public GACState deepCopy() {
		HashSet<Variable> varCopies = new HashSet<Variable>();
		for(Variable var : variables) {
			varCopies.add(var.deepCopy());
		}
		//Adding neighbors
		VCState copy = new VCState(varCopies);
		for(Variable v : varCopies) {
			for(Variable neigh : getVariableByID(v.ID).getNeighbors()) {
				v.addNeighbor(copy.getVariableByID(neigh.ID));
			}
		}
		
		for(Constraint c : constraints)
			copy.addContraint(c);
		
		return copy;
	}
}

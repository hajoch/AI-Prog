package generalArcConsistency;

import java.util.HashMap;

public class Constraint {
	
	public final String RULE;
	
	private HashMap<String, Integer> variableIDs;
	
	public Constraint(HashMap<String, Integer> variableIDs, String rule) {
		this.RULE = rule;
		this.variableIDs = variableIDs;
	}
	
	public boolean contains(int id) {
		return variableIDs.containsValue((Integer)id);
	}
	
	public boolean containsFocal(int id) {
		if((int)variableIDs.get("x") == id) {
			return true;
		}
		return false;
	}
	public boolean containsNonFocal(int id) {
		if((int)variableIDs.get("y") == id) {
			return true;
		}
		return false;
	}
	
	public HashMap<String, Integer> getVariableIDs() {
		return variableIDs;
	}
	
	public Constraint deepCopy() {
		HashMap<String, Integer> newVarIDs = new HashMap<String, Integer>(variableIDs);
		return new Constraint(newVarIDs, RULE);
		
	}
}

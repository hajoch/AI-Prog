package generalArcConsistency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Variable implements Comparable<Variable> {
	
	public final int ID;

	protected final List<?> domain;
	protected List<Variable> neighbors;
	
	protected Variable(int id, List<?> domain) {
		this.ID = id;
		this.domain = domain;
		neighbors = new ArrayList<Variable>();
	}
	
	public abstract Variable deepCopy();
	
	public boolean isDomainSingleton() {
		return (domain.size() == 1);
	}
	
	/**
	 * 		is Domain empty?
	 * @return	empty
	 */
	public boolean isDomainEmpty() {
		return domain.isEmpty();
	}
	
	public List<?> getDomain() {
		return domain;
	}
/*	public void setDomain(List<?> domain) {
		this.domain = domain;
		notifyDomainChanged();
	}
	*/
	public int getDomainSize() {
		return domain.size();
	}
	
	public List<Variable> getNeighbors() {
		return neighbors;
	}
	
	public void addNeighbor(Variable neigh){
		neighbors.add(neigh);
	}
	
	public abstract void notifyDomainChanged();
	
	/**
	 * Compares one Variable to another based on the domain
	 */
	@Override
	public int compareTo(Variable other) {

		final int WORSE = -1;
		final int EQUAL = 0;
		final int BETTER = 1;
		
		if(domain.size() == other.domain.size()) 	return EQUAL;
		if(domain.size() < other.domain.size()) 	return WORSE;
		else 										return BETTER;
	}
}

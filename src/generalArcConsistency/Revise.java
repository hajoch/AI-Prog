package generalArcConsistency;

public class Revise {
	
	public final Variable FOCAL;
	public final Variable NONFOCAL;
	
	//Constraints
	public final Constraint CONSTRAINT;
	
	public Revise(Variable focal, Variable nonFocal, Constraint constraint) {
		this.FOCAL = focal;
		this.NONFOCAL = nonFocal;
		this.CONSTRAINT = constraint;
	}
}

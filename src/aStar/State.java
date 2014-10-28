package aStar;

public abstract class State {
	
	private Object id;
		
	public abstract Object generateId();
	
	//Getter and setter for ID.
	protected void setId(Object id) 	{	this.id = id;}
	
	public Object getId() {
		if(id == null)
			return generateId();
		return id;
	}
	
}

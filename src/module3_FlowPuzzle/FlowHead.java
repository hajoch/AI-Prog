package module3_FlowPuzzle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import module2_vertexColoring.Vertex;
import generalArcConsistency.Variable;

public class FlowHead extends Variable {
	
	public final boolean GUESSING;

	private boolean connected = false;
	private PuzzlePiece current;
	private LinkedList<PuzzlePiece> path;
	
	private FlowHead goal;
	
	public final Color color;
	
	
	public FlowHead(int id, List<?> domain, PuzzlePiece location, Color color, LinkedList<PuzzlePiece> path, boolean guessing) {
		super(id, domain);
		this.color = color;
		current = location;
	//	move(location);
		
		this.GUESSING = guessing;
		
		if(path == null){		this.path = new LinkedList<PuzzlePiece>(); this.path.add(location); }
		else					this.path = path;
		
		if(this.domain == null)		generateDomain();
	}
	
	public FlowHead getGoal() {
		return goal;
	}
	
	public void move(PuzzlePiece next) { 
		if(connected) {
			return;
		}
		if(connecting(next)) {
			System.out.println("CONNECTING: "+this.color.toString());
			connected = true;
			goal.setConnected();
//			current = next;
			return;
		}
		current = next;
		path.add(current);
		generateDomain();
	}

	@Override
	public Variable deepCopy() {
		List<PuzzlePiece> domainCopy = new ArrayList<PuzzlePiece>();
		for(Object o : getDomain())
			domainCopy.add((PuzzlePiece)o);	
		
		FlowHead copy = new FlowHead(ID, domainCopy, current, color, (LinkedList) path.clone(), GUESSING);
		if(connected)
			copy.setConnected();
		
		copy.setGoal(goal);
		
		return copy;
	}
	
	public void generateDomain() {
		List<?> newDom = current.getConnectedPieces();

		ArrayList<Object> thisDomain = (ArrayList<Object>) getDomain();
		thisDomain.clear();
		
		for(Object o : newDom){
			thisDomain.add(o);
		}
//		setDomain(newDom);
//		domain = newDom;
	}
	
	private boolean connecting(PuzzlePiece subject) {
		return goal.getCurrent().equals(subject);
	}
	
	public void setGoal(FlowHead goal) {
		this.goal = goal;
	}
	
	public void setConnected() {
		this.connected = true;
	}
	public boolean getConnected() {
		return connected;
	}
	
	public PuzzlePiece getCurrent() {
		return current;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public LinkedList<PuzzlePiece> getPath() {
		return path;
	}
	
	@Override
	public boolean isDomainSingleton() {
		if(isConnected())
			return false;
		return (domain.size() == 1);
	}

	@Override
	public void notifyDomainChanged() {
//		if(isDomainSingleton())
//			move((PuzzlePiece)getDomain().get(0));
		
	}

}

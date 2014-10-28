package module2_vertexColoring;

import generalArcConsistency.Variable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Vertex extends Variable{
	
	public double x;
	public double y;

	public Color color;
	
	/**		Vertex constructor
	 * 
	 * @param id		The vertex' id
	 * @param x			X-coordinates
	 * @param y			Y-coordinates
	 * @param domain	the Colors in the domain
	 * @param color		If undecided, use @null as parameter
	 */
	public Vertex(int id, double x, double y, List<Color> domain, Color color) {
		super(id, domain);
		this.x = x;
		this.y = y;
		
		if(color != null)
			this.color = color;
		
		neighbors = new ArrayList<Variable>();
	}
	
	@Override
	public boolean isDomainSingleton() {
		boolean singleton = (domain.size() == 1);
		if(singleton) {
			color = (Color)domain.get(0);
		}
		return singleton;
	}
	
	/**		Make a deep copy of the Vertex
	 * 
	 * @return a copy of the Vertex.
	 */
	@Override
	public Vertex deepCopy() {
		List<Color> copyColor = new ArrayList<Color>();
		for(Object o : domain)
			copyColor.add((Color)o);
		Vertex copy = new Vertex(ID, x, y, copyColor, color);
		return copy;
	}
	
	 @Override
	public String toString() {
		char[] cha = new char[6];
		for(int i=0;i<cha.length; i++)
			cha[i] = '0';
		for(Object c : domain.toArray()) {
			if(c == Color.BLUE)
				cha[0] = '1';
			if(c == Color.RED)
				cha[1] = '2';
			if(c == Color.GREEN)
				cha[2] = '3';
			if(c == Color.YELLOW)
				cha[3] = '4';
			if(c == Color.MAGENTA)
				cha[4] = '5';
			if(c == Color.ORANGE)
				cha[5] = '6';			
		}
		String string = (ID+1000)+"";
		for(char c : cha)
			string = string+c;
		return string;
	}

	@Override
	public void notifyDomainChanged() {
		// TODO Auto-generated method stub
		
	}

}


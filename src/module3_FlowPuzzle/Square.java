package module3_FlowPuzzle;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Square extends JPanel {
	
	public enum Type {
		BASE,
		NORMAL,
		HEAD
	}
	
	public final int X;
	public final int Y;
	private Type type = Type.NORMAL;
	
	private Color color = Color.WHITE;
	
	private int SIZE = 100;
	
	public Square(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type t) {
		this.type = t;
	}
	
	public void setColor(Color c) {
		if(color.equals(c))
			return;
		this.color = c;
		paintComponent(getGraphics());
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(color);
		g.fillRect(1, 1, SIZE, SIZE);
		if(type == Type.BASE) {
			g.setColor(Color.BLACK);
			g.fillOval(20, 20, 60, 60);
		}
/*		else if(type == Type.HEAD) {
			g.setColor(Color.GRAY);
			g.drawOval(1, 1, SIZE, SIZE);
		}
*/	}

}

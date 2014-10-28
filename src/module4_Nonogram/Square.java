package module4_Nonogram;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Square extends JPanel{
	
	private boolean filled = false;
	
	public final int X;
	public final int Y;
	
	private int SIZE = 30;
	
	public Square(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	
	public void setFilled(boolean fill) {
		this.filled = fill;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(filled)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.WHITE);
		g.fillOval(1, 1, SIZE, SIZE);
	}

}

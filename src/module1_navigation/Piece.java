package module1_navigation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

public class Piece extends JButton {
	
	public enum Type {
		EMPTY, PATH, HEAD, OBSTACLE, START, GOAL
	}
		
	private Type type = Type.EMPTY;
	private int size;
	
	public Piece(Dimension d) {
		super(new ImageIcon(
                new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB)));
		setSize(20,20);
		size = d.height;
		this.disable();
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void draw() {
		
		switch(type) {
			case OBSTACLE: {
				setBackground(Color.BLACK);
				break;
			}
			case START: {
				setBackground(Color.RED);
				break;
			}
			case GOAL: {
				setBackground(Color.GREEN);
				break;
			}
			case PATH:
			case HEAD: {
				setBackground(Color.BLUE);
				break;
			}
			default: { //EMPTY
				setBackground(Color.WHITE);
				break;
			}
		}
	}

}

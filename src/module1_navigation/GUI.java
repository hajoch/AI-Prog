package module1_navigation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import aStar.Node;
import aStar.Problem;
import aStar.Result;

public class GUI extends JFrame {
	
	final String TITLE = "A* Algorithm - Navigation";
	final int HEIGHT = 800;
	final int WIDTH = 600;
	
	JPanel container = new JPanel();
	JPanel topPanel;
	JPanel mainPanel;
	JPanel bottomPanel;
	
	JButton bestFirstButton;
	JButton depthFirstButton;
	JButton breadthFirstButton;
	
	JLabel resultLabel;
	JLabel headlineLabel;
	
	private Piece[][] grid;
	
	private NavProblem problem;
	private Result result;
	
	public GUI(NavProblem problem, Result result) {
		this.problem = problem;
		this.result = result;
		initializeGUI();
	}

	public void initializeGUI() {
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension size = problem.size;
		
		int biggest = (int)Math.floor( (Math.max(size.height, size.width)/WIDTH) );
		Dimension peiceDim = new Dimension(biggest, biggest);
		
		grid = new Piece[size.height][size.width];
		
		
		for(int y=0; y<grid.length; y++) {
			for(int x=0; x<grid[0].length; x++) {
				grid[y][x] = new Piece(peiceDim);
			}
		}
		
		
		
		for(int oo=0; oo<size.height; oo++) {
			for(int o=0; o<size.width; o++) {
				if(problem.getObstacles()[oo][o])
					grid[oo][o].setType(module1_navigation.Piece.Type.OBSTACLE);
			}
		}
		
		for(Node n : result.path()) {
			NavState ns = (NavState)n.getState();
			grid[ns.getLocation().y][ns.getLocation().x].setType(module1_navigation.Piece.Type.PATH);
		}
		grid[problem.start.y][problem.start.x].setType(module1_navigation.Piece.Type.START);
		grid[problem.goal.y][problem.goal.x].setType(module1_navigation.Piece.Type.GOAL);
		
		mainPanel = new JPanel(new GridLayout(size.height, size.width));
		mainPanel.setBorder(new LineBorder(Color.BLACK));
		
		Collections.reverse(Arrays.asList(grid));
		
		for( Piece[] pp : grid) {
			for( Piece p : pp) {
				p.draw();
				mainPanel.add(p);
				p.setVisible(true);
			}
		}
		//container
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		//top Panel
		headlineLabel = new JLabel("Title");
		headlineLabel.setText(TITLE);
		topPanel = new JPanel();
		topPanel.add(headlineLabel);
		headlineLabel.setVisible(true);
		
		//bottom panel
		JTextField resultDisp = new JTextField();
		resultDisp.setText("Completed in "+result.iterations()+" iterations");
		bottomPanel = new JPanel(new GridLayout(1,1));
		bottomPanel.add(resultDisp);
		
		//container.add(topPanel);
		container.add(mainPanel);
		container.add(bottomPanel);
		add(container);
		
/*		for(int ii=0; ii<size.height; ii++) {
			for(int jj=0; jj<size.width; jj++) {
				grid
			}
		}
*/		
		
		pack();
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		setVisible(true);
	}
}

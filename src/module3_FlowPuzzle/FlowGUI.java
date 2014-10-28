package module3_FlowPuzzle;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;

import generalArcConsistency.AstarGAC;
import generalArcConsistency.StateChangeListener;
import generalArcConsistency.Variable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import aStar.State;

public class FlowGUI extends JFrame {
	
	private final FlowPuzzle puzzle;
	private final Square[][] squares;
	private final AstarGAC astar;
	
	private FlowState prev;
	
	JPanel panel = new JPanel();
	
	public FlowGUI(FlowPuzzle puzzle, AstarGAC starFlow) {
		super("Flow Puzzle Solver");
		this.puzzle = puzzle;
		this.astar = starFlow;
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		panel.setLayout(new GridLayout(puzzle.SIZE, puzzle.SIZE,0,0));
		
		squares = new Square[puzzle.SIZE][puzzle.SIZE];
		init();
		add(panel);
		setVisible(true);
		setBounds(0, 0, puzzle.SIZE*100, puzzle.SIZE*100);
		
		puzzle.addListener(new StateChangeListener() {
			
			@Override
			public void onStateChanged(State state) {
				FlowState flowState = (FlowState)state;
				
				if(prev != null) {
					for(FlowHead prevH : (HashSet<FlowHead>)prev.getVariables()) {
						for(PuzzlePiece pp : prevH.getPath()) {
							squares[pp.Y][pp.X].setColor(Color.WHITE);
						}
					}
				}

				HashSet<FlowHead> vars = (HashSet<FlowHead>) flowState.getVariables();
				for(FlowHead fh : vars) {
					int i=0;
					for(PuzzlePiece pp : fh.getPath()) {
						//TODO	Selective update.
						Square squ = squares[pp.Y][pp.X];
						
						if(i==0)
							squ.setType(module3_FlowPuzzle.Square.Type.BASE);
						else if(i==fh.getPath().size()-1)
							squ.setType(module3_FlowPuzzle.Square.Type.HEAD);
						else
							squ.setType(module3_FlowPuzzle.Square.Type.NORMAL);
						
						squ.setColor(fh.color);
						i++;
					}
				}
				prev = flowState;
			}
		});
	}
	
	public void init() {
		for(int y=0; y<squares.length; y++) {
			for(int x=0; x<squares.length; x++) {
				Square s = new Square(x, y);
				squares[y][x] = s;
				panel.add(s);
			}
		}
	}

}

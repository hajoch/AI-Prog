package module3_FlowPuzzle;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import module2_vertexColoring.ProblemGenerator;

public class ProblemGeneratorFP extends ProblemGenerator {
	
	private FlowPuzzle problem;
	
	private final String path = "C:\\Users\\Hallvard\\Dropbox\\Workspace\\Java\\IT3105\\src\\module3_FlowPuzzle\\problems";
	
	public ProblemGeneratorFP(){
		super(null);
	}
	
	private FlowPuzzle constructPuzzle(ArrayList<String> input) {
		
		int[] base = getArray(input.get(0));
		int size = base[0];
		int flows = base[1];
		
		int[][] puzzleInit = new int[flows][5];
		for(int i=0; i<flows; i++) {
			puzzleInit[i] = (int[])getArray(input.get(i+1));
		}
		
		//initializing board.
				PuzzlePiece[][] puzzle = new PuzzlePiece[size][size];
				for(int y=0; y<size; y++)
					for(int x=0; x<size; x++)
						puzzle[y][x] = new PuzzlePiece(x, y);
				
				//Adding connected pieces. domain creation.
				for(PuzzlePiece[] row : puzzle) {
					for(PuzzlePiece piece : row) {
						ArrayList<PuzzlePiece> connectedPieces = new ArrayList<PuzzlePiece>();
						for(int i=-1; i<2; i+=2) {
							if(piece.X+i < size && piece.X+i >= 0)
								connectedPieces.add(puzzle[piece.Y][piece.X+i]);
							if(piece.Y+i < size && piece.Y+i >= 0)
								connectedPieces.add(puzzle[piece.Y+i][piece.X]);
						}
						piece.setConnectedPieces(connectedPieces);
					}
				}
		
		//Generating the variables from the raw DATA
		HashSet<FlowHead> variables = new HashSet<FlowHead>();
		for(int[] flow : puzzleInit) {			
			Color color = getColor(flow[0]);
			FlowHead fh1 = new FlowHead((flow[0]*100)+1, (List<?>) puzzle[flow[1]][flow[2]].getConnectedPieces().clone(), puzzle[flow[1]][flow[2]], color, null, true);	//The guessing variable of its color.
			FlowHead fh2 = new FlowHead((flow[0]*100)+2, (List<?>) puzzle[flow[3]][flow[4]].getConnectedPieces().clone(), puzzle[flow[3]][flow[4]], color, null, false);
			fh1.setGoal(fh2);	//Set goal-state for variable
			fh2.setGoal(fh1);
			variables.add(fh1);	//Adding variable
			variables.add(fh2);
		
		}for(FlowHead fh : variables) {
			for(FlowHead fhn : variables) {
//				if(!fh.equals(fhn))
					fh.addNeighbor(fhn);
			}
		}
		
		return new FlowPuzzle(size, flows, puzzle, variables);
	}
	
	public FlowPuzzle getPuzzle() {
		if(problem != null)
			return problem;
		//Select file
		File file = selectFile(path);
				
		//Read File
		ArrayList<String> input;
		try {
			input = readFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		problem = constructPuzzle(input);
		return problem;
	}
	
	protected int[] getArray(String s) {
		String[] array = s.split(" ");
		int[] values = new int[array.length];
		for(int i=0; i<array.length; i++) {
			values[i] = Integer.parseInt(array[i]);
		}
		return values;
	}
	
	private Color getColor(int nmbr) {
		switch(nmbr) {
		case 0:		return Color.GREEN;
		case 1:		return Color.YELLOW;
		case 2:		return Color.RED;
		case 3:		return Color.BLUE;
		case 4:		return Color.ORANGE;
		case 5:		return Color.MAGENTA;
		case 6:		return Color.CYAN;
		case 7:		return Color.PINK;
		case 8:		return Color.GRAY;
		case 9:		return Color.LIGHT_GRAY;
		default:	return Color.DARK_GRAY;
		}
	}

}

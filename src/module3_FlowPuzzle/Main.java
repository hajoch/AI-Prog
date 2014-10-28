package module3_FlowPuzzle;

import generalArcConsistency.AstarGAC;
import aStar.Result;

public class Main {
	
	public static void main(String[]args) {
		
		ProblemGeneratorFP generator = new ProblemGeneratorFP();
		FlowPuzzle puzz = generator.getPuzzle();
		
//		printInit(puzz);
		FlowGUI gui = new FlowGUI(puzz, null);
		
		AstarGAC algorithm = new AstarGAC(puzz);
		
		Result result = algorithm.bestFirstSearch();
		
		System.out.println("ENDED");
		
	}
}

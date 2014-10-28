package module2_vertexColoring;

import java.awt.Color;
import java.util.ArrayList;

import aStar.Result;
import generalArcConsistency.AstarGAC;
import generalArcConsistency.Revise;

public class Main {
	
	private static GUI gui;
	
	public static void main(String[]args) {
		
		solveProblem();
	}
		
	private static void solveProblem() {
		ProblemGenerator generator = new ProblemGenerator(gui); //TODO
		VCProblem problem = generator.getProblem();		
		
		gui = new GUI(problem);
		
		problem.gui = gui;

		gui.redrawGraph();
		
		AstarGAC algorithm = new AstarGAC(problem);
		Result result = algorithm.bestFirstSearch();

	//	gui.solution((VCState) result.solution());
		
		System.out.println("RESULT: "+result.report()+ " "+result.iterations());
	}

}

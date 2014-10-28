package module1_navigation;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import aStar.Algorithm;
import aStar.Node;
import aStar.Result;

public class Main {
	
	private static Algorithm algorithm;
	private static NavProblem problem;
	private static Result result;
	private static GUI gui;
	
	public static void main(String[] args) {
		
		problem = getUserInput();
		
		algorithm = new Algorithm(problem);
		
		printMap();
		
		result = algorithm.breadthFirstSearch();
		
		System.out.println(result.report());
		NavState ns = (NavState)(result.solution());

		if(ns != null) {
		Point p = ns.getLocation();
		
		System.out.println("Iterations: "+result.iterations() + " | Coordinates: "+p.toString());
		}
		int i = 0;
		for(Node n : result.path()) {
			System.out.println(i+" path: "+n.getState().getId());
			i++;
		}
		System.out.println("path: "+i);
		
		Runnable r = new Runnable() {	
			@Override
			public void run() {
				gui = new GUI(problem, result);
			}
		};
		SwingUtilities.invokeLater(r);
	}
	
	private static void printMap() {
		boolean[][] test = problem.testGrid();
		for(int y=test.length-1; y>=0; y--) {
			for(int x=0; x<test[0].length; x++) {
				if(test[y][x])
					System.out.print("X");
				else
					System.out.print("0");
			}
			System.out.println();
		}
	}
	


	private static NavProblem getUserInput() {
		Scanner input = new Scanner(System.in);
		
		//Variables
		int[] gridSize = answer("Grid size: (x-y)", input, false);
		int[] startPoint = answer("Starting point: (x-y)", input, false);
		int[] goalPoint = answer("Goal position: (x-y)", input, false);
		ArrayList<int[]> obstacles = getObstacles(input);
		
		input.close();
		
		NavProblem problem = new NavProblem(new Dimension(gridSize[0], gridSize[1])
										, new Point(startPoint[0], startPoint[1])
										, new Point(goalPoint[0], goalPoint[1])
										, obstacles);
				
		return problem;
		
	}
	
	private static int[] answer(String message, Scanner input, boolean neverEnding) {
		System.out.println(message);
		String response;
		//Get valid values
		while(!validInput(response = input.nextLine())) {
			if(neverEnding && response.equals("exit")) {
				System.out.println("return null");
				return null;				
			}
			else
				System.out.println("Invalid input. Try again.");
		}
		String[] strings = response.split("-");
		//Convert to integers
		int[] values = new int[strings.length];
		for(int i=0; i<strings.length; i++)
			values[i] = Integer.parseInt(strings[i]);
		return values;
	}
	
	private static ArrayList<int[]> getObstacles(Scanner input) {
		final String MESSAGE = "To put obstacle on grid, input values (x-y-w-h). To exit write (exit)";
		ArrayList<int[]> obstacles = new ArrayList<int[]>();
		while(true) {
			int[] obstacle = answer(MESSAGE, input, true);
			if(obstacle  == null)
				break;
			obstacles.add(obstacle);
		}
		return obstacles;
	}
	
	private static boolean validInput(String s) {
		if(s.equals("exit")) //TODO REGEX
			return false;
		return true;
//		final String REGEX = ".*[^0-9-].*";
//		return Pattern.matches(REGEX, s);
	}

}



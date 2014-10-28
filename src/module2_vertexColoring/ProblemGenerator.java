package module2_vertexColoring;

import generalArcConsistency.Constraint;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProblemGenerator {
	
	private VCProblem problem;
	private GUI parent;
	
	private int domainSize;
	
	public ProblemGenerator(GUI gui) {
		
		parent = gui;
		
	}
	
	public VCProblem getProblem() {
		
		if(problem != null)
			return problem;
		//Select File
		File file = selectFile("C:\\Users\\Hallvard\\Dropbox\\Workspace\\Java\\IT3105\\src\\module2_vertexColoring");
		
		//Read File
		ArrayList<String> input;
		try {
			input = readFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
		getDomainSize();
	
		//Generate Problem
		problem = generateProblem(input);
		return problem;
	}
	
	private void getDomainSize() {
		JOptionPane pane = new JOptionPane();
		String d;
		while(!isInteger(d = pane.showInputDialog("Input domain Size"))) {
			JOptionPane.showMessageDialog(null, "Input not a number, try again");
		}
		domainSize = Integer.parseInt(d);
	}
	
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	protected File selectFile(String path) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Textfiles", "txt");
		chooser.setCurrentDirectory(new File(path));
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return selectFile(path);
		}
	}
	
	protected ArrayList<String> readFile(File file) throws Exception {
		Reader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		ArrayList<String> input = new ArrayList<String>();
		String s;
		while((s = bufferedReader.readLine()) != null) {
			input.add(s);
		}
		bufferedReader.close();
		reader.close();
		return input;
	}
	
	private VCProblem generateProblem(ArrayList<String> input) {
		Object[] first = getValues(input.get(0), false);
		int nv = (Integer)first[0];			//Number of vertices
		int ne = (Integer)first[1];			//Number of edges
		
		 //Normalize Vertices
        double negativeX = 99999999;
        double negativeY = 99999999;
        for(int i=1; i<nv+1; i++) {
            Object[] values = getValues(input.get(i), true);
            if ((Double)values[1] < negativeX){
                negativeX = (Double)values[1];
            }
            if ((Double)values[2] < negativeY){
                negativeY = (Double)values[2];
            }
        }
		
		//Vertices
		HashSet<Vertex> vertices = new HashSet<Vertex>();
		for(int i=1; i<nv+1; i++) {
			Object[] values = getValues(input.get(i), true);
			vertices.add(new Vertex((Integer)values[0], (Double)values[1]+(negativeX*-1), (Double)values[2]+(negativeY*-1), getDomain(), Color.GRAY));
		}
		
		//Edges
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		for(int j=nv+1; j<input.size(); j++) {
			Object[] values = getValues(input.get(j), false);
			edges.add(new Edge((Integer)values[0], (Integer)values[1]));
			//Constraints
			HashMap<String, Integer> containingVariablesId = new HashMap<String, Integer>();
            containingVariablesId.put("x", (Integer) values[0]);
            containingVariablesId.put("y", (Integer) values[1]);
            constraints.add(new Constraint(containingVariablesId,"x != y"));
		}
		
		System.out.println(vertices);
		return new VCProblem(constraints, vertices, edges, domainSize, parent);
	}
	
	protected Object[] getValues(String s, boolean vertex) {
		String[] array = s.split(" ");
		Object[] values = new Object[array.length];
		for(int i=0; i<array.length; i++) {
			if(vertex && i!=0)
				values[i] = Double.parseDouble(array[i]);
			else
				values[i] = Integer.parseInt(array[i]);
		}
		return values;
	}
	public List<Color> getDomain() {
		ArrayList<Color> domain = new ArrayList<Color>();
		domain.add(Color.RED);
		domain.add(Color.BLUE);
		domain.add(Color.GREEN);
		domain.add(Color.YELLOW);
		domain.add(Color.MAGENTA);
		domain.add(Color.ORANGE);
		domain.add(Color.CYAN);
		domain.add(Color.WHITE);
		domain.add(Color.BLACK);
		return domain.subList(0, domainSize);
	}
}

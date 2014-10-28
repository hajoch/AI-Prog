package interpreter;

public class Interpreter {
	
	
	public static boolean interpret(String constraint, Object[] vars) {
		
		String[] bits = constraint.split(" ");
        if (bits[0].equals("x") && bits[1].equals("!=") && bits[2].equals("y")){
            return !vars[0].equals(vars[1]);
        }
        System.out.println("Interpreter failed");
        return false;
	}	
}

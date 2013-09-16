import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	private char[][] board; //Easier access instead of string
	private Map<State,Integer> visitedStates;
	
	public static void main(String[] args) throws IOException {
		Vector<String> board = new Vector<String>();
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		
		String line;
		while(br.ready()) {
			line = br.readLine();
			board.add(line);
		} // End while
		
		// Access
		//char = board.get(row).charAt(col);
			
		System.out.println("U R R UASDNJASNDJK");
	} // main
	
	
	
	/**
	 * Solves the map and returns the path as a String
	 * @return
	 */
	public static String solveMap(){
		return null;
	}
	/**
	 * Translate from Vector<String> to char[][]
	 * Check for corners and dangerous zones
	 * Updates the "board" (char[][]) attribute.
	 * @param board
	 */
	public static void parseBoard(Vector<String> board){
		
	}
	
	
	/**
	 * (FUTURE WORK, START WITH BFS)
	 * Estimate how good is a state 
	 * @param state
	 * @return
	 */
	public static int stateHeuristic(State state){
		return -1;
	}
	
	
	/**
	 * Returns the path from a box from a goal
	 * @param position
	 * @param goal
	 * @return the path to the goal, or null if it doesn't exit.
	 */
	public static String moveBox(Position boxPosition, Position goal){
		//Check that boxPosition is actually a box
		//Check that it has at least 2 white spaces in order to move it
		return null;
	}
	
	/**
	 * Checks if a box on a position pos[] (pos[0] and pos[1])
	 * can be moved.
	 * 
	 * @param pos
	 * @return true if there is any way to move that box
	 */
	public static boolean isIllegal(int[] pos){
		return false;
	}
} // End Main
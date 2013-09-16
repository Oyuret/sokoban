import java.io.*;
import java.util.Set;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	private char[][] board; //Easier access instead of string
	private Set<State> visitedStates;
	
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
	 * Check for corners and dangerous zones
	 * Updates the "board" (char[][]) attribute.
	 * @param board
	 */
	public static void parseBoard(Vector<String> board){
		
	}
	
	/**
	 * Move the player and updates the map
	 */
	public static void movePlayer(){
		
	}
	
	
	/**
	 * Estimate how good is a state
	 * @param state
	 * @return
	 */
	public static int stateHeuristic(State state){
		return -1;
	}
} // End Main
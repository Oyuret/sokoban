import java.io.*;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
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
	
	
	public void parseBoard(Vector<String> board){
		
	}
} // End Main
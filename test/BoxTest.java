import java.util.List;
import java.util.PriorityQueue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BoxTest {
	//No moves board
	String[] b1 = {"###",
				   "#$#",
				   "###"};
	String[] b2 = {"####",
			       "#$ #",
			       "####"};
	String[] b3 = {"####",
			       "#$ #",
			       "#  #",
			       "####"}; //Corner
	//Two moves board
	String[] b4 = {"#####",
		       	   "# $ #",
		           "#   #",
		           "#####"};
	String[] b5 = {"#####",
	       	   	   "#   #",
	               "#$  #",
	               "#   #",
	               "#####"};
	//Four moves board
	String[] b6 = {"#####",
				   "#   #",
                   "# $ #",
                   "#   #",
                   "#####"};
	
	//Can't go to goal
	String[] b7 = {"#####",
				   "#   #",
				   "#   #",
				   "#$ .#",
				   "#####"};

	String[] b8 = {"#####",
				   "#   #",
				   "# . #",
				   "#$  #",
				   "#####"};	
	//Goal on corner
	String[] b9 = {"#####",
				   "#   #",
				   "#   #",
				   "# $.#",
				   "#####"};		
	String[] b10 ={"#####",
				   "#.  #",
				   "#$  #",
				   "#   #",
				   "#####"};	
	//Goal on edge
	String[] b11 = 	  {"######",
					   "#    #",
					   "# $ .#",
					   "#    #",
					   "######"};	
	//Goal on edge
	String[] b12 = 	  {"######",
					   "#    #",
					   "# $  #",
					   "# .  #",
					   "######"};
	//Goal on tunnel
	String[] b13 = 	  {"#############",
					   "#          .#",
					   "# $  ########",
					   "#    #",
					   "######"};	
	//
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testBoxStateOrdering() {
		Position goal = new Position(5, 0);
		BoxState s1 = new BoxState(new Position(0, 0), "", goal);
		BoxState s2 = new BoxState(new Position(5, 0), "", goal);
		BoxState s3 = new BoxState(new Position(4, 0), "", goal);
		BoxState s4 = new BoxState(new Position(3, 0), "", goal);
		BoxState s5 = new BoxState(new Position(2, 0), "", goal);
		BoxState s6 = new BoxState(new Position(1, 0), "", goal);

		PriorityQueue<BoxState> queue = new PriorityQueue<BoxState>();
		queue.add(s1);
		queue.add(s6);
		queue.add(s5);
		queue.add(s2);
		queue.add(s3);
		queue.add(s4);
		BoxState nextState = queue.poll();
		assertEquals(nextState,s2);
		nextState = queue.poll();
		assertEquals(nextState,s3);
		nextState = queue.poll();
		assertEquals(nextState,s4);
		nextState = queue.poll();
		assertEquals(nextState,s5);
		nextState = queue.poll();
		assertEquals(nextState,s6);
		nextState = queue.poll();
		assertEquals(nextState,s1);
	}
	@Test
	public void testNextBoxStates() {
		List<BoxState> moves;
		char[][]board;
		BoxState box = new BoxState(new Position(0,0), "", null);
		Position goal = new Position(0, 0);
		
		//No moves		
		board = new char[b1.length][b1[0].length()];
		createCharBoard(b1, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(0, moves.size());

		board = new char[b2.length][b2[0].length()];
		createCharBoard(b2, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(0, moves.size());
		
		board = new char[b3.length][b3[0].length()];
		createCharBoard(b3, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(0, moves.size());
		//Two moves
		
		board = new char[b4.length][b4[0].length()];
		createCharBoard(b4, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(2, moves.size());

		board = new char[b5.length][b5[0].length()];
		createCharBoard(b5, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(2, moves.size());
		
		//Four moves
		board = new char[b6.length][b6[0].length()];
		createCharBoard(b6, board,box,goal);
		Main.setMap(board);
		moves = box.getNextBoxStates();
		assertEquals(4, moves.size());
	}
	
	
	@Test
	public void testBoxToGoal() {
		char[][]board;
		BoxState box = new BoxState(new Position(0,0), "", null);
		Position goal = new Position(0, 0);
		String path;
		
		//Can't go to goal
		board = new char[b7.length][b7[0].length()];
		createCharBoard(b7, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assertNull(path);
		
		board = new char[b8.length][b8[0].length()];
		createCharBoard(b8, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assertNull(path);
		
		//Goal on corner
		board = new char[b9.length][b9[0].length()];
		createCharBoard(b9, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assertEquals("R", path);
		
		board = new char[b10.length][b10[0].length()];
		createCharBoard(b10, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assertEquals("U", path);
		
//		//Goal on edge
//		board = new char[b11.length][b11[0].length()];
//		createCharBoard(b11, board,box,goal);
//		Main.setMap(board);
//		path = Main.moveBox(box.getPosition(), goal);
//		assertEquals("RR", path);
		
		board = new char[b12.length][b12[0].length()];
		createCharBoard(b12, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assertEquals("D", path);
		
		//Goal on tunnel
		board = new char[b13.length][b13[0].length()];
		createCharBoard(b13, board,box,goal);
		Main.setMap(board);
		path = Main.moveBox(box.getPosition(), goal);
		assert(path.equals("UDDDDDDDDDD") || path.equals("RDDDDDDDDDD"));
		
	}
	
	
	/*------------------------*/
	public void createCharBoard(String[] lines,char[][]_board, BoxState box, Position goal){
		for(int i=0;i<lines.length;i++){
			for(int j=0;j<lines[0].length();j++){
				char c;
				if(j>=lines[i].length()){
					c = ' ';
				}else{
					c = lines[i].charAt(j);
				}
				_board[i][j] = c;
				if(c=='$')
					box.setPosition(new Position(i, j));
				if(c=='.'){
					goal.setCol(j);
					goal.setRow(i);
				}
			}
		}
	}

}

import static org.junit.Assert.*;

import java.util.PriorityQueue;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BoxTest {

	char[][] charBoard;
	
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
		//No moves		
		
		//One move
		
		//Two moves
		
		//Three moves
		
		//Four moves
	}
	
	
	@Test
	public void testBoxToGoal() {
		fail("Not yet implemented");
	}

}

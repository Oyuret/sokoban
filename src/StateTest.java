/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yuri
 */
public class StateTest {
    
    public StateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNextMoves method, of class State.
     */
//    @Test
//    public void testGetNextMoves() {
//        System.out.println("getNextMoves");
//        List<State> nextStates = null;
//        State instance = null;
//        instance.getNextMoves(nextStates);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of hashCode method, of class State.
//     */
//    @Test
//    public void testHashCode() {
//        System.out.println("hashCode");
//        State instance = null;
//        int expResult = 0;
//        int result = instance.hashCode();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of equals method, of class State.
//     */
//    @Test
//    public void testEquals() {
//        System.out.println("equals");
//        Object obj = null;
//        State instance = null;
//        boolean expResult = false;
//        boolean result = instance.equals(obj);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of clone method, of class State.
     */
    @Test
    public void testClone() throws Exception {
        System.out.println("clone");
        
        // test cloning same objects
        State instance = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,2), new Position(3,3))), "BOGUS");
        State expResult = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,2), new Position(3,3))), "BOGUS");
        State result = (State) instance.clone();
        assertEquals(expResult.getPlayer(), result.getPlayer());
        assertEquals(expResult.getCurrent_path(), result.getCurrent_path());
        Position[] resultList = new Position[result.getBoxes().size()];
        Position[] expResulttList = new Position[expResult.getBoxes().size()];
     
        for(int i=0; i< expResult.getBoxes().size(); i++) {
            resultList[i] = result.getBoxes().poll();
            expResulttList[i] = expResult.getBoxes().poll();
        }
        
        for(int i=0; i<expResulttList.length; i++) {
            assertEquals(resultList[i], expResulttList[i]);
        }
        
        
        // make sure cloning made a deep copy
        instance = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,2), new Position(3,3))), "BOGUS");
        expResult = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,2), new Position(3,3))), "BOGUS");
        result = (State) instance.clone();
        
        // alter the original
        instance.setPlayer(new Position(10,10));
        instance.setCurrent_path("BOGUSBOGUS");
        instance.getBoxes().clear();
        assertEquals(expResult.getPlayer(), result.getPlayer());
        assertEquals(expResult.getCurrent_path(), result.getCurrent_path());
        
        for(int i=0; i< expResult.getBoxes().size(); i++) {
            resultList[i] = result.getBoxes().poll();
            expResulttList[i] = expResult.getBoxes().poll();
        }
        
        for(int i=0; i<expResulttList.length; i++) {
            assertEquals(resultList[i], expResulttList[i]);
        }
    }

    /**
     * Test of getBoxes method, of class State.
     */
//    @Test
//    public void testGetBoxes() {
//        System.out.println("getBoxes");
//        State instance = null;
//        PriorityQueue expResult = null;
//        PriorityQueue result = instance.getBoxes();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPlayer method, of class State.
//     */
//    @Test
//    public void testGetPlayer() {
//        System.out.println("getPlayer");
//        State instance = null;
//        Position expResult = null;
//        Position result = instance.getPlayer();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCurrent_path method, of class State.
//     */
//    @Test
//    public void testGetCurrent_path() {
//        System.out.println("getCurrent_path");
//        State instance = null;
//        String expResult = "";
//        String result = instance.getCurrent_path();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of finished method, of class State.
//     */
//    @Test
//    public void testFinished() {
//        System.out.println("finished");
//        State instance = null;
//        boolean expResult = false;
//        boolean result = instance.finished();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}

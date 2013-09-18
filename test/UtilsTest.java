/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
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
public class UtilsTest {
    
    public UtilsTest() {
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
     * Test of findPath method, of class Utils.
     */
    @Test
    public void testFindPath() {
        System.out.println("findPath");
        Position start = null;
        Position goal = null;
        State currentState = null;
        String expResult = "";
        String result = Utils.findPath(start, goal, currentState);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tryToMoveBox method, of class Utils.
     */
    @Test
    public void testTryToMoveBox() {
        System.out.println("tryToMoveBox");
        Position box = null;
        Position player = null;
        State currentState = null;
        Movement expResult = null;
        Movement result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdjucentPositions method, of class Utils.
     */
    @Test
    public void testGetAdjucentPositions() {
        System.out.println("getAdjucentPositions");
        Position box = null;
        State currentState = null;
        List expResult = null;
        List result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

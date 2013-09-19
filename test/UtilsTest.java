/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
//    @Test
//    public void testFindPath() {
//        System.out.println("findPath");
//        Position start = null;
//        Position goal = null;
//        State currentState = null;
//        String expResult = "";
//        String result = Utils.findPath(start, goal, currentState);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of tryToMoveBox method, of class Utils.
     */
    @Test
    public void testTryToMoveBox() {
        System.out.println("tryToMoveBox");
        String board2 = "   ####   ####  ##  #   "
                + "   #  #      #  #  # # ## ##      #  ##     "
                + "#   #   ###   #   #     #####   ";
        populateBoard(board2, 10);
        for(char[] s : Main.board) {
            System.out.print(s);
            System.out.println();
        }
        
        // Box left of player (no room to move, WALL)
        Position box = new Position(2, 1);
        Position player = new Position(2, 2);
        State currentState = new State(player, new PriorityQueue<Position>(), "");
        Movement expResult = null;
        Movement result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box left of player (no room to move, BOX)
        box = new Position(2, 2);
        player = new Position(2, 3);
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2,1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box left of player (room to move)
        box = new Position(2, 2);
        player = new Position(2, 3);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2,1), new Position(2,2), "L");
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        
        // Box north of player (no room to move, WALL)
        box = new Position(2, 1);
        player = new Position(3, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box north of player (no room to move, BOX)
        box = new Position(3, 1);
        player = new Position(4, 1);
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2,1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box north of player (room to move)
        box = new Position(3, 1);
        player = new Position(4, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2,1), new Position(3,1), "U");
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box right of player (no room to move, WALL)
        box = new Position(2, 6);
        player = new Position(2, 5);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box right of player (no room to move, BOX)
        box = new Position(2, 5);
        player = new Position(2, 4);
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2,6))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box right of player (room to move)
        box = new Position(2, 5);
        player = new Position(2, 4);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2,6), new Position(2,5), "R");
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box south of player (no room to move, WALL)
        box = new Position(4, 1);
        player = new Position(3, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        // Box south of player (no room to move, BOX)
        box = new Position(3, 1);
        player = new Position(2, 1);
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(4,1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
        
        
        // Box south of player (room to move)
        box = new Position(3, 1);
        player = new Position(2, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(4,1), new Position(3,1), "D");
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAdjucentPositions method, of class Utils.
     */
    @Test
    public void testGetAdjucentPositions() {
        System.out.println("getAdjucentPositions");
        String board2 = "   ####   ####  ##  #   "
                + "   #  #      #  #  # # ## ##      #  ##     "
                + "#   #   ###   #   #     #####   ";
        populateBoard(board2, 10);
        for(char[] s : Main.board) {
            System.out.print(s);
            System.out.println();
        }
        
        // All four positions are empty
        Position box = new Position(3,4);
        State currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        List expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(3,3), new Position(4,4), new Position(2,4)));
        List result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box below
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(4,4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(3,3), new Position(2,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box to the left
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(3,3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(4,4), new Position(2,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box to the right
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(3,5))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,3), new Position(4,4), new Position(2,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box up
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(3,3), new Position(4,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box up and down
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,4), new Position(4,4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(3,3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Box left and right
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(3,5), new Position(3,3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(4,4), new Position(2,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // boxes everywhere
        box = new Position(3,4);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(4,4), new Position(2,4), new Position(3,5), new Position(3,3))), "");
        expResult = new ArrayList<>();
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // wall to the left
        box = new Position(3,1);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,2), new Position(4,1), new Position(2,1)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // wall to the right
        box = new Position(3,6);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3,5), new Position(4,6), new Position(2,6)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // wall down
        box = new Position(6,6);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(6,7), new Position(6,5), new Position(5,6)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // wall up
        box = new Position(2,2);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(2,3), new Position(2,1), new Position(3,2)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Corner up right
        box = new Position(1,5);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(1,4), new Position(2,5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Corner up left
        box = new Position(1,4);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(1,5), new Position(2,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Corner down right
        box = new Position(8,5);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8,4), new Position(7,5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Corner down left
        box = new Position(8,3);
        currentState = new State(new Position(1,1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8,4), new Position(7,3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Mix only UP empty
        box = new Position(8,3);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(7,3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Mix only RIGHT empty
        box = new Position(8,3);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(8,4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(7,3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Mix only LEFT empty
        box = new Position(8,5);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(7,5))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8,4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Mix only DOWN empty
        box = new Position(1,5);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(1,4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(2,5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
        
        // Mix NO SPACES
        box = new Position(1,5);
        currentState = new State(new Position(1,1), new PriorityQueue<>(Arrays.asList(new Position(2,5), new Position(1,4))), "");
        expResult = new ArrayList<>();
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);
    }

    void populateBoard(String lines, int number) {
        Main.board = new char[number][number];
        int i = 0;
        for (int y = 0; y < number; y++) {
            for (int x = 0; x < number; x++) {
                Main.board[y][x] = lines.charAt(i);
                i++;
            }
        }
    }
}

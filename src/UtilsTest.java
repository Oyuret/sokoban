/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
    @Test
    public void testFindPath() {
        System.out.println("findPath");
        System.out.println("Testing following board");
        System.out.println("Finding paths from all boxes to all boxes\n Even themselves");
        String board2 = "   ####   ####$ ##  #$# "
                + "#  #  #  #   #  #  # # ## ##  #   #  ## # #$"
                + "#   # $ ###   #   #     #####   ";
        populateBoard(board2, 10);

        for (char[] line : Main.board) {
            System.out.print(line);
            System.out.println();
        }

        // Create a state where we haven't moved and the boxes are in the respective positions
        State currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(1, 4), new Position(2, 1), new Position(6, 7), new Position(7, 4))), "");
        LinkedList<Position> allAdjucent = new LinkedList<>();

        // add all empty possible positions to the list
        for (Position box : currentState.getBoxes()) {
            for (Position possible : Utils.getAdjucentPositions(box, currentState)) {
                if (!allAdjucent.contains(possible)) {
                    allAdjucent.add(possible);
                }
            }
        }

        // Now walk the talk
        for (int i = 0; i < allAdjucent.size(); i++) {
            for (int j = 0; j < allAdjucent.size(); j++) {

                Position currentPos = allAdjucent.get(i);
                Position start = allAdjucent.get(i);
                Position goal = allAdjucent.get(j);

                String path = Utils.findPath(start, goal, currentState);
                for (int index = 0; index < path.length(); index++) {

                    if (path.charAt(index) == 'U') {
                        currentPos = new Position(currentPos.getRow() - 1, currentPos.getCol());
                    }
                    if (path.charAt(index) == 'D') {
                        currentPos = new Position(currentPos.getRow() + 1, currentPos.getCol());
                    }
                    if (path.charAt(index) == 'L') {
                        currentPos = new Position(currentPos.getRow(), currentPos.getCol() - 1);
                    }
                    if (path.charAt(index) == 'R') {
                        currentPos = new Position(currentPos.getRow(), currentPos.getCol() + 1);
                    }

                    if (!Main.isEmptyPosition(currentPos) || currentState.getBoxes().contains(currentPos)) {
                        fail("We walk into a box or wall");
                    }
                }
                assertEquals(currentPos, goal);
            }
        }

    }

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
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2, 1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);

        // Box left of player (room to move)
        box = new Position(2, 2);
        player = new Position(2, 3);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2, 1), new Position(2, 2), "L");
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
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2, 1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);

        // Box north of player (room to move)
        box = new Position(3, 1);
        player = new Position(4, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2, 1), new Position(3, 1), "U");
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
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(2, 6))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);

        // Box right of player (room to move)
        box = new Position(2, 5);
        player = new Position(2, 4);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(2, 6), new Position(2, 5), "R");
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
        currentState = new State(player, new PriorityQueue<>(Arrays.asList(new Position(4, 1))), "");
        expResult = null;
        result = Utils.tryToMoveBox(box, player, currentState);
        assertEquals(expResult, result);


        // Box south of player (room to move)
        box = new Position(3, 1);
        player = new Position(2, 1);
        currentState = new State(player, new PriorityQueue<Position>(), "");
        expResult = new Movement(new Position(4, 1), new Position(3, 1), "D");
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

        // All four positions are empty
        Position box = new Position(3, 4);
        State currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        List expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(3, 3), new Position(4, 4), new Position(2, 4)));
        List result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box below
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(4, 4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(3, 3), new Position(2, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box to the left
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(3, 3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(4, 4), new Position(2, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box to the right
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(3, 5))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 3), new Position(4, 4), new Position(2, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box up
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(2, 4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(3, 3), new Position(4, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box up and down
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(2, 4), new Position(4, 4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(3, 3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Box left and right
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(3, 5), new Position(3, 3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(4, 4), new Position(2, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // boxes everywhere
        box = new Position(3, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(4, 4), new Position(2, 4), new Position(3, 5), new Position(3, 3))), "");
        expResult = new ArrayList<>();
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // wall to the left
        box = new Position(3, 1);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 2), new Position(4, 1), new Position(2, 1)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // wall to the right
        box = new Position(3, 6);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(3, 5), new Position(4, 6), new Position(2, 6)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // wall down
        box = new Position(6, 6);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(6, 7), new Position(6, 5), new Position(5, 6)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // wall up
        box = new Position(2, 2);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(2, 3), new Position(2, 1), new Position(3, 2)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Corner up right
        box = new Position(1, 5);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(1, 4), new Position(2, 5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Corner up left
        box = new Position(1, 4);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(1, 5), new Position(2, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Corner down right
        box = new Position(8, 5);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8, 4), new Position(7, 5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Corner down left
        box = new Position(8, 3);
        currentState = new State(new Position(1, 1), new PriorityQueue<Position>(), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8, 4), new Position(7, 3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Mix only UP empty
        box = new Position(8, 3);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(7, 3))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Mix only RIGHT empty
        box = new Position(8, 3);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(8, 4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(7, 3)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Mix only LEFT empty
        box = new Position(8, 5);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(7, 5))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(8, 4)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Mix only DOWN empty
        box = new Position(1, 5);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(1, 4))), "");
        expResult = new ArrayList<>(Arrays.asList(new Position(2, 5)));
        result = Utils.getAdjucentPositions(box, currentState);
        assertEquals(expResult, result);

        // Mix NO SPACES
        box = new Position(1, 5);
        currentState = new State(new Position(1, 1), new PriorityQueue<>(Arrays.asList(new Position(2, 5), new Position(1, 4))), "");
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

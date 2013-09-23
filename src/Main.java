
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class Main {

    static final int[] MOVE_X = {1, -1, 0, 0};
    static final int[] MOVE_Y = {0, 0, 1, -1};
    static final char[] MOVES = {'R', 'L', 'D', 'U'};
    /**
     * @param args
     */
    static char[][] board; //Easier access instead of string
    private static int lengthMax; // max number of columns
    static Set<Position> goals;

    public static void main(String[] args) throws IOException {
        Vector<String> b = new Vector<String>();
        goals = new HashSet<Position>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        String line;

        lengthMax = 0;
        while(!br.ready());
        while (br.ready()) {
            line = br.readLine();
//            System.out.println(line);
            b.add(line);

            if (lengthMax < line.length()) {
                lengthMax = line.length();
            }
        } // End while
        long s = new Date().getTime();
        State first = parseBoard(b);
        long e = new Date().getTime();
        System.out.println("Time parse board: " + (e - s) + " ms");
        for (int i = 0; i < board.length; i++) {
//        	System.out.println(new String(board[i]));
        }
        String result = solveMap(first);
        long f = new Date().getTime();
        System.out.println("Total time: " + (f-s));
        System.out.println(result);
    } // main

    /**
     * Returns the path from a box from a goal It uses a Best First Search
     * algorithm
     *
     * @param position
     * @param goal
     * @return the path to the goal, or null if it doesn't exit.
     */
    public static String moveBox(Position boxPosition, Position goal) {
        assert (isBoxPosition(boxPosition)); //Just to be sure

        // Initialization
        PriorityQueue<BoxState> fringe = new PriorityQueue<BoxState>();
        Set<BoxState> visitedStates = new HashSet<BoxState>();

        BoxState state0 = new BoxState(boxPosition, "", goal);
        fringe.add(state0);

        //BEST-FIRST SEARCH
        while (fringe.size() > 0) {
            //Pop new state
            BoxState state = fringe.poll();
            String path = state.getPath();
            Position pos = state.getPosition();
            visitedStates.add(state);

            //Check if arrived to goal
            if (isGoal(pos)) {
                return path;
            } else { //Expand the state
                List<BoxState> nextStates = state.getNextBoxStates();
                for (int i = 0; i < nextStates.size(); i++) {
                    BoxState s = nextStates.get(i);
                    if (!visitedStates.contains(s)) //Add next States to the fringe if they are not visited
                    {
                        fringe.add(s);
                    }
                }
            }
        }//end while        
        return null;
    }

    /**
     * Solves the map and returns the path as a String
     *
     * @return
     */
    public static String solveMap(State first) {
        // Initialization
        PriorityQueue<State> fringe = new PriorityQueue<State>();
//    	Stack<State> fringe = new Stack<State>();
        Set<State> visitedStates = new HashSet<>(10000);

        fringe.add(first);
        visitedStates.add(first);

        //BEST-FIRST SEARCH
        while (fringe.size() > 0) {
            long start = new Date().getTime();
            //Pop new state
        	System.out.println("FRINGE: "+fringe.size() + " ; VISITED: "+visitedStates.size());
            State state = fringe.poll();
//        	State state = fringe.pop();

            if (state.finished()) {
                return state.getCurrent_path();
            }


            //Check if arrived to goal
            //Expand the state
            List<State> nextStates = new ArrayList<State>();
            state.getNextMoves(nextStates); //This takes ~1 ms on map 1, ~4ms on map 100.
            for (State next : nextStates) {
                if (!visitedStates.contains(next) && isValidMove(next)) {
                    fringe.add(next);
                    visitedStates.add(next);
                }
            }
            long end = new Date().getTime();
//            System.out.println("ITERATION TIMAR: "+(end-start)+" ms");

        }//end while        
        return null;
    }

    /**
     * Translate from Vector<String> to char[][] Check for corners and dangerous
     * zones Updates the "board" (char[][]) attribute.
     *
     * @param board
     * @author Carlos Perez
     */
    public static State parseBoard(Vector<String> board) {
        Main.board = new char[board.size()][lengthMax];

        Position player = null;
        PriorityQueue<Position> boxes = new PriorityQueue<Position>();

        // Normal map, exactly the same than in Vector<String> board
        for (int row = 0; row < board.size(); row++) {
            String datosF = board.get(row);
            int maxChar = datosF.length();
            for (int col = 0; col < lengthMax; col++) {
                if (col < maxChar) {
                    char c = datosF.charAt(col);
                    // Player
                    if (c == '@') {
                        Main.board[row][col] = ' ';
                        player = new Position(row, col);

                        // Player on goal
                    } else if (c == '+') { //Player on goal
                        Main.board[row][col] = '.';
                        player = new Position(row, col);
                        goals.add(new Position(row, col));

                        // Box
                    } else if (c == '$') { //Box
                        Main.board[row][col] = ' ';
                        boxes.add(new Position(row, col));

                        // Box on goal
                    } else if (c == '*') {//Box on goal
                        Main.board[row][col] = '.';
                        boxes.add(new Position(row, col));
                        goals.add(new Position(row, col));

                        // Normal Case
                    } else if (c == '.') {//Goal
                        goals.add(new Position(row, col));
                        Main.board[row][col] = '.';
                    } else {
                        Main.board[row][col] = c;
                    }
                } else {
                    Main.board[row][col] = ' ';
                }
            }
        }



        // Put c char in corners,  Doesn't look at the borders of the map!
        for (int row = 1; row < board.size() - 1; row++) {
            for (int col = 1; col < lengthMax - 1; col++) {
                // Case space
                if (Main.board[row][col] == (' ')) {
//                    // If theres one wall up or down and another wall right or left => it's a corner!
//                    if ((Main.board[row - 1][col] == ('#') || (Main.board[row + 1][col] == ('#')))
//                            && (Main.board[row][col - 1] == ('#') || (Main.board[row][col + 1] == ('#')))) {
//                        Main.board[row][col] = ('c');
//                    }
                    boolean isPath = false;
                    for (Position g : goals) {
                        if (moveBox(new Position(row, col), g) != null) {
                            isPath = true;
                            break;
                        }
                    }
                    if (!isPath) {
                        Main.board[row][col] = 'x';
                    }
                }
            }
        }

        // First and initial State
        return new State(player, boxes, "");
    }

    /**
     * Checks if a box on a position pos[] (pos[0] and pos[1]) can be moved.
     *
     * @param pos
     * @return true if there is any way to move that box
     */
    public static boolean isIllegal(State currentState) {
        return false;
//    	for(Position box : currentState.getBoxes()){
//	    	for(Position p : Utils.getAllAdjucentPositions(box, currentState)){
//	    		if(!Main.isEmptyPosition(p))
//	    			return true;
//	    	}    
//    	}
//        return false;
    }

    /*---------------------HELPER FUNCTIONS------------------*/
    public static boolean isEmptyPosition(Position p) {
        return board[p.getRow()][p.getCol()] != '#';
    }

    // Returns true if the position is considered "safe"
    // A position which contains a goal should always be considered safe.
    public static boolean isSafePosition(Position p) {
        return board[p.getRow()][p.getCol()] != 'x' || isGoal(p);
    }

    public static boolean isBoxPosition(Position p) {
        char c = board[p.getRow()][p.getCol()];
        return c == '$' || c == '*';
    }

    public static boolean isValidPosition(Position p) {
        int row = p.getRow();
        int col = p.getCol();
        return (row >= 0 && col >= 0 && row < board.length && col < board[0].length);
    }
    
    public static Position getLastMove(State current) {
        Position player = current.getPlayer();
        Position lastMovedBox = new Position(0,0);
        char lastMove = current.getCurrent_path().charAt(current.getCurrent_path().length()-1);
        
        if(lastMove == 'U') {
            lastMovedBox = new Position(player.getRow()-1, player.getCol());
        } else if(lastMove == 'D') {
            lastMovedBox = new Position(player.getRow()+1, player.getCol());
        } else if(lastMove == 'L') {
            lastMovedBox = new Position(player.getRow(), player.getCol()-1);
        } else if(lastMove == 'R') {
            lastMovedBox = new Position(player.getRow(), player.getCol()+1);
        }
        return lastMovedBox;
    }
    
    public static boolean isValidMove(State current) {
        
        // get the position of the last moved box
        Position box = getLastMove(current);
        
        int rel_row = box.getRow();
        int rel_col = box.getCol();
        
        // create the board we will play with
        char[][] tmpBoard = new char[5][5];
        
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                tmpBoard[i][j] = ' ';
            }
        }
        
        // store all relevant positions to the box
        ArrayList<Position> positions = new ArrayList<>();
        
        for(int i=box.getRow()-1, x=1; i<=box.getRow()+1; i++, x++) {
            for(int j= box.getCol()-1, y=1; j<=box.getCol()+1; j++, y++) {
                tmpBoard[x][y] = board[i][j];
                positions.add(new Position(i,j));
            }
        }
        
        
        // for each relevant position
        for(Position pos : positions) {
            
            // if it's really a box
            if(current.getBoxes().contains(pos)) {
                tmpBoard[pos.getRow()-rel_row+2][pos.getCol()-rel_col+2] = '$';
            }
        }
        
        // check all boxes in the tmpBoard
        boolean allOk = false;
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                if(tmpBoard[i][j] == '$') {
                    boolean thisOk = false;
                    if((tmpBoard[i-1][j]!= '#' && tmpBoard[i-1][j] != '$') && (tmpBoard[i+1][j]!= '#' && tmpBoard[i+1][j] != '$')) {
                        thisOk = true;
                    }
                    if((tmpBoard[i][j-1]!= '#' && tmpBoard[i][j-1] != '$') && (tmpBoard[i][j+1]!= '#' && tmpBoard[i][j+1] != '$')) {
                        thisOk = true;
                    }
                    if(thisOk) {
                        allOk = true;
                    }
                }
            }
        }
        
        // if all boxes are on goals, don't fail this state
        boolean allBoxesOngoals = true;
        for(Position pos : positions) {
            if(current.getBoxes().contains(pos)) {
                if(!goals.contains(pos)) {
                    allBoxesOngoals = false;
                }
            }
        }
        
        
        return allOk || allBoxesOngoals;
    }

    // Returns true if this position is a goal
    public static boolean isGoal(Position p) {
        return goals.contains(p);
    }
} // End Main

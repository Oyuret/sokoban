
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

public class Main {

    static final int[] MOVE_X = {1, -1, 0, 0};
    static final int[] MOVE_Y = {0, 0, 1, -1};
    static final char[] MOVES = {'R', 'L', 'D', 'U'};
    /**
     * @param args
     */
    static char[][] board; //Easier access instead of string
    private Map<State, Integer> visitedStates;
    private static int lenghtMax; // max number of columns

    public static void main(String[] args) throws IOException {
        Vector<String> b = new Vector<String>();
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        
        String line;        
        
        lenghtMax = 0;
        while (br.ready()) {
            line = br.readLine();
            b.add(line);
            
            if (lenghtMax < line.length()) {
                lenghtMax = line.length();
            }
        } // End while
        State first = parseBoard(b);
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
        Queue<State> fringe = new LinkedList<>();
        Set<State> visitedStates = new HashSet<>(10000);
        
        fringe.add(first);
        visitedStates.add(first);

        //BEST-FIRST SEARCH
        while (fringe.size() > 0) {
            //Pop new state
            State state = fringe.poll();
            
            if (state.finished()) {
                return state.getCurrent_path();
            }
           

            //Check if arrived to goal
            //Expand the state
            List<State> nextStates = new LinkedList<>();
            state.getNextMoves(nextStates);
            
            for (State next : nextStates) {
                if(!visitedStates.contains(next)) {
                    fringe.add(next);
                    visitedStates.add(next);
                }
            }
            
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
        Main.board = new char[board.size()][lenghtMax];
        
        Position player = null;
        PriorityQueue<Position> boxes = new PriorityQueue<Position>();

        // Normal map, exactly the same than in Vector<String> board
        for (int row = 0; row < board.size(); row++) {
            String datosF = board.get(row);
            int maxChar = datosF.length();
            for (int col = 0; col < lenghtMax; col++) {
                if (col < maxChar) {
                	
                	// Player
                	if(datosF.charAt(col)=='@') {
                		Main.board[row][col] = ' ';
                		player = new Position(row,col);
                		
                	// Player on goal
                	} else if(datosF.charAt(col)=='+') {
                		Main.board[row][col] = '.';
                		player = new Position(row,col);
                		
                	// Box
                	} else if(datosF.charAt(col)=='$') {
                		Main.board[row][col] = ' ';
                		boxes.add(new Position(row,col));
                		
                	// Box on goal
                	} else if(datosF.charAt(col)=='*') {
                		Main.board[row][col] = '.';
                		boxes.add(new Position(row,col));
                		
                	// Normal Case
                	} else {
                		Main.board[row][col] = datosF.charAt(col);
                	}
                } else {
                    Main.board[row][col] = ' ';
                }
            }
        }
        
    

        // Put c char in corners,  Doesn't look at the borders of the map!
        for (int row = 1; row < board.size() - 1; row++) {
            for (int col = 1; col < lenghtMax - 1; col++) {
                // Case space
                if (Main.board[row][col] == (' ')) {
                    // If theres one wall up or down and another wall right or left => it's a corner!
                    if ((Main.board[row - 1][col] == ('#') || (Main.board[row + 1][col] == ('#')))
                            && (Main.board[row][col - 1] == ('#') || (Main.board[row][col + 1] == ('#')))) {
                        Main.board[row][col] = ('c');
                    }
                }
            }
        }
       
        // First and initial State
        return new State(player,boxes,"");
        
    }

    /**
     * (FUTURE WORK, START WITH BFS) Estimate how good is a state
     *
     * @param state
     * @return
     */
    public static int stateHeuristic(State state) {
        return -1;
    }

    /**
     * Checks if a box on a position pos[] (pos[0] and pos[1]) can be moved.
     *
     * @param pos
     * @return true if there is any way to move that box
     */
    public static boolean isIllegal(int[] pos) {
        return false;
    }

    /*---------------------HELPER FUNCTIONS------------------*/
    public static boolean isEmptyPosition(Position p) {
        return board[p.getRow()][p.getCol()] != '#';
    }
    
    public static boolean isBoxPosition(Position p) {
        char c = board[p.getRow()][p.getCol()];        
        return c == '$' || c == '*';        
    }
    
    public static boolean isGoal(Position p) {
        return board[p.getRow()][p.getCol()] == '.';
    }
} // End Main

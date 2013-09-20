

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
    private static int lengthMax; // max number of columns
    static Set<Position> boxPositions;
    
    
    public static void main(String[] args) throws IOException {
    	Vector<String> b = new Vector<String>();
    	readMap(b);
    	board = new char[b.size()][lengthMax];
        parseBoard(b, board);
        State first = processBoard(board);
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

        //BREADTH FIRST SEARCH
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
     * Translate from Vector<String> to char[][].
     * Modified:
     *  Carlos Galvez=> pass the charBoard as a parameter,
     *  so we can use this method with other boards (e.g.: tests) 
     *
     * @param board
     * @param charBoard
     * @author Carlos Perez
     * 
     */
    public static void parseBoard(Vector<String> board, char[][] charBoard) {    	
        // Normal map, exactly the same than in Vector<String> board
    	boxPositions = new HashSet<Position>();
        for (int row = 0; row < board.size(); row++) {
            String datosF = board.get(row);
            int maxChar = datosF.length();
            for (int col = 0; col < lengthMax; col++) {
                if (col < maxChar) {
                	char c = datosF.charAt(col);
                	if(c == '$')
                		boxPositions.add(new Position(row,col));
                	charBoard[row][col] = datosF.charAt(col);
                } else {
                	charBoard[row][col] = ' ';
                }
            }
        }
    }
    
    /**
     * Check for corners and dangerous
     * zones Updates the "board" (char[][]) attribute.
     * @param charBoard
     * @return
     * @author Carlos Perez
     */
    public static State processBoard(char[][] charBoard){ 
        
        // Put c char in corners, C if there is the player in that space. Doesn't look at the borders of the map!
        for (int row = 1; row < charBoard.length - 1; row++) {
            for (int col = 1; col < lengthMax - 1; col++) {
                // Case space
                if (charBoard[row][col] == (' ')) {
                    // If theres one wall up or down and another wall right or left => it's a corner!
                    if ((charBoard[row - 1][col] == ('#') || (charBoard[row + 1][col] == ('#')))
                            && (charBoard[row][col - 1] == ('#') || (charBoard[row][col + 1] == ('#')))) {
                    	charBoard[row][col] = ('c');
                    }
                }
                // Case player
                if (charBoard[row][col] == ('@')) {
                    // If theres one wall up or down and another wall right or left => it's a corner!
                    if ((charBoard[row - 1][col] == ('#') || (charBoard[row + 1][col] == ('#')))
                            && (charBoard[row][col - 1] == ('#') || (charBoard[row][col + 1] == ('#')))) {
                    	charBoard[row][col] = ('C');
                    }
                }
            }
        }

        // In case we want to compare 2 different positions, we'll put and 'x' char instead of an space
        // if the player can be in this space without moving any box. (the player in that position would
        // have the @ in a different position but would be the same state.
        // This code may be moved to another method that would compare between two maps
        ArrayList<Position> queue = new ArrayList<Position>();
        ArrayList<Position> visited = new ArrayList<Position>();
        for (int row = 0; row < charBoard.length; row++) {
            for (int col = 0; col < lengthMax; col++) {
                if (charBoard[row][col] == ('@')) {
                    queue.add(new Position(row, col));
                    visited.add(new Position(row, col));
                } else if (charBoard[row][col] == ('+')) {
                	charBoard[row][col] = '.';
                    queue.add(new Position(row, col));
                    visited.add(new Position(row, col));
                } else if (charBoard[row][col] == ('C')) {
                	charBoard[row][col] = 'c';
                    queue.add(new Position(row, col));
                    visited.add(new Position(row, col));
                }
            }
        }
        
        while (queue.size() != 0) {
            Position now = queue.get(0);
            queue.remove(0);

            // Will display an error if our player is in an invalid position, 
            // or there is an invalid map, 'cause we'll reach the borders!
            Position up = new Position(now.getRow() - 1, now.getCol());
            Position down = new Position(now.getRow() + 1, now.getCol());
            Position right = new Position(now.getRow(), now.getCol() + 1);
            Position left = new Position(now.getRow(), now.getCol() - 1);
            
            Position[] pos = {up, down, right, left};
            
            for (Position p : pos) {
                if (!visited.contains(p)) {
                    if (charBoard[p.getRow()][p.getCol()] == (' ')) {
                    	charBoard[p.getRow()][p.getCol()] = 'x';
                        queue.add(new Position(p.getRow(), p.getCol()));
                    } else if (charBoard[p.getRow()][p.getCol()] == ('.')) {
                        queue.add(new Position(p.getRow(), p.getCol()));
                    } else if (charBoard[p.getRow()][p.getCol()] == ('c')) {
                        queue.add(new Position(p.getRow(), p.getCol()));
                    }
                    visited.add(new Position(p.getRow(), p.getCol()));
                }
            }
            
        }
        return null;
        
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
    /**
     * Reads the map from System.in and stores it in a vector<String>
     * 
     * @param board
     * @author Carlos Galvez
     * @throws IOException 
     */
    public static void readMap(Vector<String> board) throws IOException{    	 
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
        String line;                
        lengthMax = 0;
        while (br.ready()) {
            line = br.readLine();
            board.add(line);            
            if (lengthMax < line.length()) {
                lengthMax = line.length();
            }
        } // End while
    }
    
    public static void setMap(char[][]board){
    	Main.board = board;
    }
    
    public static boolean isEmptyPosition(Position p) {
    	char c = board[p.getRow()][p.getCol()];
    	boolean result = c!= '#' && !isBoxPosition(p);
        return result;
    }
    
    public static boolean isBoxPosition(Position p) {
        char c = board[p.getRow()][p.getCol()]; 
        boolean result = c == '$' || c == '*';    
        return result;     
    }
    
    public static boolean isGoal(Position p) {
        return board[p.getRow()][p.getCol()] == '.';
    }
} // End Main

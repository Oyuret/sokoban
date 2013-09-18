
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

        // Normal map, exactly the same than in Vector<String> board
        for (int row = 0; row < board.size(); row++) {
            String datosF = board.get(row);
            int maxChar = datosF.length();
            for (int col = 0; col < lenghtMax; col++) {
                if (col < maxChar) {
                    Main.board[row][col] = datosF.charAt(col);
                } else {
                    Main.board[row][col] = ' ';
                }
            }
        }

        // Put c char in corners, C if there is the player in that space. Doesn't look at the borders of the map!
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
                // Case player
                if (Main.board[row][col] == ('@')) {
                    // If theres one wall up or down and another wall right or left => it's a corner!
                    if ((Main.board[row - 1][col] == ('#') || (Main.board[row + 1][col] == ('#')))
                            && (Main.board[row][col - 1] == ('#') || (Main.board[row][col + 1] == ('#')))) {
                        Main.board[row][col] = ('C');
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
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < lenghtMax; col++) {
                if (Main.board[row][col] == ('@')) {
                    queue.add(new Position(row, col));
                    visited.add(new Position(row, col));
                } else if (Main.board[row][col] == ('+')) {
                    Main.board[row][col] = '.';
                    queue.add(new Position(row, col));
                    visited.add(new Position(row, col));
                } else if (Main.board[row][col] == ('C')) {
                    Main.board[row][col] = 'c';
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
                    if (Main.board[p.getRow()][p.getCol()] == (' ')) {
                        Main.board[p.getRow()][p.getCol()] = 'x';
                        queue.add(new Position(p.getRow(), p.getCol()));
                    } else if (Main.board[p.getRow()][p.getCol()] == ('.')) {
                        queue.add(new Position(p.getRow(), p.getCol()));
                    } else if (Main.board[p.getRow()][p.getCol()] == ('c')) {
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

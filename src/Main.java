
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

    
    private static final int MILISEC_F = 100;


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
            b.add(line);
 
            if (lengthMax < line.length()) {
                lengthMax = line.length();
            }
        } // End while
        State first = parseBoard(b);

        String result = solveMap(first);
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
        BoxState state0 = new BoxState(boxPosition, "", goal);
        return Utils.bestFirstSearch(state0, goal);        
    }
 
    /**
     * Solves the map and returns the path as a String
     *
     * @return
     */
    public static String solveMap(State first) {
        // Initialization
        PriorityQueue<State> fringe = new PriorityQueue<State>();

        Set<State> visitedStates = new HashSet<State>(100000);

        fringe.add(first);
        visitedStates.add(first);
        
        long start = new Date().getTime();
        //BEST-FIRST SEARCH
        while (fringe.size() > 0 && (new Date().getTime() - start) < MILISEC_F) {
        	
            State state = fringe.poll();


            if (state.finished()) {
                return state.getCurrent_path();
            }
 
 
            //Check if arrived to goal
            //Expand the state
            List<State> nextStates = new ArrayList<State>();
            state.getNextMoves(nextStates); //This takes ~1 ms on map 1, ~4ms on map 100.
            for (State next : nextStates) {
                if (!visitedStates.contains(next) && isValidMove(next) && isSafePosition(getLastMove(next))) {
                    fringe.add(next);
                    visitedStates.add(next);
                }
            }

        }//end while 
        
        
        
        // We start here moving BACKWARDS!
        fringe = new PriorityQueue<State>();
        Set<State> visitedStatesB = new HashSet<State>(100000);
        // We change goals and boxPositions in 1st state
        HashSet<Position> goalBox = new HashSet<Position>(); // boxes start in goal
        goalBox.addAll(goals);
        State state0 = new State(new Position(1,1), goalBox, "");
        Main.goals = new HashSet<Position>();
        Main.goals.addAll(first.getBoxes());
        
        state0.getNextMovesInitialBack(visitedStatesB);
        fringe.addAll(visitedStatesB);
        
        // Check if we reach a solution with the first states
        for(State firsts: visitedStatesB) {
        	if(visitedStates.contains(firsts)) { // FINISHED!! we have found a result!
       		 String path1 = null;
       		 // We take the first path, better implementations?
       		 for(State st1 : visitedStates) {
       			 if(st1.equals(firsts)) {
       				 path1 = st1.getCurrent_path();
       				 String path_for_player = Utils.findPath(st1.getPlayer(), firsts.getPlayer(), st1);
       				 path1 += path_for_player;
       				 break;
       			 }
       		 }
       		 
//       		 if(path1 == null) throw new Exception("problem at finishing backwards"); // Should not give exception
       		 return path1 + Utils.translateBack(firsts.getCurrent_path());
       	 }
        }
        
        while (fringe.size() > 0) {
        	 State state = fringe.poll();
        	 
        	 
        	 //Expand the state
             List<State> nextStates = new ArrayList<State>();
             state.getNextMovesBack(nextStates); //This takes ~1 ms on map 1, ~4ms on map 100.
             for (State next : nextStates) {
            	 
            	 if(visitedStates.contains(next)) { // FINISHED!! we have found a result!
            		 String path1 = null;
            		 // We take the first path, better implementations?
            		 for(State st1 : visitedStates) {
            			 if(st1.equals(next)) {
            				 path1 = st1.getCurrent_path();
            				 String path_for_player = Utils.findPath(st1.getPlayer(), next.getPlayer(), st1);
               				 path1 += path_for_player;
            				 break;
            			 }
            		 }
            		 
//            		 if(path1 == null) throw new Exception("problem at finishing backwards"); // Should not give exception
            		 return path1 + Utils.translateBack(next.getCurrent_path());
            	 }
            	 
                 if (!visitedStatesB.contains(next)) { // We'll have to create some rules in backwards
                     fringe.add(next);
                     visitedStatesB.add(next);
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
        Main.board = new char[board.size()][lengthMax];
 
        Position player = null;
        Set<Position> boxes = new HashSet<Position>();
 
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
                        String path = Utils.bestFirstSearch(new BoxState(new Position(row,col),"",g), g);
                        if (path != null) {
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
//      for(Position box : currentState.getBoxes()){
//              for(Position p : Utils.getAllAdjucentPositions(box, currentState)){
//                      if(!Main.isEmptyPosition(p))
//                              return true;
//              }    
//      }
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
        return (row >= 1 && col >= 1 && row < board.length-1 && col < board[0].length-1);
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
        ArrayList<Position> positions = new ArrayList<Position>();
        
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
 

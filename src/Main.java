
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    static HashMap<PositionPair, Integer> walkingDistance;
    
    // Time to run the forward algorithm
    private static final int MILISEC_F = 5500;

    public static void main(String[] args) throws IOException {
        Vector<String> b = new Vector<String>();
        goals = new HashSet<Position>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        String line;

        // The length of the longest line
        lengthMax = 0;
        while (!br.ready());
        while (br.ready()) {
            line = br.readLine();
            b.add(line);

            if (lengthMax < line.length()) {
                lengthMax = line.length();
            }
        } // End while
        
        // The first State given by the board
        State first = parseBoard(b);
        
        // Calculate all the walking distances
        calculateWalkingDistances(first);

        // Solve the map
        String result = solveMap(first);
        
        // Print out the result
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
        PriorityQueue<State> fringe = new PriorityQueue<>(100000);

        // The set of States we have already seen
        Set<State> visitedStates = new HashSet<>(100000);

        // Add the first state
        fringe.add(first);
        visitedStates.add(first);

        // Check the starting time
        long start = new Date().getTime();
        
        //BEST-FIRST SEARCH
        while (fringe.size() > 0 && (new Date().getTime() - start) < MILISEC_F) {

            // Pull the fron of the queue
            State state = fringe.poll();
            
            // Is this current State the solution?
            if (state.finished()) {
                return state.getCurrent_path();
            }


            // Expand the state
            List<State> nextStates = new ArrayList<>();
            state.getNextMoves(nextStates);
            
            // For each State we got from the expanding
            for (State next : nextStates) {
                
                // If this State isn't already in the visited list
                // or isn't a Dynamic or Static deadlock
                if (!visitedStates.contains(next) && isValidMove(next) && isSafePosition(getLastMove(next))) {
                    
                    // Add this state to the queue
                    fringe.add(next);
                    
                    // Set it as visited
                    visitedStates.add(next);
                }
            } // end for each state
        }//end while 



        // We start here moving BACKWARDS!
        fringe = new PriorityQueue<State>(100000);
        
        // The visited states by the backwards algorithm
        Set<State> visitedStatesB = new HashSet<State>(100000);
        // The goals and boxes have to be swapped
        HashSet<Position> goalBox = new HashSet<>();
        
        // Set the goals as boxes
        goalBox.addAll(goals);
        
        // Create the first State for the backwards algorithm
        //State state0 = new State(new Position(1, 1), goalBox, "");
        State state0 = new State(first.getPlayer(), goalBox, "");
        
        // Set the boxes as goals
        Main.goals = new HashSet<>();
        Main.goals.addAll(first.getBoxes());

        // Get the first Children to this state
        state0.getNextMovesInitialBack(visitedStatesB);
        fringe.addAll(visitedStatesB);

        // Check if we reach a solution with the first states
        for (State firsts : visitedStatesB) {
            if (visitedStates.contains(firsts)) { // FINISHED!! we have found a result!
                String path1 = null;
                // We take the first path, better implementations?
                for (State st1 : visitedStates) {
                    if (st1.equals(firsts)) {
                        path1 = st1.getCurrent_path();
                        String path_for_player = Utils.findPath(st1.getPlayer(), firsts.getPlayer(), st1);
                        path1 += path_for_player;
                        break;
                    }
                }

                // Return the full solution
                return path1 + Utils.translateBack(firsts.getCurrent_path());
            }
        }

        while (fringe.size() > 0) {
            State state = fringe.poll();

            //Expand the state
            List<State> nextStates = new ArrayList<>();
            state.getNextMovesBack(nextStates); //This takes ~1 ms on map 1, ~4ms on map 100.
            for (State next : nextStates) {

                if (visitedStates.contains(next)) { // FINISHED!! we have found a result!
                    String path1 = null;
                    // We take the first path, better implementations?
                    for (State st1 : visitedStates) {
                        if (st1.equals(next)) {
                            path1 = st1.getCurrent_path();
                            String path_for_player = Utils.findPath(st1.getPlayer(), next.getPlayer(), st1);
                            path1 += path_for_player;
                            break;
                        }
                    }

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
        Set<Position> boxes = new HashSet<>();

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



        // Put x in all Static deadlocks
        for (int row = 1; row < board.size() - 1; row++) {
            for (int col = 1; col < lengthMax - 1; col++) {
                // Case space
                if (Main.board[row][col] == (' ')) {

                    boolean isPath = false;
                    for (Position g : goals) {
                        
                        // Place an imaginary box here and try to move it to any goal
                        String path = Utils.bestFirstSearch(new BoxState(new Position(row, col), "", g), g);
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

    /*---------------------HELPER FUNCTIONS------------------*/
    public static boolean isEmptyPosition(Position p) {
        return board[p.getRow()][p.getCol()] != '#';
    }

    /**
     * Checks if this Position isn't a static deadlock
     * @param p The position to be checked
     * @return True if this Position can't be considered as a Static deadlock
     */
    public static boolean isSafePosition(Position p) {
        return board[p.getRow()][p.getCol()] != 'x' || isGoal(p);
    }

    /**
     * Checks if this Position is within the boundaries of the Board
     * @param p The position to be checked
     * @return false if this position is outside the board
     */
    public static boolean isValidPosition(Position p) {
        int row = p.getRow();
        int col = p.getCol();
        return (row >= 1 && col >= 1 && row < board.length - 1 && col < board[0].length - 1);
    }

    /**
     * Gets the last movement made in this State
     * @param current The State to be checked
     * @return The position of the player before making the last move
     */
    public static Position getLastMove(State current) {
        Position player = current.getPlayer();
        Position lastMovedBox = new Position(0, 0);
        char lastMove = current.getCurrent_path().charAt(current.getCurrent_path().length() - 1);

        if (lastMove == 'U') {
            lastMovedBox = new Position(player.getRow() - 1, player.getCol());
        } else if (lastMove == 'D') {
            lastMovedBox = new Position(player.getRow() + 1, player.getCol());
        } else if (lastMove == 'L') {
            lastMovedBox = new Position(player.getRow(), player.getCol() - 1);
        } else if (lastMove == 'R') {
            lastMovedBox = new Position(player.getRow(), player.getCol() + 1);
        }
        return lastMovedBox;
    }

    /**
     * Checks if the latest done movement in this State gave us a Dynamic deadlock
     * @param current The State to be checked
     * @return False if this State isn't a Dynamic deadlock
     */
    public static boolean isValidMove(State current) {

        // get the position of the last moved box
        Position box = getLastMove(current);

        int rel_row = box.getRow();
        int rel_col = box.getCol();

        // create the board we will play with
        char[][] tmpBoard = new char[5][5];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tmpBoard[i][j] = ' ';
            }
        }

        // store all relevant positions to the box
        ArrayList<Position> positions = new ArrayList<>();

        for (int i = box.getRow() - 1, x = 1; i <= box.getRow() + 1; i++, x++) {
            for (int j = box.getCol() - 1, y = 1; j <= box.getCol() + 1; j++, y++) {
                tmpBoard[x][y] = board[i][j];
                positions.add(new Position(i, j));
            }
        }


        // for each relevant position
        for (Position pos : positions) {

            // if it's really a box
            if (current.getBoxes().contains(pos)) {
                tmpBoard[pos.getRow() - rel_row + 2][pos.getCol() - rel_col + 2] = '$';
            }
        }

        // check all boxes in the tmpBoard
        boolean allOk = false;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tmpBoard[i][j] == '$') {
                    boolean thisOk = false;
                    if ((tmpBoard[i - 1][j] != '#' && tmpBoard[i - 1][j] != '$')
                            && (tmpBoard[i + 1][j] != '#'
                            && tmpBoard[i + 1][j] != '$')) {
                        thisOk = true;
                    }
                    if ((tmpBoard[i][j - 1] != '#' && tmpBoard[i][j - 1] != '$')
                            && (tmpBoard[i][j + 1] != '#'
                            && tmpBoard[i][j + 1] != '$')) {
                        thisOk = true;
                    }
                    if (thisOk) {
                        allOk = true;
                    }
                }
            }
        }

        // if all boxes are on goals, don't fail this state
        boolean allBoxesOngoals = true;
        for (Position pos : positions) {
            if (current.getBoxes().contains(pos)) {
                if (!goals.contains(pos)) {
                    allBoxesOngoals = false;
                }
            }
        }


        return allOk || allBoxesOngoals;
    }

    /**
     * Checks if this Position is a goal
     * @param p The Position to be checked
     * @return True if this Position is a goal
     */
    public static boolean isGoal(Position p) {
        return goals.contains(p);
    }

    /*--------------------------------------*/
    /**
     * Returns an integer representing the total number of reachable positions
     * from the player
     *
     * @param player
     * @return
     */
    public static int floodFill(State s) {
        // Initialization
        Queue<Position> fringe = new LinkedList<>();
        Set<Position> visitedStates = new HashSet<>();

        fringe.add(s.player);
        visitedStates.add(s.player);

        while (fringe.size() > 0) {
            //Pop new state
            Position state = fringe.poll();
            //Expand the state
            List<Position> nextStates = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Position newP = new Position(state.getRow() + MOVE_Y[i], state.getCol() + MOVE_X[i]);
                if (isValidPosition(newP) && isEmptyPosition(newP) && !s.boxes.contains(newP)) {
                    nextStates.add(newP);
                }
            }
            for (Position p : nextStates) {
                if (!visitedStates.contains(p)) //Add next States to the fringe if they are not visited
                {
                    fringe.add(p);
                    visitedStates.add(p);
                }
            }
        }//end while        

        /*Now we have all the reachable positions on the visitedStates list*/
        int result = 0;
        for (Position p : visitedStates) {
            result += 3 * p.getRow() + 7 * p.getCol();
        }
        return result;
    }

    /**
     * Creates a mapping containing all the walking distances from all reachable
     * Positions (empty map) to all goals and to all boxes
     * @param first The first State
     */
    private static void calculateWalkingDistances(State first) {
        try {
            
            // Initialize the HashMap
            walkingDistance = new HashMap<>();
            
            // Clone the first State
            State copy = (State) first.clone();
            
            // Remove all the boxes
            copy.boxes.clear();


            // Initialization
            Queue<Position> fringe = new LinkedList<>();
            Set<Position> visitedStates = new HashSet<>();

            fringe.add(copy.player);
            visitedStates.add(copy.player);

            // Calculate all the reachable Positions by the player
            while (fringe.size() > 0) {
                //Pop new state
                Position state = fringe.poll();
                //Expand the state
                List<Position> nextStates = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    Position newP = new Position(state.getRow() + MOVE_Y[i], state.getCol() + MOVE_X[i]);
                    if (isValidPosition(newP) && isEmptyPosition(newP)) {
                        nextStates.add(newP);
                    }
                }
                for (Position p : nextStates) {
                    if (!visitedStates.contains(p)) //Add next States to the fringe if they are not visited
                    {
                        fringe.add(p);
                        visitedStates.add(p);
                    }
                }
            }//end while

            // Calculate the walking distance from all reachable Positions to all
            // goals
            for (Position from : visitedStates) {
                for (Position goal : goals) {
                    String path = Utils.findPath(from, goal, copy);
                    walkingDistance.put(new PositionPair(from, goal), path.length());
                }
            }
            
            // Calcualte the walking distance from all reachable Positions
            // to all boxes
            for (Position from : visitedStates) {
                for (Position goal : first.getBoxes()) {
                    String path = Utils.findPath(from, goal, copy);
                    walkingDistance.put(new PositionPair(from, goal), path.length());
                }
            }

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} // End Main
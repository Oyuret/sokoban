

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class holds diverse algorithms used by the program. Statically declared
 *
 * @author Yuri
 */
public class Utils {

    /**
     * Tries to move a box from a starting position to a given goal. If there is
     * no path for either the box or the player returns null. Else the path it
     * took in order to move the box
     *
     * @param box The position of the box
     * @param goal The position of the goal
     * @param currentState The current state of the game
     * @return The path the player took in order to move the box. Else null
     */
    public static Movement findBoxPathToGoal(Position box, Position goal, State currentState) {
        Position player = currentState.getPlayer();

        return null;
    }

    /**
     * This function tries to find a path from start to goal w/o having to move
     * any boxes. If such path exists it is returned as a String. Else null
     *
     * @param start The start position
     * @param goal The goal position
     * @return The path from start to goal w/o moving boxes or null if no such
     * path exists. Returns null if no path exists.
     */
    public static String findPath(Position start, Position goal, State currentState) {

        // Fields used by the findPath algorithm
        HashSet<Position> visited = new HashSet<Position>();
        PriorityQueue<Step> prio = new PriorityQueue<Step>();

        // add the first position to the queue
        prio.add(new Step(start, Position.manhattanDistance(start, goal), ""));

        // set it as visited
        visited.add(start);

        // start the Best first search
        while (!prio.isEmpty()) {

            // Poll the head of this queue
            Step currentStep = prio.poll();
            Position current = currentStep.getNext();

            // If we are there return the path
            if (current.equals(goal)) {
                return currentStep.getPath();
            }

            // get all empty adjucent positions
            for (Position next : getAdjucentPositions(current, currentState)) {

                // if we haven't visited this place before
                if (!visited.contains(next)) {

                    // find out the direction of this step
                    // First add the current path
                    StringBuilder direction = new StringBuilder(currentStep.getPath());

                    // loop through Main.Moves to find out the direction we moved on
                    for (int i = 0; i < Main.MOVES.length; i++) {
                        if (next.getRow() == (current.getRow() + Main.MOVE_Y[i])
                                && next.getCol() == (current.getCol() + Main.MOVE_X[i])) {

                            // Append the direction we moved on
                            direction.append(Main.MOVES[i]);
                        }
                    }

                    // add it to the prio queue. This will order them by Manhattan
                    // distance to the goal
                    prio.add(new Step(next, Position.manhattanDistance(next, goal), direction.toString()));

                    // set it as visited
                    visited.add(next);

                } // end if we already visited this place

            } // end for all empty adjucent positions

        } // end the Best First Search

        // if we reach here there was no path
        return null;
    }

    /**
     * Tries to move the box in the direction given by player relative to box.
     * If this is possible it will return a {@link Movement}, else null.
     *
     * @param box The positioning of the box
     * @param player The positioning of the player
     * @param currentState The current State
     * @return The Movement which holds the new box position, the new player
     * position and the path there. Returns null if the box cannot be moved.
     */
    public static Movement tryToMoveBox(Position box, Position player, State currentState) {

        // the player's current row and column
        int row = player.getRow();
        int column = player.getCol();

        // for each possible direction
        for (int i = 0; i < Main.MOVES.length; i++) {

            // the position the player would have if he moved (U D L R)
            Position adjucent = new Position(row + Main.MOVE_Y[i], column + Main.MOVE_X[i]);

            // if this position is where the box is
            if (adjucent.equals(box)) {

                // Create the position where the box will now be
                Position new_box_position = new Position(box.getRow() + Main.MOVE_Y[i], box.getCol() + Main.MOVE_X[i]);
                String path = new StringBuilder().append(Main.MOVES[i]).toString();

                // if the place where we want to place the box is empty
                if (Main.isEmptyPosition(new_box_position) && !currentState.getBoxes().contains(new_box_position)) {
                    return new Movement(new_box_position, adjucent, path);
                }
            }

        }

        // if we reached here we just couldn't move the box!
        return null;
    }
    
    /**
     * Tries to move the box in the direction given by player relative to box.
     * If this is possible it will return a {@link Movement}, else null.
     *
     * @param box The positioning of the box
     * @param player The positioning of the player
     * @param currentState The current State
     * @return The Movement which holds the new box position, the new player
     * position and the path there. Returns null if the box cannot be moved.
     */
    public static Movement tryToMoveBoxBack(Position box, Position player, State currentState) {

        // the player's current row and column
        int row = box.getRow();
        int column = box.getCol();

        // for each possible direction
        for (int i = 0; i < Main.MOVES.length; i++) {

            // the position the box would have if he moved (U D L R)
            Position adjucent = new Position(row + Main.MOVE_Y[i], column + Main.MOVE_X[i]);

            // if this position is where the player is
            if (adjucent.equals(player)) {

                // Create the position where the box will now be
                Position new_player_position = new Position(player.getRow() + Main.MOVE_Y[i], player.getCol() + Main.MOVE_X[i]);
                String path = new StringBuilder().append(Main.MOVES[i]).toString();

                // if the place where we want to place the box is empty
                if (Main.isEmptyPosition(new_player_position) && !currentState.getBoxes().contains(new_player_position)) {
                    return new Movement(adjucent, new_player_position, path);
                }
            }

        }

        // if we reached here we just couldn't move the box!
        return null;
    }

    /**
     * Returns a list with all (empty) possible adjacent positions to a box
     *
     * @param box The position of the box
     * @param currentState The current State of the game
     * @return A list with all available (empty) adjacent positions to the box.
     */
    public static List<Position> getAdjucentPositions(Position box, State currentState) {

        // the row and column of this box
        int row = box.getRow();
        int col = box.getCol();

        // the list to be returned with the valid adjucent positions
        List<Position> adjucent = new ArrayList<Position>();

        // for each possible adjucent position to the box
        for (int i = 0; i < Main.MOVES.length; i++) {

            // the adjacent cell to this box (U D L R)
            Position pos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);

            // check if this spot is empty or not
            if (Main.isEmptyPosition(pos) && !currentState.getBoxes().contains(pos)) {
                // add it to the list
                adjucent.add(pos);
            }
        } // end adding positions

        return adjucent;
    }
    
    /**
     * Returns a list with all (empty) possible adjacent positions to a box in backwards
     * movement. It'll check that two fields are empty
     *
     * @param box The position of the box
     * @param currentState The current State of the game
     * @return A list with all available (empty) adjacent positions to the box.
     */
    public static List<Position> getAdjucentPositionsBack(Position box, State currentState) {

        // the row and column of this box
        int row = box.getRow();
        int col = box.getCol();

        // the list to be returned with the valid adjucent positions
        List<Position> adjucent = new ArrayList<Position>();

        // for each possible adjucent position to the box
        for (int i = 0; i < Main.MOVES.length; i++) {

            // the adjacent cell to this box (U D L R)
            Position pos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);
            // the adjacent cell to this first cell
            Position pos2 = new Position(row + Main.MOVE_Y[i]*2, col + Main.MOVE_X[i]*2);

            // check if this spot is empty or not
            if (Main.isEmptyPosition(pos) && !currentState.getBoxes().contains(pos)) {
            	if (Main.isEmptyPosition(pos2) && !currentState.getBoxes().contains(pos2)) {
                // add it to the list
              	 adjucent.add(pos);
            	}
            }
        } // end adding positions

        return adjucent;
    }

    public static List<Position> getAllAdjucentPositions(Position box, State currentState) {
        // the row and column of this box
        int row = box.getRow();
        int col = box.getCol();

        // the list to be returned with the valid adjucent positions
        List<Position> adjucent = new ArrayList<Position>();

        // for each possible adjucent position to the box
        for (int i = 0; i < Main.MOVES.length; i++) {

            // the adjacent cell to this box (U D L R)
            Position pos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);

            // add it to the list
            adjucent.add(pos);

        } // end adding positions

        return adjucent;
    }
    
    /**
     * Translate a String from resolving the game backwards to a one resolving it forwards
     * @param back
     * @return
     */
    public static String translateBack(String back) {
    	 StringBuilder builder=new StringBuilder(back); 
    	 String reverse = builder.reverse().toString();
    	 String result = "";
    	 for(int i=0; i<reverse.length();i++) {
    		 Character a =  reverse.charAt(i);
    		 if(a==' ') {
    			 result += ' ';
    		 } else if(a=='R') {
    			 result += 'L';
    		 } else if(a=='U') {
    			 result += 'D';
    		 } else if(a=='D') {
    			 result += 'U';
    		 } else if(a=='L') {
    			 result += 'R';
    		 } else {
    			 // Should not enter here!
    		 }
    	 }
    	return result;
    }
}

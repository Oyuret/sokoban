import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
 
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
    public static State findBoxPathToGoal(Position box, Position goal, State currentState) {
        HashSet<State> visited = new HashSet<>();
        PriorityQueue<StateHolder> prio = new PriorityQueue<>();
        
        // add the first state to the prio
        prio.add(new StateHolder(currentState, box));
        
        while(!prio.isEmpty()) {
            StateHolder currentStateHolder = prio.poll();
            State current = currentStateHolder.getState();
            Position currentBox = currentStateHolder.getBox();
            Position player = current.getPlayer();
            
            if(current.getBoxes().contains(goal)) {
                current.updateValue();
                return current;
            }
            
            for (Position adjucent : getAdjucentPositions(currentBox, current)) {
                // find out if we can reach it from our current position w/o
                // moving any boxes
                String path_to_adjucent = findPath(player, adjucent, current);
 
                // check if there was such path
                if (path_to_adjucent != null) {
 
                    // try to move the box
                    Movement moved_path = tryToMoveBox(currentBox, adjucent, current);
 
                    // check if we actually managed to move the box
                    if (moved_path != null) {
 
                        // create a new state
                        State new_state;
                        try {
                            // clone this state
                            new_state = (State) current.clone();
 
                            // update the BoxState we just moved
                            new_state.boxes.remove(currentBox);
                            new_state.boxes.add(moved_path.getBox());
 
                            // update the player state
                            new_state.player = moved_path.getPlayer();
 
                            // update the current path
                            new_state.current_path = new StringBuilder(new_state.current_path).
                                    append(path_to_adjucent).append(moved_path.getPath()).toString();
 
                            //new_state.updateValue();
                            new_state.value = Position.manhattanDistance(moved_path.getBox(), goal);
                            /*Update the reachable positions*/
                            new_state.setReachPositions();
                            // add this state to the list to return
                            if(!visited.contains(new_state)) {
                                visited.add(new_state);
                                prio.add(new StateHolder(new_state, moved_path.getBox()));
                            }
 
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(State.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } // end checking if moved_path is null   
                } // end checking if path to box is null
            } // end for each adjucent position to a box
            
            
        }
 
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
        PlayerState player = new PlayerState("", currentState, goal, start);
        return bestFirstSearch(player, goal);
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
 
        List<Position> adjucent = new ArrayList<>();
 
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
        List<Position> adjucent = new ArrayList<>();
 
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
    
 
    /**
     * Translate a String from resolving the game backwards to a one resolving it forwards
     * @param back The string to be translated
     * @return The translated Path as if we had walked forward
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
    
    /*---------------------------SEARCH FUNCTIONS--------------------*/
    /**
     * A generic best first search. The heuristic is defined by the class
     * implementing the interface GenericState
     * @param start The starting State
     * @param goal The position to reach
     * @return The path we would have walked to get to the goal w/o moving any
     * boxes, else null.
     */
    public static String bestFirstSearch(GenericState start, Position goal) {
        // Initialization
        PriorityQueue<GenericState> fringe = new PriorityQueue<>();
        Set<GenericState> visitedStates = new HashSet<>();
 
        fringe.add(start);
        visitedStates.add(start);
 
        while (fringe.size() > 0) {
            //Pop new state
            GenericState state = fringe.poll();
            //Check if arrived to goal
            if (state.isGoal(goal)) {
                return state.getPath();
            } else { //Expand the state
                List<GenericState> nextStates = state.getNextStates();
                for (int i = 0; i < nextStates.size(); i++) {
                    GenericState s = nextStates.get(i);
                    if (!visitedStates.contains(s)) //Add next States to the fringe if they are not visited
                    {
                        fringe.add(s);
                        visitedStates.add(s);
                    }
                }
            }
        }//end while        
        return null;
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds diverse algorithms used by the program. Statically declared
 * @author Yuri
 */
public class Utils {
    
    /**
     * This function tries to find a path from start to goal w/o
     * having to move any boxes
     * @param start The start position
     * @param goal The goal position
     * @return The path from start to goal w/o moving boxes or null if no such path exists.
     * Returns null if no path exists.
     */
    public static String findPath(Position start, Position goal, State currentState) {
        return null;
    }
    
    /**
     * Tries to move the box in the direction given by player -> box.
     * If this is possible it will return the path taken else null
     * @param box The positioning of the box
     * @param player The positioning of the player
     * @param currentState The current State
     * @return The Movement which holds the new box position, the new player position and the path there.
     * Returns null if the box cannot be moved.
     */
    public static Movement tryToMoveBox(Position box, Position player, State currentState) {
        
        // the player's current row and column
        int row = player.getRow();
        int column = player.getCol();
        
        // for each possible direction
        for(int i=0; i<Main.MOVES.length; i++) {
            
            // the position the player would have if he moved (U D L R)
            Position adjucent = new Position(row + Main.MOVE_Y[i], column + Main.MOVE_X[i]);
            
            // if this position is where the box is
            if(adjucent.equals(box)) {
                
                // Create the position where the box will now be
                Position new_box_position = new Position(box.getRow() + Main.MOVE_Y[i], box.getCol() + Main.MOVE_X[i]);
                String path = new StringBuilder().append(Main.MOVES[i]).toString();
                
                // if the place where we want to place the box is empty
                if(Main.isEmptyPosition(new_box_position) && !currentState.getBoxes().contains(new_box_position)) {
                    return new Movement(new_box_position, adjucent, path);
                }
            }
            
        }
        
        // if we reached here we just couldn't move the box!
        return null;
    }
    
    /**
     * Returns a list with all possible adjacent positions to a box
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
        for (int i=0; i < Main.MOVES.length; i++) {
            
            // the adjacent cell to this box (U D L R)
            Position pos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);
            
            // check if this spot is empty or not
            if(Main.isEmptyPosition(pos) && !currentState.getBoxes().contains(pos)) {
                // add it to the list
                adjucent.add(pos);
            }
        } // end adding positions
        
        return adjucent;
    }
    
}

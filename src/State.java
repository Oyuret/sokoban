
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State implements Cloneable {
    
    private Position player;
    private List<Position> boxes;
    private String current_path;
    
    public State(Position player, List<Position> boxes, String current_path) {
        this.player = player;
        this.boxes = boxes;
        this.current_path = current_path;
    }

    // Reimplement hashcode, Path Finding checking current position
    
    /**
     * Fills up the given list with all possible new states which involve moving
     * a box
     * @param nextStates 
     */
    public void getNextMoves(List<State> nextStates) {
        
        // for each box. Chech which boxes we can reach
        for(Position box : boxes) {
            // for each empty position adjucent to this box
            for(Position adjucent : Utils.getAdjucentPositions(box, this)) {
                // find out if we can reach it from our current position w/o
                // moving any boxes
                String path_to_adjucent = Utils.findPath(player, adjucent, this);
                
                // check if there was such path
                if(path_to_adjucent!=null) {
                    
                    // try to move the box
                    Movement moved_path = Utils.tryToMoveBox(player, player, this);
                    
                    // check if we actually managed to move the box
                    if(moved_path!=null) {
                        
                        // create a new state
                        State new_state;
                        try {
                            // clone this state
                            new_state = (State) this.clone();
                            
                            // update the BoxState we just moved
                            new_state.boxes.remove(box);
                            new_state.boxes.add(moved_path.getBox());
                            
                            // update the player state
                            new_state.player = moved_path.getPlayer();
                            
                            // update the current path
                            new_state.current_path = new StringBuilder(new_state.current_path).
                                    append(path_to_adjucent).append(moved_path.getPath()).toString();
                            
                            nextStates.add(new_state);
                            
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(State.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } // end checking if moved_path is null   
                } // end checking if path to box is null
            } // end for each adjucent position to a box
        } // end for each box
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.player);
        hash = 97 * hash + Objects.hashCode(this.boxes);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        
        // if we don't have exactly the same box positions
        // return false
        if (!this.boxes.containsAll(other.boxes)) {
            return false;
        }
        
        // if there is no path between player positions which doesn't move any
        // boxes then it's a different state
        if(Utils.findPath(this.player, other.player, this) == null) {
            return false;
        }
        
        return true;
    }
    
    
    
    public List<Position> getBoxes() {
        return this.boxes;
    }

    public Position getPlayer() {
        return player;
    }

    public String getCurrent_path() {
        return current_path;
    }
    
    
}

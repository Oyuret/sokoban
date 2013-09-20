

import java.util.Objects;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class is just a holder for a movement. Used by the State class
 * @author Yuri
 */
public class Movement {
    Position box;
    Position player;
    String path;
    
    /**
     * The constructor for Movement
     * @param box
     * @param player
     * @param path 
     */
    public Movement(Position box, Position player, String path) {
        this.box = box;
        this.player = player;
        this.path = path;
    }

    /**
     * Returns the boxe's new position
     * @return The new position of the box
     */
    public Position getBox() {
        return box;
    }

    /**
     * Returns the player's new position
     * @return The new position of the player
     */
    public Position getPlayer() {
        return player;
    }

    /**
     * Returns the path the player took to move the box
     * @return The direction the player moved to move the box one step.
     */
    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movement other = (Movement) obj;
        if (!Objects.equals(this.box, other.box)) {
            return false;
        }
        if (!this.player.equals(other.player)) {
            return false;
        }
        if (!this.path.equals(other.path)) {
            return false;
        }
        return true;
    }
    
    
    
    
}

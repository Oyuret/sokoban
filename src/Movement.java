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

    public Position getBox() {
        return box;
    }

    public Position getPlayer() {
        return player;
    }

    public String getPath() {
        return path;
    }
    
    
}

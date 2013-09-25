/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yuri
 */
public class StateHolder implements Comparable<StateHolder>{
    private State state;
    private Position box;
    
    public StateHolder(State state, Position box) {
        this.state = state;
        this.box = box;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Position getBox() {
        return box;
    }

    public void setBox(Position box) {
        this.box = box;
    }

    @Override
    public int compareTo(StateHolder o) {
        return this.state.value - o.state.value;
    }
    
    
    
}

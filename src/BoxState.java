

import java.util.ArrayList;
import java.util.List;

public class BoxState implements Comparable<BoxState> {

    private Position position;
    private String path;
    private int goalDistance;
    private Position goal;

    public BoxState(Position position, String path, Position goal) {
        this.position = position;
        this.path = path;
        this.goal = goal;
        this.goalDistance = Position.manhattanDistance(position, goal); //Manhattan distance to the goal
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getGoalDistance() {
        return goalDistance;
    }

    public void setGoalDistance(int goalDistance) {
        this.goalDistance = goalDistance;
    }

    /**
     * Returns a list of all possible movements for a box given its current
     * position
     *
     * @return
     */
    public List<BoxState> getNextBoxStates() {
        int row = this.position.getRow();
        int col = this.position.getCol();
        List<BoxState> nextStates = new ArrayList<BoxState>();
        for (int i = 0; i < Main.MOVES.length; i++) {
            Position newPos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);
            Position newPosOpposite = new Position(row - Main.MOVE_Y[i], col - Main.MOVE_X[i]);
            if (Main.isValidPosition(newPos) && Main.isValidPosition(newPosOpposite) && Main.isSafePosition(newPos) && Main.isEmptyPosition(newPosOpposite)) {
                nextStates.add(new BoxState(newPos, new StringBuilder(path).append(Main.MOVES[i]).toString(), this.goal));
            }
        }
        return nextStates;
    }

    /**
     * Implement the Comparable Interface. This way the states will be
     * automatically ordered in the PriorityQueue in such a way that the first
     * one is the closest to the goal. This way we can implement Best-First
     * Search.
     */
    @Override
    public int compareTo(BoxState o) {
        return this.goalDistance - o.goalDistance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((goal == null) ? 0 : goal.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BoxState other = (BoxState) obj;
        if (goal == null) {
            if (other.goal != null) {
                return false;
            }
        } else if (!goal.equals(other.goal)) {
            return false;
        }
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!position.equals(other.position)) {
            return false;
        }
        return true;
    }
}

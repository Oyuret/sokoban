/**
 * Just a holder class used by Utils in order to find a path
 * @author Yuri
 */
public class Step implements Comparable<Step> {
    private Position next;
    private int manhattanDistance;
    private String path;
    
    public Step(Position next, int manhattanDistance, String path) {
        this.next = next;
        this.manhattanDistance = manhattanDistance;
        this.path = path;
    }

    public Position getNext() {
        return next;
    }

    public int getManhattanDistance() {
        return manhattanDistance;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(Step o) {
        if(this.manhattanDistance < o.manhattanDistance) {
            return -1;
        } else if(this.manhattanDistance > o.manhattanDistance) {
            return 1;
        }
        return 0;
    }
    
}

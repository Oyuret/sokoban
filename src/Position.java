
public class Position implements Comparable<Position> {
 
    private int row;
    private int col;
 
    public int getRow() {
        return row;
    }
 
    public void setRow(int row) {
        this.row = row;
    }
 
    public int getCol() {
        return col;
    }
 
    public void setCol(int col) {
        this.col = col;
    }
 
    public Position(int row, int col) {
        super();
        this.row = row;
        this.col = col;
    }
 
    /**
     * Computes the Manhattan distance between two positions
     *
     * @param p1
     * @param p2
     * @return The manhattan distance between two positions
     */
    public static int manhattanDistance(Position p1, Position p2) {
        if(p2!=null)
                return Math.abs(p1.getCol() - p2.getCol()) + Math.abs(p1.getRow() - p2.getRow());
        return -1;
    }
 
    /**
     * Calculates the has of this position
     *
     * @return The hash of this position
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + row;
        return result;
    }
 
    /**
     * Checks if this {@link Position} is equal to another
     *
     * @param obj The position to check on
     * @return True if it's the same position lexically
     */
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
        Position other = (Position) obj;
        if (col != other.col) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }
 
    /**
     * Compares this {@link Position} to another.
     * @param o The position to compare against
     * @return -1 if greater, 1 or lesser and 0 if equal
     */
    @Override
    public int compareTo(Position o) {
        if (this.row > o.row) {
            return -1;
        } else if (this.row < o.row) {
            return 1;
        }
 
        if (this.col > o.col) {
            return -1;
        } else if (this.col < o.col) {
            return 1;
        }
 
        return 0;
 
    }
}

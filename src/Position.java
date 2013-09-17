
public class Position {
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
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static int manhattanDistance(Position p1,Position p2){
		return Math.abs(p1.getCol()-p2.getCol()) + Math.abs(p1.getRow()-p2.getRow());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	
}

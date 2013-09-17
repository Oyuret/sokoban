
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
	
	public static int manhattanDistance(Position p1,Position p2){
		return Math.abs(p1.getCol()-p2.getCol()) + Math.abs(p1.getRow()-p2.getRow());
	}
	
	
}

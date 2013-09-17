import java.util.ArrayList;
import java.util.List;


public class BoxState implements Comparable<BoxState> {
	private Position position;
	private String path;
	private int goalDistance;
	private Position goal;
	
	public BoxState(Position position,String path,Position goal) {
		super();
		this.position=position;
		this.path=path;
		this.goal=goal;
		this.goalDistance= Position.manhattanDistance(position, goal); //Manhattan distance to the goal
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


	public List<BoxState> getNextBoxStates(){
		int row = this.position.getRow();
		int col = this.position.getCol();
		List<BoxState> nextStates = new ArrayList<BoxState>();
		for(int i=0;i<4;i++) {
			Position newPos 		= new Position(row+Main.MOVE_Y[i],col+Main.MOVE_X[i]);
			Position newPosOpposite = new Position(row-Main.MOVE_Y[i],col-Main.MOVE_X[i]);
			if(Main.isEmptyPosition(newPos) && Main.isEmptyPosition(newPosOpposite))
					nextStates.add(new BoxState(newPos,new StringBuilder(path).append(Main.MOVES[i]).toString(),this.goal));
		}		
		return nextStates;
	}

	@Override
	public int compareTo(BoxState o) {
		return this.goalDistance-o.goalDistance;
	}
}

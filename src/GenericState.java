import java.util.List;


public interface GenericState {

	public boolean isGoal(Position goal);
	
	public List<GenericState> getNextStates();
	
	public String getPath();
	
}

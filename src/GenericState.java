import java.util.List;

/**
 * An interface used with the Generic searching algorithms
 * 
 */
public interface GenericState {

	public boolean isGoal(Position goal);
	
	public List<GenericState> getNextStates();
	
	public String getPath();
	
}

import java.util.ArrayList;
import java.util.List;
 
 
public class PlayerState implements GenericState, Comparable<PlayerState>{
        String path;
        State state;
        Position goal;
        Position own;
        int value;
        public PlayerState(String path, State s, Position goal, Position own){
                this.path = path;
                this.state = s;
                this.goal = goal;
                this.own  = own;
                this.value = Position.manhattanDistance(own, goal);
        }
        
        @Override
        public boolean isGoal(Position goal) {
                return this.own.equals(goal);
        }
 
        @Override
        public List<GenericState> getNextStates() {
        int row = this.own.getRow();
        int col = this.own.getCol();
        List<GenericState> nextStates = new ArrayList<GenericState>(); 
        for (int i = 0; i < Main.MOVES.length; i++) {
            Position pos = new Position(row + Main.MOVE_Y[i], col + Main.MOVE_X[i]);
            // check if this spot is empty or not
            if (Main.isEmptyPosition(pos) && !this.state.getBoxes().contains(pos)) {
                nextStates.add(new PlayerState(new StringBuilder(this.path).append(Main.MOVES[i]).toString(), this.state, this.goal, pos));
            }
        }
        return nextStates;
        }
 
        @Override
        public String getPath() {
                return this.path;
        }
 
        @Override
        public int compareTo(PlayerState p) {
                return this.value-p.value;
        }
 
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((own == null) ? 0 : own.hashCode());
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
                PlayerState other = (PlayerState) obj;
                if (own == null) {
                        if (other.own != null)
                                return false;
                } else if (!own.equals(other.own))
                        return false;
 
                return true;
        }
        
        
 
}
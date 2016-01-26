import java.util.ArrayList;
import java.util.List;

public class ZombieState {
	
	int round;
	int phase;
	int playerID;
	int thisTownID;
	
	List<State> states;
	List<State> otherStates;
	
	State thisState;
	
	public static void main(String[] args){
	    new ZombieState().sleep(args[0].split(";"));
	}
	
	private void sleep(String[] args) {
	
	    round = Integer.parseInt(args[0]);
	    thisTownID = Integer.parseInt(args[1]);
	
	    states = new ArrayList<>();
	    otherStates = new ArrayList<>();
	
	    for (int i = 2; i < args.length; i++){
	        states.add(new State(args[i]));
	    }
	
	    for (State state : states){
	        if (state.isMine()) {
	            thisState = state;
	        } else {
	            otherStates.add(state);
	        }
	    }
	
	    StringBuilder sb = new StringBuilder();
	    if(round == 1)
	        System.out.println("TTT");
	    else if(round == 50)
	        System.out.println("CCC");
	    else
	    {
	        while(thisState.lethalityRate >= 4)
	        {
	            sb.append("I");
	            thisState.lethalityRate -= 4;
	        }
	        sb.append("DDD");
	        System.out.println(sb.toString().substring(0, 3));
	    }
	}
	
	private class State {
	
	    private final int ownerId;
	    public int sane;
	    public int infected;
	    public int dead;
	    public int infectionRate;
	    public int contagionRate;
	    public int lethalityRate;
	    public int migrationRate;
	
	    public State(String string) {
	        String[] args = string.split("_");
	        ownerId = Integer.parseInt(args[0]);
	        sane = Integer.parseInt(args[1]);
	        infected = Integer.parseInt(args[2]);
	        dead = Integer.parseInt(args[3]);
	        infectionRate = Integer.parseInt(args[4]);
	        contagionRate = Integer.parseInt(args[5]);
	        lethalityRate = Integer.parseInt(args[6]);
	        migrationRate = Integer.parseInt(args[7]);
	    }
	
	    public int getOwnerId() {
	        return ownerId;
	    }
	
	    public boolean isMine(){
	        return getOwnerId() == playerID;
	    }
	
	}

}
import java.util.ArrayList;
import java.util.List;

public class MedicBot {

	int round;
	int phase;
	int playerID;
	int thisTownID;
	
	List<State> states;
    List<State> otherStates;

    State thisState;
	
    public static void main(String[] args){
        new MedicBot().sleep(args[0].split(";"));
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
        
        if (round % 3 == 0)
        	System.out.println("MMM");
        else if (round % 3 == 1)
        	System.out.println("III");
        else
        	System.out.println("EEE");
    }
    
    private class State {
		 
        private final int ownerId;
        private final int healthy;
        private final int infected;
        private final int dead;
        private final int infectionRate;
        private final int contagionRate;
        private final int lethalityRate;
        private final int migrationRate;

        public State(String string) {
            String[] args = string.split("_");
            ownerId = Integer.parseInt(args[0]);
            healthy = Integer.parseInt(args[1]);
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

		public int getHealthy() {
			return healthy;
		}

		public int getInfected() {
			return infected;
		}

		public int getDead() {
			return dead;
		}

		public int getInfectionRate() {
			return infectionRate;
		}

		public int getContagionRate() {
			return contagionRate;
		}

		public int getLethalityRate() {
			return lethalityRate;
		}

		public int getMigrationRate() {
			return migrationRate;
		}

		public boolean isMine(){
            return getOwnerId() == thisTownID;
        }

    }

}

import java.util.ArrayList;
import java.util.List;

public class TrumpBot {

	int round;
	int phase;
	int playerID;
	int thisTownID;
	
	List<State> states;
    List<State> otherStates;

    State thisState;
	
    public static void main(String[] args){
        new TrumpBot().sleep(args[0].split(";"));
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
        
        for (int j =0;j<3;j++){
            if (thisState.infected > 2) {
              System.out.print("C");
              thisState.infected -= 10;
            }
            else if (thisState.migrationRate > 2) {
              System.out.print("B");
              thisState.migrationRate -= 10;
            }
            else if (thisState.infectionRate > 2) {
              System.out.print("M");
              thisState.infectionRate  -= 10;
            }
            else System.out.println("T");
        }
    }
    
    private class State {
		 
        public final int ownerId;
        public final int healthy;
        public int infected;
        public final int dead;
        public int infectionRate;
        public final int contagionRate;
        public final int lethalityRate;
        public int migrationRate;

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
		
		public void setInfected(int infected) {
			this.infected = infected;
		}

		public void setInfectionRate(int infectionRate) {
			this.infectionRate = infectionRate;
		}

		public boolean isMine(){
            return getOwnerId() == playerID;
        }

    }

}

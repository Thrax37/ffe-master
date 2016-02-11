import java.util.ArrayList;
import java.util.List;

public class InfectionBot {

	int round;
	int phase;
	int playerID;
	int thisTownID;
	
	List<State> states;
    List<State> otherStates;

    State thisState;
	
    public static void main(String[] args){
        new InfectionBot().sleep(args[0].split(";"));
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
        
        int lethal=10;
        if(round == 1) System.out.print("OOO");
        else {
        for(int x=0;x<3;x++) {
          if(thisState.getLethalityRate() <=6 && x == 0) {
            if(lethal<50) {
              System.out.print("W");
              lethal = lethal + 3;
              }
            else System.out.print("D");
          } else if(thisState.getLethalityRate()<=2 && x == 1) {
            if(lethal<50) {
              System.out.print("W");
              lethal = lethal + 3;
              }
            else System.out.print("D");
          } else if(thisState.getLethalityRate() ==0 && x == 2) {
            System.out.print("D");
          } else System.out.print("I");
        }
        }
	          
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

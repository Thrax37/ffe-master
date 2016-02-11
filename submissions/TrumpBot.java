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
        //states.add(new State(args[thisTownID+2]));

        otherStates = new ArrayList<>();


        for (int i = 2; i < args.length; i++){
            states.add(new State(args[i]));
        }

        for (State state : states){
            if (state.ownerId == thisTownID) {
                thisState = state;
            } else {
                otherStates.add(state);
            }
        }

        StringBuilder cmd = new StringBuilder();

        for (int j =0;j<3;j++){
            if (thisState.infected > 6) {
              if (thisState.infected > 20){
                cmd.append("Q");
                thisState.infected -= 30;
              }
              else {
                cmd.append("C");
                thisState.infected -= 10;
              }
            }
            else if (thisState.migrationRate > 2) {
              cmd.append("B");
              thisState.migrationRate -= 10;
            }
            else if (thisState.infectionRate > 4) {
              cmd.append("M");
              thisState.infectionRate  -= 4;
            }
            else if (thisState.contagionRate > 10 || thisState.lethalityRate > 6 || thisState.infectionRate > 0) {
              cmd.append("V");
              thisState.contagionRate  -= 4;
              thisState.lethalityRate  -= 2;
              thisState.infectionRate  -= 1;
            }

            else if (thisState.infected % 10 <= 6){
              cmd.append("T");
              thisState.infected +=4;
            }
            else cmd.append("V");
        }
        System.out.print(cmd.reverse());
    }
    
    private class State {
		 
        public final int ownerId;
        public final int healthy;
        public int infected;
        public final int dead;
        public int infectionRate;
        public int contagionRate;
        public int lethalityRate;
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
            return getOwnerId() == thisTownID;
        }

    }

}

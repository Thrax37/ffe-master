import java.util.ArrayList;
import java.util.List;

public class Mooch {

    int round;
    int phase;
    int playerID;

    List<State> states;
    List<State> otherStates;

    State thisState;

    public static void main(String[] args){
        new Mooch().sleep(args[0].split(";"));
    }

    private void sleep(String[] args) {

        round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);

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

        if (round == 50) {
          System.out.println("CCC");
          return;
        }

        String output = "";

        while( thisState.migrationRate < 100) {
          output += "O";
          thisState.migrationRate += 10;
        }

        while( thisState.lethalityRate >= 4) {
          output += "I";
          thisState.lethalityRate -= 4;
        }

        while( thisState.lethalityRate > 0) {
          output += "V";
          thisState.lethalityRate -= 2;
          thisState.contagionRate -= 4;
          thisState.infectionRate -= 1;
        }

        while( thisState.contagionRate >= 8) {
          output += "E";
          thisState.contagionRate -= 8;
        }

        while( thisState.contagionRate > 0) {
          output += "V";
          thisState.lethalityRate -= 2;
          thisState.contagionRate -= 4;
          thisState.infectionRate -= 1;
        }

        while( thisState.infectionRate > 0) {
          output += "M";
          thisState.infectionRate -= 4;
        }

        while( output.length() < 3) {
          output += "C";
        }

        System.out.println(output.substring(0,3));

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
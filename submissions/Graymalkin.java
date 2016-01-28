import java.util.ArrayList;
import java.util.List;

public class Graymalkin {

    int round;
    int phase;
    int playerID;
    int thisTownID;

    List<State> states;
    List<State> otherStates;

    State thisState;

    public static void main(String[] args) {
        new Graymalkin().sleep(args[0].split(";"));
    }

    private void sleep(String[] args) {

        round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);

        states = new ArrayList<>();
        otherStates = new ArrayList<>();

        for (int i = 2; i < args.length; i++) {
            states.add(new State(args[i]));
        }

        for (State state : states) {
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

        String out = "";

        if (round == 1) {
            out += "B";
        }

        if (thisState.infectionRate < 10 && thisState.infected >= 10) {
            out += "C";
            thisState.infected -= 10;
        }

        while (thisState.infectionRate >= 4) {
            out += "M";
            thisState.infectionRate -= 4;
        }

        while (thisState.infectionRate > 0) {
            out += "V";
            thisState.infectionRate -= 1;
        }

        while (out.length() < 3) {
            if (thisState.infected > 0) {
                out += "C";
                thisState.infected -= 10;
            } else if (thisState.contagionRate > 0) {
                out += "E";
                thisState.contagionRate -= 8;
            } else if (thisState.lethalityRate > 0) {
                out += "I";
                thisState.lethalityRate -= 4;
            } else {
                out += "N";
            }
        }

        System.out.println(out.substring(0, 3));
    }

    private class State {

        private final int ownerId;
        private int sane;
        private int infected;
        private int dead;
        private int infectionRate;
        private int contagionRate;
        private int lethalityRate;
        private int migrationRate;

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

        public int getSane() {
            return sane;
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

        public boolean isMine() {
            return getOwnerId() == playerID;
        }
    }
}
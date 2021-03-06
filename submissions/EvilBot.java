import java.util.ArrayList;
import java.util.List;

public class EvilBot {

int round;
int phase;
int playerID;
int thisTownID;

List<State> states;
List<State> otherStates;

State thisState;
String action = "";
int cc=0; // command count

public static void main(String[] args){
    new EvilBot().sleep(args[0].split(";"));
}

private void action(String newAction) {
    action += newAction;
    cc+= newAction.length();
    if (cc>=3) {
        System.out.println(action.substring(0, 3));
        System.exit(0);;
    }
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

    // Round specific commands
    if (round == 1 )                                { action("B");   }
    if (round == 50)                                { action("CCC"); }

    for (int i=0;i<3;i++){
        if (thisState.getLethalityRate() >= 4)  { action("I"); thisState.lethalityRate -= 4;}
    }

    // Nothing else to do, cause trouble.
    action("DWT");
}


private class State {

    private final int ownerId;
    private int healthy;
    private int infected;
    private int dead;
    private int infectionRate;
    private int contagionRate;
    private int lethalityRate;
    private int migrationRate;

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
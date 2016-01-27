import java.util.ArrayList;
import java.util.List;

public class InfectedTown {

    int playerID;
    State thisState;

    public static void main(String[] args){
        new InfectedTown().sleep(args[0].split(";"));
    }

    private void sleep(String[] args) {
        // Parse arguments
        int round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);

        for (int i = 2; i < args.length; i++){
            thisState = new State(args[i]);
            if(thisState.isMine()){
                break;
            }
        }

        // Special actions on turn 1 and 50.
        String action="";
        if(round == 1){
            action = "B";
        } else if(round == 50){
            action="CCC";
        } 

        while(action.length()<3){
            if(thisState.lethalityRate<=2 && action.length()<2){
                // We still have at least one action: lets increase the 
                // lethality rate for everyone, we will decrease it with our 
                // remaining actions.
                action+="W";
                thisState.lethalityRate+=2;
            } else if (thisState.lethalityRate>=4 
                    ||(thisState.lethalityRate>0 && action.length()==2)) {
                // Don't let people die!
                action+="I";
                thisState.lethalityRate-=4;
            } else {
                // Nothing better to do, lets distract other towns by  
                // increasing some useless values
                action+="D";
            }
        }

       System.out.println(action);
    }

    private class State {
        public int ownerId;
        public int lethalityRate;

        public State(String string) {
            String[] args = string.split("_");
            ownerId = Integer.parseInt(args[0]);
            lethalityRate = Integer.parseInt(args[6]);
        }

        public boolean isMine(){
            return ownerId == playerID;
        }
    }
}
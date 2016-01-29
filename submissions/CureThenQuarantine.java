public class CureThenQuarantine {
    static int playerID;

    public static void main(String[] args)
    {
        State thisState=null;

        args = args[0].split(";");

        // Parse arguments
        int round = Integer.parseInt(args[0]);
        playerID = Integer.parseInt(args[1]);

        for (int i = 2; i < args.length; i++){
            thisState = new State(args[i]);
            if(thisState.isMine()){
                break;
            }
        }

        String action="";
        if(round == 1) action = "B"; // ensure no migration.
        else if (round == 50 ) action ="CCC"; // not much else we can do so just cure some people.

        // Highest Priority to Curing and then Quarantining infected, but do not perform either action if it would be wasteful.
        if (thisState.infected>9)
        {
            if (thisState.infected<19) action+="C";
            else if (thisState.infected<29) action+="CC";
            else if (thisState.infected<39) action+="CCC";
            else if (thisState.infected<49) action+="CQ";
            else if (thisState.infected<59) action+="CCQ";
            else if (thisState.infected<79) action+="CQQ";
            else action+="QQQ";
        }

        // Next priority is to reduce infection rate
        if (thisState.infectionRate>8) action+="MMM";
        else if (thisState.infectionRate>4) action+="MM";
        else if (thisState.infectionRate>1) action+="M";
        else if (thisState.infectionRate>0) action+="V";

        // then reduce contagion rate
        if (thisState.contagionRate>16) action+="EEE";
        else if (thisState.contagionRate>8) action+="EE";
        else if (thisState.contagionRate>1) action+="E";
        else if (thisState.contagionRate>0) action+="V";

        // and least priority is lethality rate... since we are only going to quarantine infected persons anyway.
        if (thisState.lethalityRate>8) action+="III";
        else if (thisState.lethalityRate>4) action+="II";
        else if (thisState.lethalityRate>1) action+="I";
        else if (thisState.lethalityRate>0) action+="V";

        // and if we have managed to clean up our state then we help others states.
        action+="PPP";

        System.out.println(action.substring(0,3));
    }

    static private class State {
        public int ownerId;
        public int lethalityRate;
        public int healthy;
        public int infected;
        public int infectionRate;
        public int contagionRate;

        public State(String string) {
            String[] args = string.split("_");
            ownerId = Integer.parseInt(args[0]);
            healthy = Integer.parseInt(args[1]);
            infected = Integer.parseInt(args[2]);
            infectionRate = Integer.parseInt(args[4]);
            contagionRate = Integer.parseInt(args[5]);
            lethalityRate = Integer.parseInt(args[6]);
        }

        public boolean isMine(){
            return ownerId == playerID;
        }
        public String toString()
        {
            return "H: "+healthy+" I: "+infected+" IR: "+infectionRate+" LR: "+lethalityRate+" CR: "+contagionRate;
        }
    }
}
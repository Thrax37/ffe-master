public class Researcher {
    public static void main(String[] args){
        String[] args1 = args[0].split(";");
        int id = Integer.parseInt(args1[1]);
        for (int i = 2; i < args1.length; ++ i) {
            String[] args2 = args1[i].split("_");
            if (Integer.parseInt(args2[0]) == id) {
                int infected = Integer.parseInt(args2[2]);
                if (infected == 0) {
                    System.out.println("MEI");
                } else if(infected < 15) {
                    System.out.println("MEC");
                } else {
                    System.out.println("MEQ");
                }
            }
        }
    }
}
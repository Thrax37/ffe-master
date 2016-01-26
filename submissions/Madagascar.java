public class Madagascar{
    public static void main(String[]args){
        Boolean bool = false;
        bool = args[0].startsWith("1;");

        if(bool) {
            System.out.println("BBB");
        }
        else {
            System.out.println("CVV");
        }
    }
}
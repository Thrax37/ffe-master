import java.io.File;

public class Madagascar{
    public static void main(String[]a){
        File f = null;
        Boolean bool = false;

        try {
            f = new File("Madagascar.txt");
            bool = f.exists();
            f.createNewFile();
        }
        catch(Exception e){
            System.out.println("CVV");
            return;
        }

        if(bool) {
            System.out.println("CVV");
        }
        else {
            System.out.println("BBB");
        }
    }
}
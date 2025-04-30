import java.io.ObjectInputStream;
import java.util.Scanner;

public class FilServidorXat extends Thread{
    private ObjectInputStream objectInputStream;

    public FilServidorXat(ObjectInputStream objectInputStream){
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run(){
        try {
            String msg;
            while (true) {
                msg = (String) objectInputStream.readObject();
                if(msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;    
                System.out.println("Missatge ('sortir' per tancar): Rebut: " + msg);
            }
            System.out.println("Fil de xat finalitzat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

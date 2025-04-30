import java.io.ObjectOutputStream;
import java.util.Scanner;

public class FilLectorCX extends Thread {
    private ObjectOutputStream objectInputStream;
    public FilLectorCX(ObjectOutputStream objectOutputStream){
        this.objectInputStream = objectOutputStream;
    }
    @Override
    public void run(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Missatge ('sortir' per tancar): Fil de lectura iniciat");
            String msg;
            while (true) {
                msg = scanner.nextLine();
                if(msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;
                System.out.println("Enviant missatge: " + msg);
                objectInputStream.writeObject(msg);
                objectInputStream.flush();
            }
            System.out.println("Enviant missatge: sortir");
            objectInputStream.writeObject(ServidorXat.MSG_SORTIR);
            objectInputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
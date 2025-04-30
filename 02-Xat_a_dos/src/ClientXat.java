import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXat {
    public static Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream sortida;

    public void conecta()throws Exception{
        socket = new Socket( ServidorXat.HOST, ServidorXat.PORT);
        entrada = new ObjectInputStream(socket.getInputStream());
        sortida = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Client connectat a " + ServidorXat.HOST +" "+ ServidorXat.PORT);
        System.out.println("Flux d'entrada i sortida creat.");
    }

    public void enviarMissatge(String msg)throws Exception{
        sortida.writeObject(msg);
        sortida.flush();
    }

    public void tancarClient()throws Exception{
        socket.close();
        entrada.close();
        sortida.close();
        System.out.println("Tancant client...");
        System.out.println("Client tancat.");
    }

    public static void main(String[] args) {
        try {
            ClientXat clientXat = new ClientXat();
            clientXat.conecta();
            FilLectorCX fil = new FilLectorCX(clientXat.sortida);
            fil.start();
            String msg;
            System.out.println("Rebut: Escriu el teu nom:");
            while (true) {
                msg = (String) clientXat.entrada.readObject();
                if(msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;
                System.out.println("Missatge ('sortir' per tancar): Rebut: " + msg);
            }
            fil.join();
            clientXat.tancarClient();
            System.out.println("El servidor ha tancat la connexió.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

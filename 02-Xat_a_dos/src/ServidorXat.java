import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorXat{
    public static final int PORT =  9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void iniciarServidor()throws Exception{
        serverSocket = new ServerSocket(PORT);
    } 
    public void paraServidor()throws Exception{
        serverSocket.close();
    }
    public void getNom(){

    }

    public static void main(String[] args) {
        ServidorXat servidorXat = new ServidorXat();
        try {
            servidorXat.iniciarServidor();
            servidorXat.clientSocket = servidorXat.serverSocket.accept();
            ObjectOutputStream output = new ObjectOutputStream(servidorXat.clientSocket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(servidorXat.clientSocket.getInputStream());

            System.out.println("Servidor iniciat a "+ServidorXat.HOST+":"+ServidorXat.PORT);
            System.out.println("Client connectat: /127.0.0.1");

            String nomClient = (String) input.readObject();
            System.out.println("Nom rebut: " + nomClient);

            FilServidorXat fil = new FilServidorXat(input);
            fil.start();

            System.out.println("Fil de xat creat");
            System.out.println("Fil de " + nomClient + " iniciat");

            Scanner scanner = new Scanner(System.in);
            String msg;
            while (true) {
                msg = scanner.nextLine();
                if(msg.equalsIgnoreCase(MSG_SORTIR)) break;
                output.writeObject(msg);
                output.flush();
            }
            output.writeObject(MSG_SORTIR);
            output.flush();

            fil.join();
            scanner.close();
            servidorXat.clientSocket.close();
            servidorXat.paraServidor();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
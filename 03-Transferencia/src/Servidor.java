import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public void connectar()throws Exception{
        System.out.println("Acceptant connexions ->" + HOST + ":" + PORT);
        serverSocket = new ServerSocket(PORT);
        System.out.println("Esperant connexio...");
        clientSocket = serverSocket.accept();  
        System.out.println("Connexio acceptada: " + clientSocket.getInetAddress());
    }
    public void tancarConnexio(Socket socket)throws Exception{
        socket.close();
        System.out.println("Tancat connexio amb el client: " + clientSocket.getInetAddress());
        serverSocket.close();
    }
    public void enviarFitxer()throws Exception{
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        Fitxer fitxer = new Fitxer(input.readObject().toString());
        System.out.println("Nom fitxer rebut: " + fitxer.getNom());
        byte[] content = fitxer.getContingut();
        System.out.println("Contingut del fitxer del client: " + content+ " bytes");
        System.out.println("Fitxer enviat al client: " + fitxer.getPath());
        output.writeObject(content);
        output.flush();

    }
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.connectar(); 
            System.out.println("Esperant nom del fitxer...");
            servidor.enviarFitxer();
            System.out.println("Fitxer enviat al client al client: ");
            servidor.tancarConnexio(servidor.clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
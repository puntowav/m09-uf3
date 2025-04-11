import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    public static final int PORT = 7777;
    public static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public void conecta() throws Exception{
        System.out.printf("%nServidor en marxa a %s:%d", HOST, PORT);
        serverSocket = new ServerSocket(PORT);
        System.out.printf("%nEsperant connexions a %s:%d", HOST, PORT);
        clientSocket = serverSocket.accept();
        System.out.println("Client conectat: "+ clientSocket.getInetAddress());
    }
    public void repDades(){
        try (BufferedReader bR = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while (true) {
                line = bR.readLine();
                if(line == null) break;
                System.out.println("Rebut: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void tanca() throws Exception{
        clientSocket.close();
        serverSocket.close();
        System.out.println("Servidor tancat.");
    }
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.conecta();
            servidor.repDades();
            servidor.tanca();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
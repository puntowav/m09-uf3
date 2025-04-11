import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int PORT = Servidor.PORT;
    private static final String HOST = Servidor.HOST;
    private Socket socket;
    private PrintWriter out;
    public void conecta()throws Exception{
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.printf("%nConactant al servidor en %s:%d", HOST, PORT);
    }
    public void tanca() throws Exception{
        out.close();
        socket.close();
        System.out.println("Client tancat");
    }
    public void envia(String string){
        out.println(string);
        System.out.println("Enviat al servidor: " + string);
    }
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.conecta();
            client.envia("Prova d'enviamnet 1");
            client.envia("Prova d'enviament 2");
            client.envia("Adéu!");
            BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Prem enter per tencar el client...");
            bR.readLine();
            client.tanca();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

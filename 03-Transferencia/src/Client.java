import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    public final static String DIR_ARRIBADA = System.getProperty("java.io.tmpdir");
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    public void connectar()throws Exception{
        socket = new Socket(Servidor.HOST ,Servidor.PORT);
    }   
    public void rebreFitxer()throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introdueix el nom del fitxer que vols rebre: ");
        String nomFitxer = scanner.nextLine();

        if(nomFitxer.equals("sortir")){
            scanner.close();
            return;
        }

        output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(nomFitxer);
        output.flush();
        System.out.println("Nom del fitxer enviat al servidor: " + nomFitxer);

        input = new ObjectInputStream(socket.getInputStream());

        byte[] file = (byte[]) input.readObject();
        Path desti = Paths.get(DIR_ARRIBADA + "/" + nomFitxer);
        Files.write(desti, file);

        System.out.println("Fitxer desat a: " + desti.toAbsolutePath());
        System.out.println("Mida del fitxer: " + file.length + " bytes");
    }
    public void tancarConnexio()throws Exception{
        socket.close();
    }
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connectar();
            client.rebreFitxer();
            client.tancarConnexio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

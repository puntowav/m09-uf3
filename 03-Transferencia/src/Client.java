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
    public void rebreFitxer(String nom, String guardar)throws Exception{
        output.writeObject(nom);
        output.flush();

        byte[] file = (byte[]) input.readObject();
        if(file == null){
            System.out.println("El servidor no ha trobat el fitxer `" + nom + "`");
            return;
        }

        
        Path desti = Paths.get(guardar);
        Path parePath = desti.getParent();
        if(parePath != null && Files.exists(parePath)){
            Files.createDirectories(parePath);
        }

        Files.write(desti, file);
        System.out.println("Fitxer desat a: " + desti.toAbsolutePath());
        System.out.println("Mida del fitxer: " + file.length + " bytes");
    }
    public void tancarConnexio()throws Exception{
        socket.close();
    }

    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        try {
            client.connectar();

            client.output = new ObjectOutputStream(client.socket.getOutputStream());
            client.input = new ObjectInputStream(client.socket.getInputStream());


            while (true) {
                System.out.print("Introdueix el nom del fitxer (o 'sortir'): ");
                String nom = scanner.nextLine();
                if(nom.equalsIgnoreCase("sortir")){
                    client.output.writeObject(nom);
                    client.output.flush();
                    break;
                }
                System.out.print("Nom fitxer a guardar: ");
                String local = scanner.nextLine().trim();
                client.rebreFitxer(nom, local);
            }
            client.tancarConnexio();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            scanner.close();
        }
    }
}

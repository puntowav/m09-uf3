import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServidorXat servidorXat;
    private String nom;
    private boolean sortir = false;

    public GestorClients(Socket socket, ServidorXat servidorXat) throws Exception {
        this.socket = socket;
        this.servidorXat = servidorXat;
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream(socket.getInputStream());
    }

    public String getNom() {
        return nom;
    }

    @Override
    public void run() {
        try {
            while (!sortir) {
                String msgRaw = (String) in.readObject();
                processaMissatge(msgRaw);
            }
        } catch (Exception e) {
        } finally {
            try { socket.close(); } catch (Exception e) {}
        }
    }

    public void enviarRaw(String raw) throws Exception {
        out.writeObject(raw);
    }

    public void processaMissatge(String msgRaw) {
        String codi = Missatge.getCodiMissatge(msgRaw);
        String[] parts = Missatge.getPartsMissatge(msgRaw);
        try {
            switch (codi) {
                case Missatge.CODI_CONECTAR:
                    this.nom = parts[1];
                    servidorXat.afegirClient(this);
                    break;
                case Missatge.CODI_MSG_GRUP:
                    servidorXat.enviarMissatgeGrup(msgRaw);
                    break;
                case Missatge.CODI_MSG_PERSONAL:
                    String destinatari = parts[1];
                    String missatge    = parts[2];
                    servidorXat.enviarMissatgePersonal(destinatari, this.nom, missatge);
                    break;
                case Missatge.CODI_SORTIR_CLIENT:
                    servidorXat.eliminarClient(this.nom);
                    sortir = true;
                    break;
                case Missatge.CODI_SORTIR_TOTS:
                    servidorXat.finalitzarXat();
                    sortir = true;
                    break;
                default:
                    System.out.println("ERROR: codi desconegut -> " + codi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

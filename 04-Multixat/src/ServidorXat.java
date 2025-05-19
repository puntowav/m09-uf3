import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;
    private ServerSocket serverSocket;

    public void servidorAEscoltar() throws Exception {
        serverSocket = new ServerSocket(PORT, 0, InetAddress.getByName(HOST)); 
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }

    public void pararServidor() throws Exception {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    public void finalitzarXat() throws Exception {
        for (GestorClients g : clients.values()) {
            g.enviarRaw(Missatge.getMissatgeSortirTots(MSG_SORTIR));
        }
        clients.clear();
        sortir = true;
        System.out.println("Tancant tots els clients.");
    }

    public void afegirClient(GestorClients gestor) throws Exception {
        String nom = gestor.getNom();
        clients.put(nom, gestor);
        System.out.println(nom + " connectat."); 
        String debug = "DEBUG: multicast Entra: " + nom;
        for (GestorClients g : clients.values()) {
            g.enviarRaw(Missatge.getMissatgeGrup(debug));
        }
    }

    public void eliminarClient(String nom) {
        if (clients.remove(nom) != null) {
            System.out.println(nom + " ha sortit.");
        }
    }

    public void enviarMissatgeGrup(String raw) throws Exception {
        for (GestorClients g : clients.values()) {
            g.enviarRaw(raw);
        }
    }

    public void enviarMissatgePersonal(String destinatari, String remitent, String msg) throws Exception {
        System.out.println("Missatge personal per (" + destinatari + ") de (" + remitent + "): " + msg);
        GestorClients g = clients.get(destinatari);
        if (g != null) {
            String raw = Missatge.CODI_MSG_PERSONAL + "#" + remitent + "#" + msg;
            g.enviarRaw(raw);
        }
    }

    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        try {
            servidor.servidorAEscoltar();
            while (!servidor.sortir) {
                Socket socketClient = servidor.serverSocket.accept();
                System.out.println("Client connectat: " + socketClient.getRemoteSocketAddress());
                GestorClients gestor = new GestorClients(socketClient, servidor);
                gestor.start();
            }
            servidor.pararServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

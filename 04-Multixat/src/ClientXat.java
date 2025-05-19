import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean sortir = false;

    public void connecta() throws Exception {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        out    = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);
    }

    public void enviarMissatge(String raw) throws Exception {
        out.writeObject(raw);
    }

    public void tancarClient() throws Exception {
        sortir = true;
        if (in    != null) in.close();
        if (out   != null) out.close();
        if (socket!= null) socket.close();
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while (!sortir) {
                String msgRaw = (String) in.readObject();
                String codi   = Missatge.getCodiMissatge(msgRaw);
                String[] p    = Missatge.getPartsMissatge(msgRaw);
                switch (codi) {
                    case Missatge.CODI_SORTIR_TOTS:
                        sortir = true;
                        break;
                    case Missatge.CODI_MSG_PERSONAL:
                        System.out.println("Missatge de (" + p[1] + "): " + p[2]);
                        break;
                    case Missatge.CODI_MSG_GRUP:
                        System.out.println(p[1]);
                        break;
                    default:
                        System.out.println("Error: codi desconegut " + codi);
                }
            }
        } catch (Exception e) {
        } finally {
            try { tancarClient(); } catch (Exception e) {}
        }
    }

    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("  1.- Conectar al servidor (primer pas obligatori)");
        System.out.println("  2.- Enviar missatge personal");
        System.out.println("  3.- Enviar missatge al grup");
        System.out.println("  4.- (o línia en blanc)-> Sortir del client");
        System.out.println("  5.- Finalitzar tothom");
        System.out.println("---------------------");
    }

    public String getLinea(Scanner sc, String prompt, boolean obligatori) {
        String line;
        do {
            System.out.print(prompt + " ");
            line = sc.nextLine().trim();
        } while (obligatori && line.isEmpty());
        return line;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientXat client = new ClientXat();
        try {
            client.connecta();
            client.start();
            client.ajuda();
            boolean sortir = false;
            while (!sortir) {
                String op = sc.nextLine().trim();
                switch (op) {
                    case "":
                    case "4":
                        client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                        sortir = true;
                        break;
                    case "1":
                        String nom = client.getLinea(sc, "Introdueix el nom:", true);
                        client.enviarMissatge(Missatge.getMissatgeConectar(nom));
                        break;
                    case "2":
                        String dest = client.getLinea(sc, "Destinatari:", true);
                        String msg  = client.getLinea(sc, "Missatge a enviar:", true);
                        client.enviarMissatge(Missatge.getMissatgePersonal(dest, msg));
                        break;
                    case "3":
                        String mg   = client.getLinea(sc, "Missatge grup:", true);
                        client.enviarMissatge(Missatge.getMissatgeGrup(mg));
                        break;
                    case "5":
                        client.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                        sortir = true;
                        break;
                    default:
                        System.out.println("Opció no vàlida");
                }
            }
            client.tancarClient();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Fitxer {
    private String nom;
    private byte[] content;
    public Fitxer(String nom){
        this.nom = nom;
    }
    public byte[] getContingut()throws Exception{
        File file = new File(Client.DIR_ARRIBADA + "/" + nom);
        if (!file.exists()) return null;

        Path path = file.toPath();
        content = Files.readAllBytes(path);
        return content;
    }
    public String getNom() {
        return nom;
    }
    public String getPath(){
        return new File(Client.DIR_ARRIBADA + "/" + nom).getAbsolutePath();
    }
}

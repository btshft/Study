import java.security.MessageDigest;

public class SHA256HashProvider implements IHashProvider {

    public byte[] hash(byte[] data) throws Exception {
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
        return hash.digest(data);
    }

}

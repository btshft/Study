import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hasher {

    public static byte[] calculateHash(String algorithm, String message) throws Exception {
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        return hash.digest(messageBytes);
    }
}

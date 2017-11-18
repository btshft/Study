import javax.crypto.KeyGenerator;

public class EncryptionKeyProvider {
    public static byte[] createKey(String algorithm) throws Exception {
        return KeyGenerator.getInstance(algorithm).generateKey().getEncoded();
    }
}

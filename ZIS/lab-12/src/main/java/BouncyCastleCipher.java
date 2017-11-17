import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class BouncyCastleCipher implements ICipher<byte[], byte[], byte[]> {

    private static final String BcSecurityProvider = "BC";

    private final Cipher _cipher;
    private final String _algorithm;

    public BouncyCastleCipher(String algorithm) throws Exception {
        _algorithm = algorithm;
        _cipher = Cipher.getInstance(algorithm, BcSecurityProvider);
    }

    public byte[] encrypt(byte[] message, byte[] key) throws Exception {
        return operate(message, key, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] message, byte[] key) throws Exception {
        return operate(message, key, Cipher.DECRYPT_MODE);
    }

    private byte[] operate(byte[] message, byte[] key, int operation) throws Exception {
        _cipher.init(operation, getSecretKey(key));
        return _cipher.doFinal(message);
    }

    private SecretKey getSecretKey(byte[] key){
        return new SecretKeySpec(key, _algorithm);
    }
}

package factories;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class BouncyCastleSymmecticKeyFactory implements IKeyFactory {

    private static final SecureRandom random = new SecureRandom();

    private final String algorithm;
    private final int keyLength;

    private Key key;

    public BouncyCastleSymmecticKeyFactory(String algorithm, int keyLength) throws Exception {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        key = generateKey();
    }

    @Override
    public Key createEncryptionKey() {
        return key;
    }

    @Override
    public Key createDecryptionKey() {
        return key;
    }

    private Key generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        keyGenerator.init(keyLength, random);
        return keyGenerator.generateKey();
    }
}

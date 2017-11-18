package factories;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class BouncyCastleAsymmetricKeyFactory implements IKeyFactory, IAsymmetricKeyFactory {

    private static final SecureRandom random = new SecureRandom();

    private final String algorithm;
    private final int keyLength;

    private KeyPair keyPair;

    public BouncyCastleAsymmetricKeyFactory(String algorithm, int keyLength) throws Exception {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        keyPair = generateKeyPair();
    }

    @Override
    public Key createEncryptionKey() {
        return keyPair.getPublic();
    }

    @Override
    public Key createDecryptionKey() {
        return keyPair.getPrivate();
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        keyGenerator.initialize(keyLength, random);
        return keyGenerator.generateKeyPair();
    }

    @Override
    public PublicKey createPublicKey() {
        return keyPair.getPublic();
    }

    @Override
    public PrivateKey createPrivateKey() {
        return keyPair.getPrivate();
    }
}

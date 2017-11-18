import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RsaKeyProvider implements IKeyProvider {

    private KeyPair _currentKeyPair;

    public RsaKeyProvider(int keyLength) throws Exception {
        KeyPairGenerator _keyPairGenerator = KeyPairGenerator
                .getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);

        _keyPairGenerator.initialize(keyLength);
        _currentKeyPair = _keyPairGenerator.generateKeyPair();
    }

    public byte[] getPrivateKey() {
        return _currentKeyPair.getPrivate().getEncoded();
    }

    public byte[] getPublicKey() {
        return _currentKeyPair.getPublic().getEncoded();
    }
}

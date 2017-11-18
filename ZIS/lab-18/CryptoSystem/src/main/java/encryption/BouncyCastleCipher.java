package encryption;

import common.Serializer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.security.Key;

public class BouncyCastleCipher implements ICipher {

    private final String algorithm;

    public BouncyCastleCipher(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public <T extends Serializable> byte[] encrypt(T data, Key key) throws Exception {
        byte[] bytes = Serializer.serialize(data);
        Cipher cipher = Cipher.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(bytes);
    }

    @Override
    public <T extends Serializable> T decrypt(byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] bytes = cipher.doFinal(data);
        return Serializer.deserialize(bytes);
    }
}

package hash;

import common.Serializer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.Serializable;
import java.security.MessageDigest;

public class BouncyCastleHashProvider implements IHashProvider {

    public static final String DefaultHashAlgorithm = "SHA-256";

    private final String hashAlgorithm;

    public BouncyCastleHashProvider() {
        hashAlgorithm = DefaultHashAlgorithm;
    }

    public BouncyCastleHashProvider(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public <T extends Serializable> byte[] hash(T obj) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        return messageDigest.digest(Serializer.serialize(obj));
    }

    @Override
    public byte[] hash(byte[] data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        return messageDigest.digest(data);
    }
}

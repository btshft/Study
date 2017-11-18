import java.math.BigInteger;

public class ElGamalKeyProvider implements IKeyProvider {

    private final BigInteger _privateKey;
    private final BigInteger _publicKey;

    public ElGamalKeyProvider(ElGamalSignatureAlgorithm algorithm){
        _privateKey = algorithm.createPrivateKey();
        _publicKey = algorithm.createPublicKey(_privateKey);
    }

    public byte[] getPrivateKey() {
        return _privateKey.toByteArray();
    }

    public byte[] getPublicKey() {
        return _publicKey.toByteArray();
    }
}

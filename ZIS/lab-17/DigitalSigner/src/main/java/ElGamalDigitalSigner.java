import javafx.util.Pair;
import org.apache.commons.lang3.SerializationUtils;

import java.math.BigInteger;

public class ElGamalDigitalSigner implements IDigitalSigner {

    private final ElGamalSignatureAlgorithm _algorithm;
    private final IHashProvider _hashProvider;

    public ElGamalDigitalSigner(ElGamalSignatureAlgorithm algorithm, IHashProvider hashProvider){
        _algorithm = algorithm;
        _hashProvider = hashProvider;
    }

    public byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        BigInteger biPk = new BigInteger(privateKey);
        BigInteger biMsg = new BigInteger(_hashProvider.hash(data));
        Pair<BigInteger, BigInteger> signature = _algorithm.sign(biMsg, biPk);
        return SerializationUtils.serialize(signature);
    }

    public boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws Exception {
        Pair<BigInteger, BigInteger> pSing = SerializationUtils.deserialize(signature);
        BigInteger biPk = new BigInteger(publicKey);
        BigInteger biMsg = new BigInteger(_hashProvider.hash(data));
        return _algorithm.verify(biMsg, biPk, pSing);
    }
}

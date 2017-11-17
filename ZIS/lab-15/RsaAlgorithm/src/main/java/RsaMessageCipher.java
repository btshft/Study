import java.math.BigInteger;

public class RsaMessageCipher implements IMessageCipher{

    private final RsaAlgorithm _rsaAlgorithm;

    public RsaMessageCipher() {
        _rsaAlgorithm = new RsaAlgorithm();
    }

    public RsaMessageCipher(int keyLength){
        _rsaAlgorithm = new RsaAlgorithm(keyLength);
    }

    public byte[] encrypt(String data) {
        byte[] raw = data.getBytes();
        BigInteger biData = new BigInteger(raw);
        return _rsaAlgorithm.encrypt(biData).toByteArray();
    }

    public String decrypt(byte[] encryptedData) {
        BigInteger encrypted = new BigInteger(encryptedData);
        return new String(_rsaAlgorithm.decrypt(encrypted).toByteArray());
    }

    public byte[] getPrivateKey() {
        return _rsaAlgorithm.getPrivateKey().toByteArray();
    }

    public byte[] getPublicKey() {
        return _rsaAlgorithm.getPublicKey().toByteArray();
    }
}

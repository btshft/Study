import java.nio.charset.Charset;

public class StringCipher implements ICipher<String, byte[], byte[]> {

    private final ICipher<byte[], byte[], byte[]> _baseCipher;

    public StringCipher(ICipher<byte[], byte[], byte[]> baseCipher) {
        _baseCipher = baseCipher;
    }

    public byte[] encrypt(String message, byte[] key) throws Exception {
        byte[] bMessage = encode_utf8(message);
        return _baseCipher.encrypt(bMessage, key);
    }

    public String decrypt(byte[] message, byte[] key) throws Exception {
        return decode_utf8(_baseCipher.decrypt(message, key));
    }

    private byte[] encode_utf8(String str) throws Exception {
        return str.getBytes("UTF-8");
    }

    private String decode_utf8(byte[] data) throws Exception {
        return new String(data, "UTF-8");
    }
}

public class StringSigner implements IContentSigner<String> {

    private IKeyProvider _keyProvider;
    private IDigitalSigner _digitalSigner;

    public StringSigner(IKeyProvider keyProvider, IDigitalSigner digitalSigner) {
        _keyProvider = keyProvider;
        _digitalSigner = digitalSigner;
    }

    public byte[] sign(String s) throws Exception {
        byte[] content = s.getBytes("UTF-8");
        return _digitalSigner.sign(content, _keyProvider.getPrivateKey());
    }

    public boolean verify(String s, byte[] signature) throws Exception {
        byte[] content = s.getBytes("UTF-8");
        return _digitalSigner.verify(content, signature, _keyProvider.getPublicKey());
    }

}

import java.io.File;
import java.nio.file.Files;

public class FileSigner implements IContentSigner<File> {

    private final IDigitalSigner _digitalSinger;
    private final IKeyProvider _keyProvider;

    public FileSigner(IDigitalSigner digitalSinger, IKeyProvider keyProvider) {
        _digitalSinger = digitalSinger;
        _keyProvider = keyProvider;
    }

    public byte[] sign(File file) throws Exception {
        byte[] data = Files.readAllBytes(file.toPath());
        return _digitalSinger.sign(data, _keyProvider.getPrivateKey());
    }

    public boolean verify(File file, byte[] signature) throws Exception {
        byte[] data = Files.readAllBytes(file.toPath());
        return _digitalSinger.verify(data, signature, _keyProvider.getPublicKey());
    }
}

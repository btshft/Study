import java.nio.file.Files;
import java.nio.file.Paths;

@FunctionalInterface
interface TFunction<T, U, R> {
    R apply(T t, U u) throws Exception;
}

public class FileCipher {

    private final ICipher<byte[], byte[], byte[]> _baseCipher;

    public FileCipher(ICipher<byte[], byte[], byte[]> baseCipher) {
        _baseCipher = baseCipher;
    }

    public void encrypt(String inFilePath, String outFileName, byte[] key) throws Exception {
        operate(inFilePath, outFileName, key, _baseCipher::encrypt);
    }

    public void decrypt(String inFilePath, String outFileName, byte[] key) throws Exception {
        operate(inFilePath, outFileName, key, _baseCipher::decrypt);
    }

    private void operate(String inFilePath, String outFileName, byte[] key,
                         TFunction<byte[], byte[], byte[]> operation) throws Exception
    {
        byte[] content = Files.readAllBytes(Paths.get(inFilePath));
        byte[] changedContent = operation.apply(content, key);

        if (!Files.exists(Paths.get(outFileName))){
            Files.createFile(Paths.get(outFileName));
        }

        Files.write(Paths.get(outFileName), changedContent);
    }
}

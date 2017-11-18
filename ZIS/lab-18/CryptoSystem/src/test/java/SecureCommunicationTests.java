import communication.IPacketDecryptor;
import communication.IPacketEncryptor;
import communication.PacketDecryptor;
import communication.PacketEncryptor;
import encryption.BouncyCastleCipher;
import encryption.BouncyCastleDigitalSigner;
import encryption.ICipher;
import encryption.IDigitalSigner;
import factories.BouncyCastleAsymmetricKeyFactory;
import factories.BouncyCastleSymmecticKeyFactory;
import factories.IAsymmetricKeyFactory;
import factories.IKeyFactory;
import hash.BouncyCastleHashProvider;
import hash.IHashProvider;
import javafx.util.Pair;
import logging.IArgumentTransformer;
import logging.ILogger;
import logging.PrintStreamLogger;
import models.DecryptedPacket;
import models.EncryptedPacket;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.Security;
import java.util.ArrayList;

public class SecureCommunicationTests {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String DataCipherAlgorithm = "Blowfish";
    public static final String KeyCipherAlgorithm = "RSA";
    public static final String SignatureHashAlgorithm = "GOST3411";
    public static final String SignatureAlghorithm = "RSA";

    public static final int DataCipherKeyLength = 512;
    public static final int KeyCipherKeyLength = 2048;
    public static final int SignatureKeyLegnth = 1024;

    @Test
    public void Test_SecureCommunication() throws Exception {

        // Arrange
        ILogger logger = createLogger();
        IDigitalSigner digitalSigner = createDigitalSigner(SignatureAlghorithm, SignatureHashAlgorithm);

        ICipher dataCipher = createDataCipher(DataCipherAlgorithm);
        ICipher keyCipher = createKeyCipher(KeyCipherAlgorithm);

        IAsymmetricKeyFactory digitalSignerKeyFactory = createDigitalSignerKeyFactory(SignatureAlghorithm, SignatureKeyLegnth);
        IKeyFactory dataCipherKeyFactory = createDataCipherKeyFactory(DataCipherAlgorithm, DataCipherKeyLength);
        IKeyFactory keyCipherKeyFactory = createKeyCipherKeyFactory(KeyCipherAlgorithm, KeyCipherKeyLength);

        IPacketEncryptor packetEncryptor = new PacketEncryptor(
                dataCipher,
                keyCipher,
                digitalSigner,
                dataCipherKeyFactory,
                keyCipherKeyFactory,
                digitalSignerKeyFactory,
                logger);

        IPacketDecryptor packetDecryptor = new PacketDecryptor(
                dataCipher,
                keyCipher,
                digitalSigner,
                keyCipherKeyFactory,
                digitalSignerKeyFactory,
                logger);

        String message = "Hello world";

        logger.logInfo("\n\nТест защищенного взаимодействия внутри криптосистемы\n\n" +
                "Алгоритм подписи: %s (%s bit)\n" +
                "Алгоритм хеширования данных: %s\n" +
                "Алгоритм шифрования данных: %s (%s bit)\n" +
                "Алгоритм шифрования ключа чтения данных: %s (%s bit)",
                SignatureAlghorithm, SignatureKeyLegnth,
                SignatureHashAlgorithm,
                DataCipherAlgorithm, DataCipherKeyLength,
                KeyCipherAlgorithm, KeyCipherKeyLength);

        // Act
        logger.logInfo("\n\n[Шифрование данных]");
        EncryptedPacket encryptedPacket = packetEncryptor.encrypt(message);

        logger.logInfo("\n\n[Дешифрование данных]");
        DecryptedPacket<String> decryptedPacket = packetDecryptor.decrypt(encryptedPacket);

        // Assert
        Assert.assertTrue(true);

    }

    private ILogger createLogger() {

        ArrayList<Pair<Class<?>, IArgumentTransformer>> transformers = new ArrayList<>();

        transformers.add(new Pair<>(byte[].class, argument -> "0x" + DatatypeConverter.printHexBinary((byte[])argument)));
        transformers.add(new Pair<>(Key.class, (argument) -> "0x" + DatatypeConverter.printHexBinary(Key.class.cast(argument).getEncoded())));
        transformers.add(new Pair<>(boolean.class, argument -> ((boolean)argument) ? "Да" : "Нет"));

        return new PrintStreamLogger(System.out, transformers);
    }

    private IAsymmetricKeyFactory createDigitalSignerKeyFactory(String algorithm, int keyLength) throws Exception {
        return new BouncyCastleAsymmetricKeyFactory(algorithm, keyLength);
    }

    private IKeyFactory createDataCipherKeyFactory(String algorithm, int keyLength) throws Exception {
        return new BouncyCastleSymmecticKeyFactory(algorithm, keyLength);
    }

    private IKeyFactory createKeyCipherKeyFactory(String algorithm, int keyLength) throws Exception {
        return new BouncyCastleAsymmetricKeyFactory(algorithm, keyLength);
    }

    private ICipher createDataCipher(String algorithm){
        return new BouncyCastleCipher(algorithm);
    }

    private ICipher createKeyCipher(String algorithm){
        return new BouncyCastleCipher(algorithm);
    }

    private IDigitalSigner createDigitalSigner(String algorithm, String hashAlgorithm){
        return new BouncyCastleDigitalSigner(algorithm, createHashProvider(hashAlgorithm));
    }

    private IHashProvider createHashProvider(String algorithm){
        return new BouncyCastleHashProvider(algorithm);
    }


}

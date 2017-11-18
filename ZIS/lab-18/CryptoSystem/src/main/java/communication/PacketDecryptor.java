package communication;

import encryption.ICipher;
import encryption.IDigitalSigner;
import factories.IAsymmetricKeyFactory;
import factories.IKeyFactory;
import logging.ILogger;
import models.DecryptedPacket;
import models.EncryptedPacket;
import models.SignedContainer;

import java.io.Serializable;
import java.security.Key;

public class PacketDecryptor implements IPacketDecryptor {

    private final ICipher dataCipher;
    private final ICipher keyCipher;

    private final IDigitalSigner digitalSigner;

    private final IKeyFactory keyCipherKeyFactory;
    private final IAsymmetricKeyFactory digitalSignerKeyFactory;

    private final ILogger logger;

    public PacketDecryptor(
            ICipher dataCipher,
            ICipher keyCipher,
            IDigitalSigner digitalSigner,
            IKeyFactory keyCipherKeyFactory,
            IAsymmetricKeyFactory digitalSignerKeyFactory,
            ILogger logger)
    {
        this.dataCipher = dataCipher;
        this.keyCipher = keyCipher;
        this.digitalSigner = digitalSigner;
        this.keyCipherKeyFactory = keyCipherKeyFactory;
        this.digitalSignerKeyFactory = digitalSignerKeyFactory;
        this.logger = logger;
    }


    @Override
    public <T extends Serializable> DecryptedPacket<T> decrypt(EncryptedPacket packet) throws Exception {
        // Распаковываем ключ для распаковки контейнера
        Key decryptedDataEncryptionKey = keyCipher.decrypt(packet.getEncryptedKey(), keyCipherKeyFactory.createDecryptionKey());

        logger.logInfo("\nРасшифрованный ключ: %s", decryptedDataEncryptionKey);

        // Распаковываем контейнер с данными
        SignedContainer<T> signedContainer = dataCipher.decrypt(packet.getEncryptedContainer(), decryptedDataEncryptionKey);

        logger.logInfo("Расшифрованные данные: %s\nРасшифрованная подпись: %s",
                signedContainer.getData(),
                signedContainer.getSignature());

        // Проверяем подпись
        boolean isSignatureValid = digitalSigner.verify(signedContainer, digitalSignerKeyFactory.createPublicKey());

        logger.logInfo("Подпись данных верна: %s", isSignatureValid);

        return new DecryptedPacket<>(decryptedDataEncryptionKey, signedContainer);
    }
}

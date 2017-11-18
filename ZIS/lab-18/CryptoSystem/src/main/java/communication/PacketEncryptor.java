package communication;

import encryption.ICipher;
import encryption.IDigitalSigner;
import factories.IAsymmetricKeyFactory;
import factories.IKeyFactory;
import logging.ILogger;
import models.EncryptedPacket;
import models.SignedContainer;

import java.io.Serializable;
import java.security.Key;
import java.security.PrivateKey;

public class PacketEncryptor implements IPacketEncryptor{

    private final ICipher dataCipher;
    private final ICipher keyCipher;

    private final IDigitalSigner digitalSigner;

    private final IKeyFactory dataCipherKeyFactory;
    private final IKeyFactory keyCipherKeyFactory;
    private final IAsymmetricKeyFactory digitalSignerKeyFactory;

    private final ILogger logger;

    public PacketEncryptor(ICipher dataCipher,
                           ICipher keyCipher,
                           IDigitalSigner digitalSigner,
                           IKeyFactory dataCipherKeyFactory,
                           IKeyFactory keyCipherKeyFactory,
                           IAsymmetricKeyFactory digitalSignerKeyFactory,
                           ILogger logger)
    {
        this.dataCipher = dataCipher;
        this.keyCipher = keyCipher;
        this.digitalSigner = digitalSigner;
        this.dataCipherKeyFactory = dataCipherKeyFactory;
        this.keyCipherKeyFactory = keyCipherKeyFactory;
        this.digitalSignerKeyFactory = digitalSignerKeyFactory;
        this.logger = logger;
    }

    @Override
    public <T extends Serializable> EncryptedPacket encrypt(T data) throws Exception {

        logger.logInfo("\nИсходное сообщение: %s", data);

        // Ключи
        PrivateKey digitalSignPrivateKey = digitalSignerKeyFactory.createPrivateKey();
        Key dataEncryptionKey = dataCipherKeyFactory.createEncryptionKey();
        Key keyEncryptionKey = keyCipherKeyFactory.createEncryptionKey();

        logger.logInfo("\nКлюч ЭЦП: %s\nКлюч шифрования даннных: %s\nКлюч шифрования ключа чтения данных: %s",
                digitalSignPrivateKey, dataEncryptionKey, keyEncryptionKey);

        // Подписываем оригинальные данные и сохраняем в контейнер вместе с подписью
        SignedContainer<T> signedDataContainer = digitalSigner.sign(data, digitalSignPrivateKey);

        logger.logInfo("Подпись: %s", signedDataContainer.getSignature());

        // Шифруем контейнер вместе с подписью
        byte[] encryptedSignedDataContainer = dataCipher.encrypt(signedDataContainer, dataEncryptionKey);

        logger.logInfo("Зашифрованный контейнер (сообщение + подпись): %s", encryptedSignedDataContainer);

        // Шифруем ключ для распаковки контейнера с данными.
        byte[] encryptedDataEncryptionKey = keyCipher.encrypt(dataEncryptionKey, keyEncryptionKey);

        logger.logInfo("Зашифрованный ключ: %s", encryptedDataEncryptionKey);

        return new EncryptedPacket(encryptedSignedDataContainer, encryptedDataEncryptionKey);
    }
}

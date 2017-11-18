package communication;

import models.DecryptedPacket;
import models.EncryptedPacket;

import java.io.Serializable;

/**
 * Компонент для дешифрования пакетов.
 */
public interface IPacketDecryptor {
    /**
     * Расшифровывает пакет.
     * @param packet - зашифрованный пакет.
     * @param <T> - тип данных.
     * @return расшифрованный пакет.
     */
    <T extends Serializable> DecryptedPacket<T> decrypt(EncryptedPacket packet) throws Exception;
}

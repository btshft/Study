package communication;

import models.EncryptedPacket;

import java.io.Serializable;

/**
 * Компонент для шифрования пакетов.
 */
public interface IPacketEncryptor {
    /**
     * Зашифровыаает данные в пакет.
     * @param data - данные.
     * @param <T> - тип данных.
     * @return зашифрованный пакет.
     */
    <T extends Serializable> EncryptedPacket encrypt(T data) throws Exception;
}

package models;

import java.io.Serializable;
import java.security.Key;

/**
 * Модель расшифрованного информационного пакета.
 * @param <T> - тип данных.
 */
public class DecryptedPacket<T extends Serializable> {

    private final Key decryptedKey;
    private final SignedContainer<T> decryptedContainer;

    public DecryptedPacket(Key decryptedKey, SignedContainer<T> decryptedContainer) {
        this.decryptedKey = decryptedKey;
        this.decryptedContainer = decryptedContainer;
    }

    public Key getDecryptedKey() {
        return decryptedKey;
    }

    public SignedContainer<T> getDecryptedContainer() {
        return decryptedContainer;
    }
}

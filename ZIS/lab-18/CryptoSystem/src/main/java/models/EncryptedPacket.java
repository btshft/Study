package models;

/**
 * Модель зашифрованного информационного пакета.
 */
public class EncryptedPacket {

    private final byte[] encryptedContainer;
    private final byte[] encryptedKey;

    public EncryptedPacket(byte[] encryptedContainer, byte[] encryptedKey){
        this.encryptedContainer = encryptedContainer;
        this.encryptedKey = encryptedKey;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public byte[] getEncryptedContainer() {
        return encryptedContainer;
    }
}

/**
 * Интерфейс компонента для шифрации сообщений.
 */
public interface IMessageCipher {
    byte[] encrypt(String data);
    String decrypt(byte[] encryptedData);

    byte[] getPrivateKey();
    byte[] getPublicKey();
}

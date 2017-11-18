import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Интерфейс компонента для создания и верификации ЭЦП.
 */
public interface IDigitalSigner {

    /**
     * Подписывает данные.
     * @param data - данные.
     * @param privateKey - приватный ключ.
     * @return - подпись.
     */
    byte[] sign(byte[] data, byte[] privateKey) throws Exception;

    /**
     * Проверяет подпись данных.
     * @param data - данные.
     * @param signature - подпись.
     * @param publicKey - публичный ключ.
     * @return - признак корректности данных.
     */
    boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws Exception;

}

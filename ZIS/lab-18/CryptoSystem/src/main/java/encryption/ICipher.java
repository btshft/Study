package encryption;

import java.io.Serializable;
import java.security.Key;

/**
 * Интерфейс компонента для шифрования данных.
 */
public interface ICipher {

    /**
     * Зашифровывает данные.
     * @param data - данные.
     * @param key - ключ.
     * @param <T> - тип данных.
     * @return зашифрованные данные.
     */
    <T extends Serializable> byte[] encrypt(T data, Key key) throws Exception;

    /**
     * Расшифровывает данные.
     * @param data - данные.
     * @param key - ключ.
     * @param <T> - тип данных.
     * @return - расшифрованные данные.
     */
    <T extends Serializable> T decrypt(byte[] data, Key key) throws Exception;
}

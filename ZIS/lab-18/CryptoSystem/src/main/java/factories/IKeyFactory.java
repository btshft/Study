package factories;

import java.security.Key;

/**
 * Фабрика для получения ключей.
 */
public interface IKeyFactory {

    /**
     * Создает ключ шифрования данных.
     */
    Key createEncryptionKey();

    /**
     * Создает ключ дешифрвоания данных.
     */
    Key createDecryptionKey();
}

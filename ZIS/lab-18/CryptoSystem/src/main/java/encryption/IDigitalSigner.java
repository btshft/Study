package encryption;

import models.SignedContainer;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Интерфейс компонента ЭЦП.
 */
public interface IDigitalSigner {

    /**
     * Подписывает данные и упаковывает подпись + данные в контейнер.
     * @param data - данные.
     * @param privateKey - приватный ключ.
     * @param <T> - тип данных.
     */
    <T extends Serializable> SignedContainer<T> sign(T data, PrivateKey privateKey) throws Exception;

    /**
     * Выполняет проверку подписи.
     * @param container - контейнер.
     * @param publicKey - публичный ключ.
     * @param <T> - тип данных.
     * @return признак корректности подписи.
     */
    <T extends Serializable> boolean verify(SignedContainer<T> container, PublicKey publicKey) throws Exception;
}

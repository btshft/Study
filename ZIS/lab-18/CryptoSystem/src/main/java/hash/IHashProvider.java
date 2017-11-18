package hash;

import java.io.Serializable;

/**
 * Интерфейс провайдера хеша.
 */
public interface IHashProvider {
    /**
     * Считает хеш для объекта.
     * @param obj - объект.
     * @param <T> - тип объекта.
     * @return хеш.
     */
    <T extends Serializable> byte[] hash(T obj) throws Exception;

    /**
     * Считает хеш от массива данных.
     * @param data - данные.
     */
    byte[] hash(byte[] data) throws Exception;
}

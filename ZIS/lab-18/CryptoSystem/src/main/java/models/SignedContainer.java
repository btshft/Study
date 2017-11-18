package models;

import java.io.Serializable;

/**
 * Контейнер для хранения пары (данные + подпись)
 * @param <T> - тип данных.
 */
public class SignedContainer<T extends Serializable> implements Serializable {
    private final T data;
    private final byte[] signature;

    public SignedContainer(T data, byte[] signature){
        this.data = data;
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public T getData() {
        return data;
    }
}

/**
 * Интерфейс алгоритма шифрации.
 */
public interface ICipher<TData, TEncrypted, TKey> {
    TEncrypted encrypt(TData message, TKey key) throws Exception;
    TData decrypt(TEncrypted message, TKey key) throws Exception;
}

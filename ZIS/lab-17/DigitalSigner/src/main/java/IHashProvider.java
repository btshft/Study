/**
 * Провайдер хеша.
 */
public interface IHashProvider {
    /**
     * Вычисляет хеш.
     * @param data - данные.
     * @return хеш.
     */
    byte[] hash(byte[] data) throws Exception;
}

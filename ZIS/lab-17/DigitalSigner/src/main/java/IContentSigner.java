/**
 * Верхнеуровневый интерфейс для подписи произвольных объектов.
 * @param <TContent> - тип объекта.
 */
public interface IContentSigner<TContent> {

    /**
     * Подписывает объект.
     * @param content - объект для подписи.
     * @return - подпись.
     */
    byte[] sign(TContent content) throws Exception;

    /**
     * Валидирует подпись объекта.
     * @param content - объект.
     * @param signature - подпись.
     * @return - признак соответствия подписи контенту.
     */
    boolean verify(TContent content, byte[] signature) throws Exception;
}

package logging;

/**
 * Интерфейс логера чтоб был.
 */
public interface ILogger {
    void logInfo(String pattern, Object... arguments);
}

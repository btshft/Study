package logging;

@FunctionalInterface
public interface IArgumentTransformer {
    String apply(Object argument);
}

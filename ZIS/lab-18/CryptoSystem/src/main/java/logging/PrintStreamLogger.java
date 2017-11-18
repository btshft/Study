package logging;

import javafx.util.Pair;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class PrintStreamLogger implements ILogger {

    private final PrintStream stream;
    private final ArrayList<Pair<Class<?>, IArgumentTransformer>> transformers;

    public PrintStreamLogger(PrintStream stream) {
        this.stream = stream;
        this.transformers = new ArrayList<>();
    }

    public PrintStreamLogger(PrintStream stream, ArrayList<Pair<Class<?>,IArgumentTransformer >> transformers){
        this.stream = stream;
        this.transformers = transformers;
    }

    @Override
    public void logInfo(String pattern, Object... arguments) {
        Object[] transformedArguments = Arrays.stream(arguments).map(o -> {

            Optional<IArgumentTransformer> trs = transformers.stream()
                    .filter(t -> t.getKey().isAssignableFrom(o.getClass()))
                    .map(Pair::getValue)
                    .findFirst();

            if (trs.isPresent())
                return trs.get().apply(o);

            return o;

        }).toArray();

        stream.println(String.format(pattern, transformedArguments));
    }
}

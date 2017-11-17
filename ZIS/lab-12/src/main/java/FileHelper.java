import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileHelper {

    public static void writeFile(String path, String content) throws Exception {
        Path fPath = Paths.get(path);
        if (Files.exists(fPath))
            Files.delete(fPath);

        Files.createFile(fPath);

        try(BufferedWriter writer = Files.newBufferedWriter(fPath, StandardCharsets.UTF_8)){
            writer.write(content);
            writer.close();
        }
    }

    public static String readFile(String path) throws Exception {
        return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.joining());
    }

    public static byte[] readFileBytes(String path) throws Exception {
        return Files.readAllBytes(Paths.get(path));
    }

}

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.stream.Collectors;

public class HasherTests {

    public static final String[] Algorithms = {
            "GOST3411",
            "MD5",
            "SHA-224", "SHA-256", "SHA-512", "SHA-384",
            "RIPEMD128", "RIPEMD160", "RIPEMD320", "RIPEMD256" };

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void Test_ThreeEqualMessages_Hash() throws Exception {

        printHeader("Одинаковые сообщения\n");

        // Arrange
        String[] messages = { "Hello World", "Hello World", "Hello World" };

        // Act
        IterateHash(messages, true);

        // Assert (lol)
        Assert.assertTrue(true);
    }

    @Test
    public void Test_ThreeDiffOneSymbolMessages_Hash() throws Exception {

        printHeader("Разные сообщения (отличие 1 буква)\n");

        // Arrange
        String[] messages = { "Hello Worlk", "Hello Worlq", "Hello Worln" };

        // Act
        IterateHash(messages, true);

        // Assert (lol)
        Assert.assertTrue(true);
    }

    @Test
    public void Test_800kb_Message() throws Exception {
        printHeader("Сообщение 800кб\n");

        // Arrange
        String[] messages = { readFile("files/800kb.txt") };

        // Act
        IterateHash(messages, false);

        // Assert (lol)
        Assert.assertTrue(true);
    }

    @Test
    public void Test_2400kb_Message() throws Exception {
        printHeader("Сообщение 2400кб\n");

        // Arrange
        String[] messages = { readFile("files/2400kb.txt") };

        // Act
        IterateHash(messages, false);

        // Assert (lol)
        Assert.assertTrue(true);
    }

    @Test
    public void Test_12000kb_Message() throws Exception {
        printHeader("Сообщение 12000кб\n");

        // Arrange
        String[] messages = { readFile("files/12000kb.txt") };

        // Act
        IterateHash(messages, false);

        // Assert (lol)
        Assert.assertTrue(true);
    }

    private void IterateHash(String[] messages, boolean printMessage) throws Exception {
        for (String algorithm : Algorithms) {

            print("\nАлгоритм: %s\n", algorithm);

            for (String message : messages) {
                String hash = byte2hex(Hasher.calculateHash(algorithm, message));

                if (printMessage) {
                    print("Сообщение: %s", message);
                }

                print("Хеш: %s\n", hash);
            }

            print("---------------------------------------------------");
        }
    }

    private static void printHeader(String text) {
        print("\n\n[ТЕСТ] Хеширование: %s", text);
    }

    private static String byte2hex(byte[] data){
        return "0x" + DatatypeConverter.printHexBinary(data);
    }

    private static void print(String message, Object... arguments) {
        System.out.println(String.format(message, arguments));
    }

    private static String readFile(String path) throws Exception {
        return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
                .stream()
                .collect(Collectors.joining());
    }
}

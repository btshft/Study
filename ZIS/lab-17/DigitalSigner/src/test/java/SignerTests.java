import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.Security;


public class SignerTests {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void Test_RSA_StringSigner_Sign_Correct() throws Exception {

        print("\nТест подписи текстового сообщения по алгоритму RSA\n");

        // Arrange
        String text = "Hello world";
        IContentSigner<String> stringSigner = new StringSigner(
                new RsaKeyProvider(1024),
                new RsaDigitalSigner(new SHA256HashProvider()));

        // Act
        byte[] signature = stringSigner.sign(text);
        boolean isSignatureCorrect = stringSigner.verify(text, signature);

        print("Сообщение: %s\nПодпись: %s\nПодпись верна: %s\n", text, byte2hex(signature), (isSignatureCorrect ? "Да" : "Нет"));

        // Assert
        Assert.assertTrue(isSignatureCorrect);
    }

    @Test
    public void Test_ElGamal_StringSigner_Sign_Correct() throws Exception {

        print("\nТест подписи текстового сообщения по алгоритму Эль-Гамаля\n");

        // Arrange
        String text = "Hello world";
        ElGamalSignatureAlgorithm algorithm = new ElGamalSignatureAlgorithm(512);
        IContentSigner<String> stringSigner = new StringSigner(
                new ElGamalKeyProvider(algorithm),
                new ElGamalDigitalSigner(algorithm, new SHA256HashProvider()));

        // Act
        byte[] signature = stringSigner.sign(text);
        boolean isSignatureCorrect = stringSigner.verify(text, signature);

        print("Сообщение: %s\nПодпись: %s\nПодпись верна: %s\n", text, byte2hex(signature), (isSignatureCorrect ? "Да" : "Нет"));

        // Assert
        Assert.assertTrue(isSignatureCorrect);
    }

    @Test
    public void Test_RSA_FileSigner_Sign_Correct() throws Exception {

        print("\nТест подписи файла по алгоритму RSA\n");

        // Arrange
        File file = new File("files/example.xml");
        IContentSigner<File> fileSigner = new FileSigner(
                new RsaDigitalSigner(new SHA256HashProvider()),
                new RsaKeyProvider(1024));

        // Act
        byte[] signature = fileSigner.sign(file);
        boolean isSignatureCorrect = fileSigner.verify(file, signature);

        print("Подпись: %s\nПодпись верна: %s\n", byte2hex(signature), (isSignatureCorrect ? "Да" : "Нет"));

        // Assert
        Assert.assertTrue(isSignatureCorrect);
    }

    @Test
    public void Test_ElGamal_FileSigner_Sign_Correct() throws Exception {

        print("\nТест подписи файла по алгоритму Эль-Гамаля\n");

        // Arrange
        File file = new File("files/example.xml");
        ElGamalSignatureAlgorithm algorithm = new ElGamalSignatureAlgorithm(512);
        IContentSigner<File> stringSigner = new FileSigner(
                new ElGamalDigitalSigner(algorithm, new SHA256HashProvider()),
                new ElGamalKeyProvider(algorithm));

        // Act
        byte[] signature = stringSigner.sign(file);
        boolean isSignatureCorrect = stringSigner.verify(file, signature);

        print("Подпись: %s\nПодпись верна: %s\n", byte2hex(signature), (isSignatureCorrect ? "Да" : "Нет"));

        // Assert
        Assert.assertTrue(isSignatureCorrect);
    }

    private static String byte2hex(byte[] data){
        return "0x" + DatatypeConverter.printHexBinary(data);
    }

    private static void print(String message, Object... arguments) {
        System.out.println(String.format(message, arguments));
    }
}

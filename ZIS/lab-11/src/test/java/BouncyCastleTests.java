import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.Security;

/**
 * Набор тестов по 12й лабе ЗИС.
 */
public class BouncyCastleTests {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String[] Algorithms = { "GOST-28147", "AES", "DES", "BLOWFISH"};

    @Test
    public void Test_Bc_File_Cipher() throws Exception {
        for (String algorithm : Algorithms){

            // Arrange
            ICipher<byte[], byte[], byte[]> bcCipher = new BouncyCastleCipher(algorithm);
            FileCipher fileCipher = new FileCipher(bcCipher);
            byte[] key = EncryptionKeyProvider.createKey(algorithm);

            String sourceFileContent = "Невероятно интересная история";

            String sourceFileName = String.format("temp/source_%s.txt", algorithm);
            String encryptedFileName = String.format("temp/encrypted_%s.txt", algorithm);
            String decryptedFileName = String.format("temp/decrypted_%s.txt", algorithm);


            FileHelper.writeFile(sourceFileName, sourceFileContent);

            // Act
            fileCipher.encrypt(sourceFileName, encryptedFileName, key);
            fileCipher.decrypt(encryptedFileName, decryptedFileName, key);

            // Assert
            String decryptedFileContent = FileHelper.readFile(decryptedFileName);

            Assert.assertEquals(sourceFileContent, decryptedFileContent);

            System.out.println(
                    String.format(
                            "Алгоритм: %s \nКонтент: %s \nКлюч: %s\nЗашифрованный контент: %s \nРасшифрованный контент: %s\n\n",
                            algorithm,
                            sourceFileContent,
                            byte2hex(key),
                            byte2hex(FileHelper.readFileBytes(encryptedFileName)),
                            decryptedFileContent
                    )
            );
        }
    }

    @Test
    public void Test_Bc_String_Cipher() throws Exception {

        for (String algorithm : Algorithms) {

            // Arrange
            ICipher<byte[], byte[], byte[]> bcCipher = new BouncyCastleCipher(algorithm);
            ICipher<String, byte[], byte[]> stringCipher = new StringCipher(bcCipher);

            String sourceString = "Привет Мир!";
            byte[] key = EncryptionKeyProvider.createKey(algorithm);

            // Act
            byte[] encrypted = stringCipher.encrypt(sourceString, key);
            String decryptedString = stringCipher.decrypt(encrypted, key);

            // Assert

            Assert.assertEquals(sourceString, decryptedString);

            System.out.println(
                    String.format(
                            "Алгоритм: %s \nСообщение: %s \nКлюч: %s\nЗашифрованное сообщение: \nРасшифрованное сообщение: %s\n\n",
                            algorithm,
                            sourceString,
                            byte2hex(key),
                            byte2hex(encrypted),
                            decryptedString
                    )
            );
        }
    }

    private String byte2hex(byte[] data){
        return "0x" + DatatypeConverter.printHexBinary(data);
    }
}

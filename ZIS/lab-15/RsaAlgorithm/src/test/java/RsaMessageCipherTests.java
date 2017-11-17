import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

public class RsaMessageCipherTests {

    @Test
    public void Test_Rsa_1024bit_Encrypt_Decrypt(){
        // Arrange
        IMessageCipher messageCipher = new RsaMessageCipher(512);
        String sourceMessage = "Hello world!";

        // Act

        byte[] encryptedMessage = messageCipher.encrypt(sourceMessage);
        String decryptedMessage = messageCipher.decrypt(encryptedMessage);

        // Assert
        Assert.assertEquals(sourceMessage, decryptedMessage);

        System.out.println(
                String.format(
                        "Сообщение: %s\nДлина ключа: %s\nПубличный ключ: %s\nПриватный ключ: %s\nЗашифрованное сообщение: %s\nРасшифрованное сообщение: %s\n\n",
                        sourceMessage,
                        512,
                        byte2hex(messageCipher.getPublicKey()),
                        byte2hex(messageCipher.getPrivateKey()),
                        byte2hex(encryptedMessage),
                        decryptedMessage
                )
        );
    }

    @Test
    public void Test_Rsa_2048_Encrypt_Decrypt(){
        // Arrange
        IMessageCipher messageCipher = new RsaMessageCipher(2048);
        String sourceMessage = "Hello world!";

        // Act

        byte[] encryptedMessage = messageCipher.encrypt(sourceMessage);
        String decryptedMessage = messageCipher.decrypt(encryptedMessage);

        // Assert
        Assert.assertEquals(sourceMessage, decryptedMessage);

        System.out.println(
                String.format(
                        "Сообщение: %s\nДлина ключа: %s\nПубличный ключ: %s\nПриватный ключ: %s\nЗашифрованное сообщение: %s\nРасшифрованное сообщение: %s\n\n",
                        sourceMessage,
                        2048,
                        byte2hex(messageCipher.getPublicKey()),
                        byte2hex(messageCipher.getPrivateKey()),
                        byte2hex(encryptedMessage),
                        decryptedMessage
                )
        );
    }

    private String byte2hex(byte[] data){
        return "0x" + DatatypeConverter.printHexBinary(data);
    }
}

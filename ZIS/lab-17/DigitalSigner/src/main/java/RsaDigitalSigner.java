import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Реализация ЭЦП на основе RSA.
 */
public class RsaDigitalSigner implements IDigitalSigner {

    private final IHashProvider _hashProvider;

    public RsaDigitalSigner(IHashProvider hashProvider) {
        _hashProvider = hashProvider;
    }

    public byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, decodePrivateKey(privateKey));

        // Генерация хеша.
        byte[] hash = _hashProvider.hash(data);

        // Отправитель "подписывает" хеш своим приватным ключом.
        byte[] signature = cipher.doFinal(hash);
        return signature;
    }

    public boolean verify(byte[] data, byte[] signature, byte[] publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, decodePublicKey(publicKey));

        // Получатель считает хеш от исходного чтобы
        byte[] hash = _hashProvider.hash(data);
        // И расшифровывает подпись своим публичным ключом.
        byte[] expectedHash = cipher.doFinal(signature);
        // Если хеши совпали, значит контент документа достоверный.
        return MessageDigest.isEqual(hash, expectedHash);
    }

    private PrivateKey decodePrivateKey(byte[] key) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(new PKCS8EncodedKeySpec(key));
    }

    private PublicKey decodePublicKey(byte[] key) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(new X509EncodedKeySpec(key));
    }
}

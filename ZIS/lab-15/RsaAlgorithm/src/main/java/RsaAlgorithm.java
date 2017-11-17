import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Реализация алгоритма шифрования RSA.
 */
public class RsaAlgorithm {

    public static class Constants {
        public static final BigInteger BigIntegerOne = new BigInteger("1");
        public static final BigInteger BigIntegerTwo = new BigInteger("2");
    }

    /**
     * Значение длины ключа по умолчанию.
     */
    public static final int DefaultKeyLength = 1024;

    // Публичный ключ
    private BigInteger n, e;

    // Приватный ключ
    private BigInteger d;

    public RsaAlgorithm() {
        init(DefaultKeyLength);
    }

    public RsaAlgorithm(int keyLength){
        init(keyLength);
    }

    /**
     * Зашифрованный текст определяется по формуле: E = M^e mod n;
     */
    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    /**
     * Исходный текст определяется по формуле: M = E^d mod n;
     */
    public BigInteger decrypt(BigInteger message) {
        return message.modPow(d, n);
    }

    public BigInteger getPrivateKey() {
        return d;
    }

    public BigInteger getPublicKey() {
        return e;
    }

    private void init(int keyLength){
        SecureRandom random = new SecureRandom();

        BigInteger q = generateQ(keyLength, random);
        BigInteger p = generateP(keyLength, random);
                   n = generateN(q, p);

        BigInteger phiN = generatePhiN(q, p);
                   e = generateE(phiN, keyLength, random);
                   d = generateD(e, phiN);
    }

    /**
     * Получаем простое число Q.
     */
    private static BigInteger generateQ(int keyLength, SecureRandom random) {
        return BigInteger.probablePrime(keyLength / 2, random);
    }

    /**
     * Получаем простое число P.
     */
    private static BigInteger generateP(int keyLength, SecureRandom random) {
        return BigInteger.probablePrime(keyLength / 2, random);
    }

    /**
     * Вычисляем n = p * q;
     */
    private BigInteger generateN(BigInteger q, BigInteger p) {
        return p.multiply(q);
    }

    /**
     * Вычисляем phi(n) = (p - 1)*(q - 1);
     */
    private BigInteger generatePhiN(BigInteger q, BigInteger p) {
        return p.subtract(Constants.BigIntegerOne)
                .multiply(q.subtract(Constants.BigIntegerOne));
    }

    /**
     * Вычисляем е как число взаимно-простое с phi(n);
     */
    private BigInteger generateE(BigInteger phiN, int keyLength, SecureRandom random){
        BigInteger e = BigInteger.probablePrime(keyLength / 4, random);
        while (phiN.gcd(e).intValue() > 1) {
            e = e.add(Constants.BigIntegerTwo);
        }

        return e;
    }

    /**
     * Вычисляем d из уравнения de = 1 mod phi(n) -> d = (1 mod phi(n)) / e -> d = e^(-1) mod phi(n);
     */
    private BigInteger generateD(BigInteger e, BigInteger phiN){
        return e.modInverse(phiN);
    }
}

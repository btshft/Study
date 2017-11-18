import javafx.util.Pair;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.params.DHParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * В Bouncy Castle не реализован алгоритм El Gamal для подписи :(
 * http://bouncy-castle.1462172.n4.nabble.com/ElGamal-signature-td4657354.html
 * https://ru.wikipedia.org/wiki/Схема_Эль-Гамаля
 */
public class ElGamalSignatureAlgorithm {

    private final int _keyLength;
    private final SecureRandom random = new SecureRandom();
    private final BigInteger p, g;

    public ElGamalSignatureAlgorithm(int keyLength){
        _keyLength = keyLength;
        DHParametersGenerator dhParametersGenerator = new DHParametersGenerator();
        dhParametersGenerator.init(keyLength, 40, random);

        DHParameters parameters = dhParametersGenerator.generateParameters();

        g = parameters.getG();
        p = parameters.getP();
    }

    public Pair<BigInteger, BigInteger> sign(BigInteger message, BigInteger privateKey){
        BigInteger ephemeralKey = computeE(p, _keyLength, random);
        BigInteger firstPart = g.modPow(ephemeralKey, p);
        BigInteger secondPart = (message.subtract(privateKey.multiply(firstPart)))
                .multiply(ephemeralKey.modInverse(p.subtract(BigInteger.ONE)))
                .mod(p.subtract(BigInteger.ONE));

        return new Pair<BigInteger, BigInteger>(firstPart, secondPart);
    }

    public boolean verify(BigInteger message, BigInteger publicKey, Pair<BigInteger, BigInteger> signature){
        BigInteger firstPart = (publicKey.modPow(signature.getKey(), p)
                .multiply(signature.getKey().modPow(signature.getValue(), p)))
                .mod(p);

        BigInteger secondPart = g.modPow(message, p);

        return firstPart.equals(secondPart);
    }

    public BigInteger createPrivateKey(){
        BigInteger s;

        while(true){
            s = new BigInteger(_keyLength, random);
            if(s.compareTo(BigInteger.ONE) > 0 &&
                    s.compareTo(p.subtract(BigInteger.ONE)) < 0)
                break;
        }

        return s;
    }

    public BigInteger createPublicKey(BigInteger privateKey){
        return g.modPow(privateKey, p);
    }

    private static BigInteger computeE(BigInteger p, int keyLength, SecureRandom random){
        BigInteger e;
        while(true){
            e = new BigInteger(keyLength, random);
            if(e.compareTo(BigInteger.ONE) > 0 &&
                    e.compareTo(p.subtract(BigInteger.ONE)) < 0 &&
                    e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE))
                break;
        }

        return e;
    }
}

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class ElGamalAlgorithmTests {

    @Test
    public void Test_ElGamal_Signature(){
        // Arrange
        ElGamalSignatureAlgorithm algorithm = new ElGamalSignatureAlgorithm(512);
        BigInteger message  = BigInteger.valueOf(123);

        BigInteger privateKey = algorithm.createPrivateKey();
        BigInteger publicKey = algorithm.createPublicKey(privateKey);

        // Act
        Pair<BigInteger, BigInteger> signature = algorithm.sign(message, privateKey);
        boolean isValid = algorithm.verify(message, publicKey, signature);

        // Assert
        Assert.assertTrue(isValid);
    }

}

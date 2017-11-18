package encryption;

import hash.IHashProvider;
import models.SignedContainer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.Serializable;
import java.security.*;

public class BouncyCastleDigitalSigner implements IDigitalSigner {

    private static final SecureRandom random = new SecureRandom();

    private final String signatureAlgorithm;
    private IHashProvider hashProvider;

    public BouncyCastleDigitalSigner(String signatureAlgorithm, IHashProvider hashProvider) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.hashProvider = hashProvider;
    }

    @Override
    public <T extends Serializable> SignedContainer<T> sign(T data, PrivateKey privateKey) throws Exception {
        byte[] hash = hashProvider.hash(data);

        Signature signer = Signature.getInstance(signatureAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        signer.initSign(privateKey, random);
        signer.update(hash);

        byte[] signature = signer.sign();

        return new SignedContainer<>(data, signature);
    }

    @Override
    public <T extends Serializable> boolean verify(SignedContainer<T> container, PublicKey publicKey) throws Exception {
        byte[] hash = hashProvider.hash(container.getData());

        Signature signer = Signature.getInstance(signatureAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        signer.initVerify(publicKey);
        signer.update(hash);

        return signer.verify(container.getSignature());
    }
}

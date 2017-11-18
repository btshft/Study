package factories;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface IAsymmetricKeyFactory {
    PublicKey createPublicKey();
    PrivateKey createPrivateKey();
}

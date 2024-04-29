import java.security.*;
import java.util.Base64;

public class Transaction {
    private String id;
    private long timestamp;
    private String data;
    private String signature; // Digital signature

    public Transaction(String id, long timestamp, String data, String signature) {
        this.id = id;
        this.timestamp = timestamp;
        this.data = data;
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getSignature() {
        return signature;
    }

    // Sign the transaction with a private key
    public void signTransaction(PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data.getBytes("UTF-8"));
        byte[] signatureBytes = sig.sign();
        this.signature = Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Verify the transaction signature with a public key
    public boolean verifySignature(PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes("UTF-8"));
        return sig.verify(Base64.getDecoder().decode(signature));
    }
}

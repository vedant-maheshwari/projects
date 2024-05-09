import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Block {
    private String previousHash;
    private String hash;
    private List<Object> transactions;
    private long nonce; // Nonce for PoW
    private int difficulty; // Difficulty level
    private long timestamp;
    private String minerAddress;

    public Block(String previousHash, List<Object> transactions, int difficulty, String minerAddress) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.difficulty = difficulty;
        this.nonce = 0;
        this.minerAddress = minerAddress;
        this.timestamp = System.currentTimeMillis();
        this.hash = mineBlock(); // Mine the block to find a valid hash
    }

    String calculateHash() {
        StringBuilder input = new StringBuilder();
        input.append(previousHash)
             .append(timestamp) // Include timestamp in hash calculation
             .append(minerAddress) // Include miner's address in hash calculation
             .append(nonce)
             .append(transactionsToString());
             for (Object tx : transactions) {
                input.append(tx.toString());
            }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //String input = previousHash + nonce + transactions.toString();
            byte[] hash = digest.digest(input.toString().getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String mineBlock() {
        nonce = 0; // Start nonce from zero
        String newHash = calculateHash();
        while (!newHash.substring(0, difficulty).equals(repeat("0", difficulty))) { // Check if the hash meets the difficulty
            nonce++; // Increment nonce
            newHash = calculateHash(); // Recalculate hash
        }
        return newHash;
    }
    private String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
    private String transactionsToString() {
        StringBuilder sb = new StringBuilder();
        for (Object tx : transactions) {
            if (tx instanceof Transaction) {
                Transaction regularTx = (Transaction) tx;
                sb.append(regularTx.getId())
                  .append(regularTx.getTimestamp())
                  .append(regularTx.getSignature());
            } else if (tx instanceof CoinbaseTransaction) {
                CoinbaseTransaction coinbaseTx = (CoinbaseTransaction) tx;
                sb.append(coinbaseTx.getId())
                  .append(coinbaseTx.getTimestamp())
                  .append(coinbaseTx.getReward())
                  .append(coinbaseTx.getMinerAddress());
            }
        }
        return sb.toString();
    }    

    private boolean isHashValid(String hash) {
        return hash.startsWith("0".repeat(difficulty)); // Check for leading zeros
    }    

    public String getHash() {
        return hash;
    }
    public long getNonce() {
        return nonce;
    } 

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Object> getTransactions() {
        return transactions;
    }
    public String getMinerAddress() {
        return minerAddress;
    }
    public long getTimestamp() {
        return timestamp;
    }
}

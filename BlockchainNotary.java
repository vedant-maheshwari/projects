import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.Date;

public class BlockchainNotary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Blockchain blockchain = new Blockchain();

        // Exception handling to address possible exceptions
        try {
            // Generate key pairs for signature and verification
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            boolean addMoreBlocks = true;
            int blockReward = 50;
            while (addMoreBlocks) {
                // Ask the user how many transactions they want to add
                System.out.print("Enter the number of transactions for the new block: ");
                int transactionCount = scanner.nextInt();
                scanner.nextLine(); // consume newline

                List<Object> transactions = new ArrayList<>();
                String minerAddress = "Miner-1"; // Address of the miner who mined the block
                CoinbaseTransaction coinbaseTx = new CoinbaseTransaction(minerAddress, blockReward);
                transactions.add(coinbaseTx);

                // Gather transactions from the user
                for (int i = 0; i < transactionCount; i++) {
                    System.out.print("Enter ID for transaction " + (i + 1) + ": ");
                    String id = scanner.nextLine();

                    System.out.print("Enter data for transaction " + (i + 1) + ": ");
                    String data = scanner.nextLine();

                    // Create a new transaction and sign it with the private key
                    Transaction tx = new Transaction(id, System.currentTimeMillis(), data, null);
                    tx.signTransaction(privateKey);
                    transactions.add(tx);
                }

                // Get the last block's hash and difficulty for mining the new block
                Block lastBlock = blockchain.getChain().get(blockchain.getChain().size() - 1);
                int difficulty = 3;

                // Create a new block with the signed transactions and assign a miner address
                Block newBlock = new Block(
                    lastBlock.getHash(),
                    transactions,
                    difficulty,
                    "Miner-1"
                );

                blockchain.addBlock(newBlock);

                // Ask if the user wants to add more blocks
                System.out.print("Do you want to add another block? (yes/no): ");
                String response = scanner.nextLine();
                addMoreBlocks = response.equalsIgnoreCase("yes");
            }

            // Create a SimpleDateFormat to specify the output format for readable timestamps
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Display the entire blockchain in a readable format
            System.out.println("\n--- Blockchain Structure ---");
            for (int i = 0; i < blockchain.getChain().size(); i++) {
                Block block = blockchain.getChain().get(i);

                System.out.println("Block " + (i + 1));
                System.out.println("Previous Hash: " + block.getPreviousHash());
                System.out.println("Hash: " + block.getHash());
                System.out.println("Nonce: " + block.getNonce());
                
                // Convert the timestamp to a readable format
                System.out.println("Timestamp: " + sdf.format(new Date(block.getTimestamp())));
                
                System.out.println("Miner's Address: " + block.getMinerAddress());
                
                System.out.println("Transactions:");
                for (Object tx : block.getTransactions()) {
                    if (tx instanceof CoinbaseTransaction) {
                        CoinbaseTransaction coinbase = (CoinbaseTransaction) tx;
                        System.out.println("   Coinbase: Miner " + coinbase.getMinerAddress() + ", Reward: " + coinbase.getReward() + ", Timestamp: " + sdf.format(new Date(coinbase.getTimestamp())));
                    } else if (tx instanceof Transaction) {
                        Transaction regularTx = (Transaction) tx;
                        System.out.println(" - ID: " + regularTx.getId() + ", Data: " + regularTx.getData() + ", Timestamp: " + sdf.format(new Date(regularTx.getTimestamp())) + ", Signature: " + regularTx.getSignature());
                    }
                }
                System.out.println();
            }

            System.out.println("Is the blockchain valid? " + blockchain.isChainValid());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: RSA algorithm not found.");
        } catch (SignatureException | InvalidKeyException e) {
            System.out.println("Error signing or verifying transaction.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        int initialDifficulty = 1; // Set a low difficulty for the genesis block
        String initialMinerAddress = "Genesis Miner";
        // Create the genesis block
        Block genesisBlock = new Block("0", new ArrayList<>(), initialDifficulty, initialMinerAddress);
        chain.add(genesisBlock); // Add it to the chain
    }

    // public void addBlock(List<Transaction> transactions, int difficulty) {
    //     Block previousBlock = chain.get(chain.size() - 1); // Get the last block
    //     // Create a new block with the previous block's hash, new transactions, and the given difficulty
    //     Block newBlock = new Block(previousBlock.getHash(), transactions, difficulty);
    //     chain.add(newBlock); // Add the block to the chain
    // }
    public void addBlock(Block block) {
        if (validateBlock(block)) {
            chain.add(block); // Add the block to the chain if it's valid
        } else {
            throw new IllegalArgumentException("Block is invalid");
        }
    }

    private boolean validateBlock(Block block) {
        Block lastBlock = chain.get(chain.size() - 1);
        return lastBlock.getHash().equals(block.getPreviousHash());
    }

    public List<Block> getChain() {
        return chain;
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
        }
        return true; // The blockchain is valid
    }
}

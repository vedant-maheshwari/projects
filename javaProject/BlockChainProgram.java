import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BlockChainProgram {
    private static final int MINING_REWARD = 10; // Define the mining reward
    private static final String WALLET_FILE = "wallets.dat"; // File to store wallet data
    private static final String MINER_ADDRESS = "Miner Address"; // Placeholder for actual miner address

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Block> blockChain = new ArrayList<>();
        Map<String, Wallet> wallets = loadWallets(); // Load wallets from file
        boolean continueAddingBlocks = true;

        while (continueAddingBlocks) {
            System.out.println("Enter name, comment, and date for the next transaction (comma-separated), or type 'stop' to finish adding blocks:");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("stop")) {
                continueAddingBlocks = false;
            } else {
                String[] transactionData = input.split(",");
                if (transactionData.length < 3) {
                    System.out.println("Invalid input format. Please provide name, comment, and date separated by commas.");
                    continue;
                }
                
                String name = transactionData[0].trim();
                String comment = transactionData[1].trim();
                String dateString = transactionData[2].trim();
                
                LocalDate date;
                try {
                    date = LocalDate.parse(dateString);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please provide the date in YYYY-MM-DD format.");
                    continue;
                }
                
                Transaction transaction = new Transaction(name, comment, date);
                
                int previousBlockHash = blockChain.isEmpty() ? 0 : blockChain.get(blockChain.size() - 1).getBlockHash();
                Block newBlock = new Block(new Transaction[]{transaction}, previousBlockHash);

                if (isBlockValid(newBlock, blockChain)) {
                    // Add mining reward transaction
                    Wallet minerWallet = wallets.computeIfAbsent(MINER_ADDRESS, k -> new Wallet());
                    minerWallet.addBalance(MINING_REWARD); // Add mining reward to miner's wallet
                    
                    Transaction miningRewardTransaction = new Transaction("Mining Reward", "Mining reward for block " + blockChain.size(), LocalDate.now());
                    newBlock.addTransaction(miningRewardTransaction);
                    
                    blockChain.add(newBlock);
                    System.out.println("Block added: " + newBlock.toString());
                    System.out.println("The block chain is now: " + blockChain.toString());
                    System.out.println("Miner rewarded with " + MINING_REWARD + " coins!");
                } else {
                    System.out.println("Invalid block! Rejected.");
                }
            }
        }

        System.out.println("Block chain creation stopped. Final block chain:");
        System.out.println(blockChain.toString());

        saveWallets(wallets); // Save wallets to file

        // Loop for checking individual block validity
        boolean continueValidating = true;
        while (continueValidating) {
            System.out.println("Do you want to check the validity of a block? Enter 'yes' or 'no':");
            String validateInput = scanner.nextLine();
            if (validateInput.equalsIgnoreCase("yes")) {
                System.out.println("Enter the string to compare with block transactions:");
                String searchString = scanner.nextLine();
                checkBlockValidity(searchString, blockChain);
            } else if (validateInput.equalsIgnoreCase("no")) {
                continueValidating = false;
                System.out.println("Final block chain:");
                System.out.println(blockChain.toString());
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        // Check miner's balance
        Wallet minerWallet = wallets.get(MINER_ADDRESS);
        if (minerWallet != null) {
            System.out.println("Miner's balance: " + minerWallet.getBalance() + " coins");
        } else {
            System.out.println("Miner's wallet not found.");
        }
    }

    public static boolean isBlockValid(Block newBlock, ArrayList<Block> blockChain) {
        if (blockChain.isEmpty()) {
            return true; // First block is always valid
        } else {
            Block previousBlock = blockChain.get(blockChain.size() - 1);
            return newBlock.isValid(previousBlock);
        }
    }

    public static void checkBlockValidity(String searchString, ArrayList<Block> blockChain) {
        boolean found = false;
        for (Block block : blockChain) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.getName().equals(searchString) || transaction.getComment().equals(searchString)) {
                    System.out.println("String found in block: " + block.toString());
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("String not found in any block.");
        }
    }

    public static Map<String, Wallet> loadWallets() {
        Map<String, Wallet> wallets = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(WALLET_FILE))) {
            wallets = (Map<String, Wallet>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Wallet file not found or could not be read. Creating new wallets.");
        }
        return wallets;
    }

    public static void saveWallets(Map<String, Wallet> wallets) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(WALLET_FILE))) {
            oos.writeObject(wallets);
        } catch (IOException e) {
            System.out.println("Error saving wallets to file: " + e.getMessage());
        }
    }
}

class Block {
    private Transaction[] transactions;
    private int blockHash;
    private int previousBlockHash;
    
    @Override
    public String toString() {
        return "Block [transactions=" + Arrays.toString(transactions) + ", blockHash=" + blockHash + ", previousBlockHash=" + previousBlockHash + "]";
    }
    
    public Block(Transaction[] transactions, int previousBlockHash) {
        super();
        this.transactions = transactions;
        this.previousBlockHash = previousBlockHash;
        this.blockHash = Arrays.hashCode(new int[]{Arrays.hashCode(Arrays.toString(transactions).getBytes()), this.previousBlockHash});
    }
    
    public Transaction[] getTransactions() {
        return transactions;
    }
    
    public void addTransaction(Transaction transaction) {
        Transaction[] newTransactions = Arrays.copyOf(transactions, transactions.length + 1);
        newTransactions[transactions.length] = transaction;
        this.transactions = newTransactions;
    }
    
    public int getBlockHash() {
        return blockHash;
    }
    
    public void setBlockHash(int blockHash) {
        this.blockHash = blockHash;
    }
    
    public int getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public void setPreviousBlockHash(int previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }
    
    public boolean isValid(Block previousBlock) {
        return this.blockHash == Arrays.hashCode(new int[]{Arrays.hashCode(Arrays.toString(transactions).getBytes()), previousBlock.getBlockHash()});
    }
}

class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String comment;
    private LocalDate date;

    public Transaction(String name, String comment, LocalDate date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getDate() {
        return date;
    }
}

class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;
    private int balance;

    public Wallet() {
        this.balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void deductBalance(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            System.out.println("Insufficient balance.");
        }
    }
}

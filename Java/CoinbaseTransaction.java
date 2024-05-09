public class CoinbaseTransaction {
    private String id;
    private long timestamp;
    private String minerAddress;
    private int reward; // Reward amount for the miner

    public CoinbaseTransaction(String minerAddress, int reward) {
        this.id = "coinbase-" + System.currentTimeMillis(); // Unique ID for the coinbase transaction
        this.timestamp = System.currentTimeMillis();
        this.minerAddress = minerAddress;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public int getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return "CoinbaseTransaction [minerAddress=" + minerAddress + ", reward=" + reward + ", timestamp=" + timestamp + "]";
    }
}

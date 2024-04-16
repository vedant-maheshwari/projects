package java;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockChainProgram {
    public static void main(String[] args) {
  
        String statement1 = "Java java java"; 
        int hashvalue = statement1.hashCode();
         
        System.out.println("Statement - " + statement1 + " Hash value=" + hashvalue);
        
        String[] list1 = {"pg", "pgpg", "pgpgpg"}; 
        String[] list2 = {"PG", "PGPG", "PGPGPG"};
        
        int hash1 = Arrays.hashCode(list1); 
        int hash2 = Arrays.hashCode(list2);
        System.out.println("hash1=" + hash1 + " hash2=" + hash2);
        
        //series of blocks in a chain 
        ArrayList<Block> blockChain = new ArrayList<Block>();
        
        String[] initialValues = {"I HAVE 0 MONEY", "PGPG HAS 500000"};
        Block firstBlock = new Block(initialValues, 0); 
        blockChain.add(firstBlock);
        System.out.println("First block is " + firstBlock.toString()); 
        System.out.println("The block chain is " + blockChain.toString());
        
        // block two
        String[] shadGivesItAway = {"I KEPT PGPG K 50 RUPEES", "I GAVE 20 TO NIK ", "W GAVE ME 10000"};
        Block secondBlock = new Block(shadGivesItAway, firstBlock.getBlockHash());
        blockChain.add(secondBlock);
        System.out.println("Second block is " + secondBlock.toString());
        System.out.println("The block chain is " + blockChain.toString());
        
        // block three
        String[] shadGetsSomeBack = {"I GIVE W 50 NOW HE HAS 50", "NIK GIVES W 1000"}; 
        Block thirdBlock = new Block(shadGetsSomeBack, secondBlock.getBlockHash());
        blockChain.add(thirdBlock);
        System.out.println("Third block is " + thirdBlock.toString()); 
        System.out.println("The block chain is " + blockChain.toString());
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainreader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

/**
 *
 * @author CED
 */
public class BlockchainReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlockchainReader tb = new BlockchainReader();
        tb._doWork();
    }
    // TODO: Replace with valid path
    // blk*dat folder path
    static String PREFIX = "C:/blocks/";

    // A simple method with everything in it
    public void _doWork() {

        // Initial setup
        NetworkParameters np = new MainNetParams();
        Context.getOrCreate(MainNetParams.get());

        // BloclFileLoaderObject initialized with block files
        BlockFileLoader loader = new BlockFileLoader(np, _getListOfBlockFiles());

        // Store results in a map in the form 
        // day -> n. of transactions
        Map<String, Integer> dailyTotTxs = new HashMap<>();

        // Progress counter
        int blockCounter = 0;

        // bitcoinj magic; read files
        for (Block block : loader) {

            blockCounter++;
            // Just for rogress tracking
            System.out.println("Analysing block " + blockCounter);

            // Block.getTime() returns date
            // Get only day and convert to string
            String day = new SimpleDateFormat("yyyy-MM-dd").format(block.getTime());

            // Populate map (day -> number of transactions)
            // If date is non existing in map, create entry
            if (!dailyTotTxs.containsKey(day)) {
                dailyTotTxs.put(day, 0);
            }
            
            // TODO: For efficiency, use block.getTransactions().size()
            // This loop shows how to iterate over transacions in a block and add 1 to corresponding map entry
            for (Transaction tx : block.getTransactions()) {
                dailyTotTxs.put(day, dailyTotTxs.get(day) + 1);
            }
        } // End loop

        // Result
        for (String d : dailyTotTxs.keySet()) {
            System.out.println(d + "," + dailyTotTxs.get(d));
        }
    }  // end method

    // Return files according to pattern (block files uses name like blkNNNNN.dat)
    private List<File> _getListOfBlockFiles() {
        List<File> list = new LinkedList<File>();
        for (int i = 0; true; i++) {
            File file = new File(PREFIX + String.format(Locale.US, "blk%05d.dat", i));
            if (!file.exists()) {
                break;
            }
            list.add(file);
        }
        return list;
    }
}

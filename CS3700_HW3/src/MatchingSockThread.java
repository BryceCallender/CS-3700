import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MatchingSockThread extends Thread {
    private BlockingQueue<Sock> blockingQueue; //Checking for pairs
    private BlockingQueue<String> washingQueue;
    private ConcurrentHashMap<String, Integer> sockMap;

    MatchingSockThread(BlockingQueue<Sock> blockingQueue, BlockingQueue<String> washingQueue, ConcurrentHashMap<String, Integer> sockMap) {
        this.blockingQueue = blockingQueue;
        this.washingQueue = washingQueue;
        this.sockMap = sockMap;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while(true) {
            try {
                
                washingQueue.put("Red");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    private boolean stillHaveSocksToMatch() {
//        for (Map.Entry<String, Integer> entry : sockMap.entrySet()) {
//            //2 socks can be matched! It will be done when everything has 0 or 1s left
//            if(entry.getValue() > 1) {
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean canMatchAPair(String color) {
        return sockMap.get(color) > 1;
    }
}

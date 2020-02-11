import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class MatchingSockThread extends Thread {
    private BlockingQueue<Sock> blockingQueue; //Checking for pairs
    private BlockingQueue<String> washingQueue;
    private Set<String> sockCheck = new HashSet<>();

    MatchingSockThread(BlockingQueue<Sock> blockingQueue, BlockingQueue<String> washingQueue) {
        this.blockingQueue = blockingQueue;
        this.washingQueue = washingQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {

                Iterator<Sock> blockingQueueItr = blockingQueue.iterator();
                Sock itrSock = null;
                boolean foundMatch = false;
                while(!foundMatch && blockingQueueItr.hasNext()) {
                    itrSock = blockingQueueItr.next();
                    if(sockCheck.contains(itrSock.color)) {
                        System.out.format("Matching Thread: Send %s socks to washer. Total socks %d. Total inside queue %d %n",
                                itrSock.color, blockingQueue.size(), washingQueue.size());
                        foundMatch = true;
                        washingQueue.put(itrSock.color);
                        sockCheck.clear();
                    }else {
                        sockCheck.add(itrSock.color);
                    }
                }
            }
        } catch (InterruptedException e) {

        }
    }
}

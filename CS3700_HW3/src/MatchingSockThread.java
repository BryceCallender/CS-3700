import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class MatchingSockThread extends Thread {
    private BlockingDeque<Sock> blockingDeque; //Checking for pairs
    private BlockingQueue<String> washingQueue;
    public Set<String> sockSet = new HashSet<>();

    MatchingSockThread(BlockingDeque<Sock> blockingDeque, BlockingQueue<String> washingQueue) {
        this.blockingDeque = blockingDeque;
        this.washingQueue = washingQueue;

        sockSet = Collections.synchronizedSet(sockSet);
    }

    @Override
    public void run() {
        while(true) {
            try {
                Iterator<Sock> sockIterator = blockingDeque.iterator();
                boolean matchedPair = false;

                //While we have not found a pair and the iterator has something to go to next
                while(!matchedPair && sockIterator.hasNext()) {
                    Sock sockCheck = sockIterator.next();
                    if(sockSet.contains(sockCheck.color)) {
                        matchedPair = true;
                        blockingDeque.remove(sockCheck);
                        boolean removedSecondSock = false;

                        while(!removedSecondSock && sockIterator.hasNext()) {
                            Sock removeSock = sockIterator.next();

                            if(removeSock.color.equals(sockCheck.color)) {
                                blockingDeque.remove(removeSock);
                                removedSecondSock = true;
                            }
                        }
                        System.out.format("Matching Thread: Send %s socks to washer. Total socks %d. Total inside queue %d %n",
                                sockCheck.color, blockingDeque.size(), washingQueue.size());

                        washingQueue.put(sockCheck.color);
                    }else {
                        sockSet.add(sockCheck.color);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

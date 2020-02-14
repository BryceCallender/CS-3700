import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class MatchingSockThread extends Thread {
    private BlockingDeque<Sock> blockingDeque; //Checking for pairs
    private BlockingQueue<String> washingQueue;
    public Set<String> sockSet = new HashSet<>();

    private Sock sockCheck = null;
    private boolean removedSecondSock = false;

    MatchingSockThread(BlockingDeque<Sock> blockingDeque, BlockingQueue<String> washingQueue) {
        this.blockingDeque = blockingDeque;
        this.washingQueue = washingQueue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Iterator<Sock> sockIterator = blockingDeque.iterator();

                while(sockIterator.hasNext() && !isSockUnique(sockCheck = sockIterator.next())) {}

                boolean matchedPair = false;

                //While we have not found a pair and the iterator has something to go to next
                while(!matchedPair && sockIterator.hasNext()) {
                    //Sock sockCheck = sockIterator.next();
                    Sock removeSock = null;
                    if(sockSet.contains(sockCheck.color)) {
                        matchedPair = true;
                        removedSecondSock = false;

                        sockIterator = blockingDeque.iterator();
                        while(!removedSecondSock && sockIterator.hasNext()) {
                            removeSock = sockIterator.next();

                            if(sockCheck != removeSock && removeSock.color.equals(sockCheck.color)) {
                                removedSecondSock = true;
                            }
                        }

                        if(removedSecondSock) {
                            blockingDeque.remove(sockCheck);
                            blockingDeque.remove(removeSock);

                            System.out.format("Matching Thread: Send %s socks to washer. Total socks %d. Total inside queue %d %n",
                                    sockCheck.color, blockingDeque.size(), washingQueue.size());

                            sockSet.clear();
                            washingQueue.put(sockCheck.color);
                        } else {
                            //Second sock wasnt removed so it was not found
                        }
                    }else {
                        sockSet.add(sockCheck.color);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasPairsStill() {
        Set<String> socks = new HashSet<>();

        for (Sock sockCheck : blockingDeque) {
            if (socks.contains(sockCheck.color)) {
                return true;
            } else {
                socks.add(sockCheck.color);
            }
        }

        return false;
    }

    private boolean isSockUnique(Sock color) {
        for(Sock sockCheck: blockingDeque) {
            if(sockCheck != color && sockCheck.color.equals(color.color)) {
                return true;
            }
        }

        return false;
    }
}

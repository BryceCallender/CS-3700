import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchingSockThread extends Thread {
    private BlockingDeque<Sock> blockingDeque; //Checking for pairs
    private BlockingQueue<String> washingQueue;
    public Set<String> sockSet = new HashSet<>();
    private AtomicInteger atomicInteger;
    boolean isDone = false;

    MatchingSockThread(BlockingDeque<Sock> blockingDeque, BlockingQueue<String> washingQueue, AtomicInteger atomicInteger) {
        this.blockingDeque = blockingDeque;
        this.washingQueue = washingQueue;
        this.atomicInteger = atomicInteger;
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
                    Sock removeSock = null;
                    if(sockSet.contains(sockCheck.color)) {
                        matchedPair = true;
                        blockingDeque.remove(sockCheck);
                        boolean removedSecondSock = false;

                        sockIterator = blockingDeque.iterator();
                        while(!removedSecondSock && sockIterator.hasNext()) {
                            removeSock = sockIterator.next();

                            if(removeSock.color.equals(sockCheck.color)) {

                                removedSecondSock = true;
                            }
                        }

                        if(matchedPair && removedSecondSock) {
                            System.out.println(sockCheck.color + ":" + removeSock.color);
                            blockingDeque.remove(sockCheck);
                            blockingDeque.remove(removeSock);
                            System.out.println("Successful");
                        }else {
                            System.out.println("OOF " + blockingDeque.size());
                            break;
                        }

                        System.out.format("Matching Thread: Send %s socks to washer. Total socks %d. Total inside queue %d %n",
                                sockCheck.color, blockingDeque.size(), washingQueue.size());

                        System.out.println("Sock set length: " + sockSet.size());
                        sockSet.clear();
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

    public boolean hasPairsStill() {
        Set<String> socks = new HashSet<>();

        Iterator<Sock> sockIterator = blockingDeque.iterator();
        while(sockIterator.hasNext()) {
            Sock sockCheck = sockIterator.next();
            if(socks.contains(sockCheck.color)) {
                return true;
            } else {
                socks.add(sockCheck.color);
            }
        }



        return false;
    }
}

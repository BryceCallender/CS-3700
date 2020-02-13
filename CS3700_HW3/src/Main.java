import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        //Question 1
        BlockingDeque<Sock> blockingDeque = new LinkedBlockingDeque<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(100);
        AtomicInteger sockThreadsFinished = new AtomicInteger(0);

        new SockThread("Red", blockingDeque, sockThreadsFinished).start();
        new SockThread("Green", blockingDeque, sockThreadsFinished).start();
        new SockThread("Blue", blockingDeque, sockThreadsFinished).start();
        new SockThread("Orange", blockingDeque, sockThreadsFinished).start();

        MatchingSockThread matchingThread = new MatchingSockThread(blockingDeque, washingQueue, sockThreadsFinished);
        Thread washerThread = new WasherMachineThread(washingQueue);

        matchingThread.start();
        washerThread.start();

        //Lets keep just wait here until all the sock threads are done
        //I dont want to use join because i want them to do any order
        while(sockThreadsFinished.get() != 4) {

        }

        System.out.println("Sock threads finished");

        while(matchingThread.hasPairsStill()) {

        }

        //wait on washing to finish
        while(!washingQueue.isEmpty()) {

        }

        System.out.println(blockingDeque.size());
        System.out.println("Washing is done");

        System.exit(0);
    }
}

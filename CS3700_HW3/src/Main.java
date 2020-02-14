import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        BlockingDeque<Sock> blockingDeque = new LinkedBlockingDeque<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(100);
        AtomicInteger sockThreadsFinished = new AtomicInteger(0);

        new SockThread("Red", blockingDeque, sockThreadsFinished).start();
        new SockThread("Green", blockingDeque, sockThreadsFinished).start();
        new SockThread("Blue", blockingDeque, sockThreadsFinished).start();
        new SockThread("Orange", blockingDeque, sockThreadsFinished).start();

        MatchingSockThread matchingThread = new MatchingSockThread(blockingDeque, washingQueue);
        Thread washerThread = new WasherMachineThread(washingQueue);

        matchingThread.start();
        washerThread.start();

        //Tested out atomic instead of joining threads
        while(sockThreadsFinished.get() != 4) {

        }

        System.out.println("Sock threads finished");

        while(matchingThread.hasPairsStill()) {

        }

        System.out.println("No more socks to match!");

        //wait on washing to finish
        while(!washingQueue.isEmpty()) {

        }

        System.out.println("Washing is done!");

        System.out.print("left over socks: ");
        blockingDeque.forEach((sock -> {
            System.out.print(sock.color + " ");
        }));
        System.out.println();

        System.exit(0);
    }
}

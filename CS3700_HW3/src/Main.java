import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        //Question 1
        BlockingQueue<Sock> blockingQueue = new ArrayBlockingQueue<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(100);
        ConcurrentHashMap<String, Integer> sockMap = new ConcurrentHashMap<>();

        sockMap.put("Red", 0);
        sockMap.put("Green", 0);
        sockMap.put("Blue", 0);
        sockMap.put("Orange", 0);

        new MatchingSockThread(blockingQueue, washingQueue, sockMap).start();
        //new WasherMachineThread(washingQueue).start();

        new SockThread("Red", blockingQueue, sockMap).start();
        new SockThread("Green", blockingQueue, sockMap).start();
        new SockThread("Blue", blockingQueue, sockMap).start();
        new SockThread("Orange", blockingQueue, sockMap).start();
    }
}

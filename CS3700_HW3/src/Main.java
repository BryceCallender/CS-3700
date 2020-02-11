import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Sock> blockingQueue = new ArrayBlockingQueue<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(100);

        new MatchingSockThread(blockingQueue, washingQueue).start();
        new WasherMachineThread(washingQueue).start();

        new SockThread("Red", blockingQueue).start();
        new SockThread("Green", blockingQueue).start();
        new SockThread("Blue", blockingQueue).start();
        new SockThread("Orange", blockingQueue).start();

    }
}

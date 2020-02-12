import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        //Question 1
        BlockingDeque<Sock> blockingDeque = new LinkedBlockingDeque<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(100);

        new SockThread("Red", blockingDeque).start();
        new SockThread("Green", blockingDeque).start();
        new SockThread("Blue", blockingDeque).start();
        new SockThread("Orange", blockingDeque).start();

        new MatchingSockThread(blockingDeque, washingQueue).start();
        new WasherMachineThread(washingQueue).start();
    }
}

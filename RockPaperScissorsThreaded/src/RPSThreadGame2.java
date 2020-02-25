import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class RPSThreadGame2 implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;
    BlockingQueue<RPSThreadGame2> rpsQueue;


    RPSThreadGame2(BlockingQueue<RPSThreadGame2> rpsQueue) {
        name = "Thread" + rpsQueue.size();
        this.rpsQueue = rpsQueue;
    }

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);
    }

    public void redoGesture() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": retried and got " + handGesture);
    }
}
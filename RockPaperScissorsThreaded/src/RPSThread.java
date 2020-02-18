import java.util.concurrent.ThreadLocalRandom;

public class RPSThread implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String handGesture;

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length) + 1;

        handGesture = values[randomPick];

        System.out.println(handGesture);
    }
}

import java.util.concurrent.ThreadLocalRandom;

public class RPSThread implements Runnable {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;

    RPSThread(int index) {
        name = "Thread" + index;
    }

    @Override
    public void run() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        System.out.println(name + ": used " + handGesture);
    }
}

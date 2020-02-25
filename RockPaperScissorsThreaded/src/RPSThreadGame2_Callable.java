import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class RPSThreadGame2_Callable implements Callable<RPSThreadInfo_Future> {
    String[] values = {"Rock", "Paper", "Scissors"};

    String name;
    String handGesture;

    @Override
    public RPSThreadInfo_Future call() throws Exception {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        handGesture = values[randomPick];

        name = Thread.currentThread().getName();
        System.out.println(name + ": used " + handGesture);

        return new RPSThreadInfo_Future(name, handGesture);
    }
}

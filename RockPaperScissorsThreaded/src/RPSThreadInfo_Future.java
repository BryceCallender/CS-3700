import java.util.concurrent.ThreadLocalRandom;

public class RPSThreadInfo_Future
{
    String[] values = {"Rock", "Paper", "Scissors"};

    private String name;
    private String gesture;

    RPSThreadInfo_Future(String name, String gesture) {
        this.name = name;
        this.gesture = gesture;
    }

    String getName() {
        return name;
    }

    String getGesture() {
        return gesture;
    }

    public void redoGesture() {
        int randomPick = ThreadLocalRandom.current().nextInt(values.length);

        gesture = values[randomPick];

        System.out.println(name + ": retried and got " + getGesture());
    }
}

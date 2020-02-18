import java.util.ArrayList;

public class WinnerThread extends Thread {
    private ArrayList<RPSThread> players;
    private ArrayList<Integer> playerPoints;

    private int indexOfWinner;

    WinnerThread(ArrayList<RPSThread> players) {
        this.players = players;
    }

    @Override
    public void run() {
        super.run();
    }
}

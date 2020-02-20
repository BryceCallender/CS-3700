import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WinnerThreadMemoized extends Thread {
    private ArrayList<RPSThread> players;
    private Map<String, Integer> memoizedTable;
    private ArrayList<Integer> playerPoints;

    WinnerThreadMemoized(ArrayList<RPSThread> players) {
        this.players = players;
        memoizedTable = new HashMap<>();

        memoizedTable.put("Rock", 0);
        memoizedTable.put("Paper", 0);
        memoizedTable.put("Scissors", 0);

        for (RPSThread hand: players) {
            memoizedTable.put(hand.handGesture, memoizedTable.get(hand.handGesture) + 1);
        }
    }

    @Override
    public void run() {
        playerPoints = new ArrayList<>();
        int points = 0;
        ArrayList<RPSThread> threadsToRemove = new ArrayList<>();

        for(RPSThread rpsThread: players) {
            switch(rpsThread.handGesture) {
                case "Rock":
                    points = memoizedTable.get("Scissors") - memoizedTable.get("Paper");
                    break;
                case "Paper":
                    points = memoizedTable.get("Rock") - memoizedTable.get("Scissors");
                    break;
                case "Scissors":
                    points = memoizedTable.get("Paper") - memoizedTable.get("Rock");
                    break;

            }
            playerPoints.add(points);
            points = 0;
        }

        int smallest = Integer.MAX_VALUE;
        int index = 0;

        for (Integer pointCount: playerPoints) {
            if(pointCount < smallest) {
                smallest = pointCount;
            }
            index++;
        }

        index = 0;
        for (Integer count: playerPoints) {
            if(count == smallest) {
                threadsToRemove.add(players.get(index));
            }
            index++;
        }

        int randomRemoval = ThreadLocalRandom.current().nextInt(threadsToRemove.size());
        System.out.println(threadsToRemove.get(randomRemoval).name + " has been eliminated!");
        players.remove(threadsToRemove.remove(randomRemoval));
    }
}

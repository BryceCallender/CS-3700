import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WinnerThread extends Thread {
    private ArrayList<RPSThread> players;
    private ArrayList<Integer> playerPoints;

    WinnerThread(ArrayList<RPSThread> players) {
        this.players = players;
    }

    @Override
    public void run() {
        playerPoints = new ArrayList<>();
        int points;
        ArrayList<RPSThread> threadsToRemove = new ArrayList<>();

        for (RPSThread player: players) {
            points = 0;
            for (RPSThread otherPlayer: players) {
                if(player != otherPlayer) {
                    points += calculatePoints(player.handGesture, otherPlayer.handGesture);
                }
            }
            playerPoints.add(points);
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

    public int calculatePoints(String myGesture, String opponentGesture) {
        switch (myGesture) {
            case "Rock":
                switch (opponentGesture) {
                    case "Rock":
                        return 0;
                    case "Paper":
                        return -1;
                    case "Scissors":
                        return 1;
                }
                break;
            case "Paper":
                switch (opponentGesture) {
                    case "Rock":
                        return 1;
                    case "Paper":
                        return 0;
                    case "Scissors":
                        return -1;
                }
                break;
            case "Scissors":
                switch (opponentGesture) {
                    case "Rock":
                        return -1;
                    case "Paper":
                        return 1;
                    case "Scissors":
                        return 0;
                }
                break;
            default:
                return -999;
        }
        return -999;
    }
}

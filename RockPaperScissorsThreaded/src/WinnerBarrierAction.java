import java.util.ArrayList;

public class WinnerBarrierAction implements Runnable {
    ArrayList<RPSThread2Game2> readyPlayers;

    WinnerBarrierAction(ArrayList<RPSThread2Game2> readyPlayers) {
        this.readyPlayers = readyPlayers;
    }

    @Override
    public void run() {
        System.out.println("Barrier has been triggered");

        RPSThread2Game2 firstPlayer = readyPlayers.get(0);
        RPSThread2Game2 secondPlayer = readyPlayers.get(1);

        System.out.println(firstPlayer.name + " vs " + secondPlayer.name);
        RPSThread2Game2 winner = calculateWinner(firstPlayer, secondPlayer);

        System.out.println(winner.name + " is victorious... Adding to the back of the queue");

        addPlayerAndAwait(readyPlayers, winner);
    }

    public RPSThread2Game2 calculateWinner(RPSThread2Game2 myself, RPSThread2Game2 opponent) {
        if(myself.handGesture.equals(opponent.handGesture)) {
            boolean sameGesture = true;
            while(sameGesture) {
                myself.redoGesture();
                opponent.redoGesture();

                if(!myself.handGesture.equals(opponent.handGesture)) {
                    sameGesture = false;
                }
            }
        }

        String myGesture = myself.handGesture;
        String opponentGesture = opponent.handGesture;

        switch (myGesture) {
            case "Rock":
                switch (opponentGesture) {
                    case "Paper":
                        return opponent;
                    case "Scissors":
                        return myself;
                }
                break;
            case "Paper":
                switch (opponentGesture) {
                    case "Rock":
                        return myself;
                    case "Scissors":
                        return opponent;
                }
                break;
            case "Scissors":
                switch (opponentGesture) {
                    case "Rock":
                        return opponent;
                    case "Paper":
                        return myself;
                }
                break;
        }
        return myself;
    }

    public static void addPlayerAndAwait(ArrayList<RPSThread2Game2> readyPlayers, RPSThread2Game2 winner) {
        readyPlayers.add(winner);
        winner.awaitOpponent();
    }
}

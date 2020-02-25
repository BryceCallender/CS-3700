import java.util.concurrent.BlockingQueue;

public class WinnerBarrierAction implements Runnable {
    BlockingQueue<RPSThread2Game2> readyPlayers;

    WinnerBarrierAction(BlockingQueue<RPSThread2Game2> readyPlayers) {
        this.readyPlayers = readyPlayers;
    }

    @Override
    public void run() {
        while(readyPlayers.size() > 1) {
            try {
                RPSThread2Game2 firstPlayer = readyPlayers.take();
                RPSThread2Game2 secondPlayer = readyPlayers.take();

                System.out.println(firstPlayer.name + " vs " + secondPlayer.name);
                RPSThread2Game2 winner = calculateWinner(firstPlayer, secondPlayer);

                System.out.println(winner.name + " is victorious... Adding to the back of the queue");

                addPlayerAndAwait(readyPlayers, winner);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void addPlayerAndAwait(BlockingQueue<RPSThread2Game2> readyPlayers, RPSThread2Game2 winner) {
        try {
            readyPlayers.put(winner);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

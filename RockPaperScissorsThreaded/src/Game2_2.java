import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game2_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Enter the amount of players: ");
        int numPlayers = scanner.nextInt();

        System.out.println("Number of players = " + numPlayers);

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println(coreCount + " processor"
                + (coreCount != 1 ? "s are " : " is ")
                + "available");

        ExecutorService threadPool = Executors.newFixedThreadPool(coreCount);

        //Responsible for grabbing 2 threads to play against each other
        BlockingQueue<RPSThreadGame2> readyPlayers = new ArrayBlockingQueue<>(numPlayers);

        long start = System.currentTimeMillis();
        for(int i = 0; i < numPlayers; i++) {
            RPSThreadGame2 rpsThread = new RPSThreadGame2(readyPlayers);
            threadPool.execute(rpsThread);
            try {
                readyPlayers.put(rpsThread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        do {
            //We have 2 threads ready to be picked out and played
            if(readyPlayers.size() >= 2) {
                try {
                    RPSThreadGame2 firstPlayer = readyPlayers.take();
                    RPSThreadGame2 secondPlayer = readyPlayers.take();

                    System.out.println(firstPlayer.name + " vs " + secondPlayer.name);
                    RPSThreadGame2 winner = calculateWinner(firstPlayer, secondPlayer);

                    System.out.println(winner.name + " is victorious... Adding to the back of the queue");

                    readyPlayers.put(winner);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(readyPlayers.size() > 1);

        long end = System.currentTimeMillis();

        System.out.println("\n\n------------------------END GAME------------------------");
        assert readyPlayers.peek() != null;
        System.out.println(readyPlayers.peek().name + " is the victor of the tournament!!!");
        System.out.println("It took a total of " + (end-start) + "ms...");

        threadPool.shutdown();
    }


    public static RPSThreadGame2 calculateWinner(RPSThreadGame2 myself, RPSThreadGame2 opponent) {
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
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Game2_Future {
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

        List<Future<RPSThreadInfo_Future>> futures = new ArrayList<>();
        ArrayList<RPSThreadInfo_Future> readyPlayers = new ArrayList<>();

        long start = System.currentTimeMillis();
        for(int i = 0; i < numPlayers; i++) {
            RPSThreadGame2_Callable rpsThread = new RPSThreadGame2_Callable();
            futures.add(threadPool.submit(rpsThread));
        }

        for (Future<RPSThreadInfo_Future> gesture : futures) {
            try {
                RPSThreadInfo_Future value = gesture.get();
                readyPlayers.add(value);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        do {
            //We have 2 threads ready to be picked out and played
            if(readyPlayers.size() >= 2) {
                RPSThreadInfo_Future firstPlayer = readyPlayers.get(0);
                RPSThreadInfo_Future secondPlayer = readyPlayers.get(1);

                System.out.println(firstPlayer.getName() + " vs " + secondPlayer.getName());
                RPSThreadInfo_Future winner = calculateWinner(firstPlayer, secondPlayer);

                System.out.println(winner.getName() + " is victorious... Adding to the back of the queue");

                readyPlayers.remove(firstPlayer);
                readyPlayers.remove(secondPlayer);

                readyPlayers.add(winner);
            }
        }while(readyPlayers.size() > 1);

        long end = System.currentTimeMillis();

        System.out.println("\n\n------------------------END GAME------------------------");
        assert readyPlayers.get(0) != null;
        System.out.println(readyPlayers.get(0).getName() + " is the victor of the tournament!!!");
        System.out.println("It took a total of " + (end-start) + "ms...");

        threadPool.shutdown();
    }


    public static RPSThreadInfo_Future calculateWinner(RPSThreadInfo_Future myself, RPSThreadInfo_Future opponent) {
        if(myself.getGesture().equals(opponent.getGesture())) {
            boolean sameGesture = true;
            while(sameGesture) {
                myself.redoGesture();
                opponent.redoGesture();

                if(!myself.getGesture().equals(opponent.getGesture())) {
                    sameGesture = false;
                }
            }
        }

        String myGesture = myself.getGesture();
        String opponentGesture = opponent.getGesture();

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

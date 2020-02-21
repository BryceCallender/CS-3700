import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Enter the amount of players: ");
        int numPlayers = scanner.nextInt();

        System.out.println("Number of players = " + numPlayers);

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println(coreCount + " processor"
                + (coreCount != 1 ? "s are " : " is ")
                + "available");

        ArrayList<RPSThread2> players = new ArrayList<>(numPlayers);

        ExecutorService threadPool = Executors.newFixedThreadPool(coreCount);

        AtomicInteger numThreadsReady;

        long start = System.currentTimeMillis();
        do {
            players.clear();
            numThreadsReady = new AtomicInteger(0);

            System.out.println("----------------BEGINNING A RPS SESSION----------------");

            //Pick the gestures for the round
            for (int i = 0; i < numPlayers; i++) {
                RPSThread2 rpsThread = new RPSThread2(i, numThreadsReady);
                players.add(rpsThread);

                threadPool.execute(rpsThread);
            }

            while(numThreadsReady.get() != numPlayers) {

            }

            try {
                WinnerThread2 winnerThread = new WinnerThread2(players);
                winnerThread.start();
                winnerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            numPlayers--; //A player HAS to be removed so just decrement
        }while(numPlayers > 1);

        long end = System.currentTimeMillis();

        threadPool.shutdown();

        System.out.println("\n\n----------------END GAME----------------");
        System.out.println("The winner is " + players.get(0).name + " using the hand gesture " + players.get(0).handGesture);
        System.out.println("It took a total of " + (end-start) + "ms...");
    }
}

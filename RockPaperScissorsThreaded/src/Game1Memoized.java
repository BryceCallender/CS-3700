import java.util.*;
import java.util.concurrent.*;

public class Game1Memoized {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Enter the amount of players: ");
        int numPlayers = scanner.nextInt();

        System.out.println("Number of players = " + numPlayers);

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println(coreCount + " processor"
                + (coreCount != 1 ? "s are " : " is ")
                + "available");

        ArrayList<RPSThread> players = new ArrayList<>(numPlayers);
        List<Future<?>> futures = new ArrayList<>();

        ExecutorService threadPool = Executors.newFixedThreadPool(coreCount);

        System.out.println("----------------BEGINNING A RPS SESSION----------------");

        //Pick the gestures for the round
        for (int i = 0; i < numPlayers; i++) {
            RPSThread rpsThread = new RPSThread(i);
            players.add(rpsThread);

            Future<?> f = threadPool.submit(rpsThread);
            futures.add(f);
        }

        long start = System.currentTimeMillis();
        do {

            for (Future<?> future : futures) {
                try {
                    future.get(); // get will block until the future is done
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            futures.clear();

            try {
                WinnerThreadMemoized winnerThread = new WinnerThreadMemoized(players);
                winnerThread.start();
                winnerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            numPlayers--; //A player HAS to be removed so just decrement

            if(numPlayers > 1) {
                System.out.println("----------------BEGINNING A RPS SESSION----------------");
                for (RPSThread player : players) {
                    Future<?> f = threadPool.submit(player);
                    futures.add(f);
                }
            }
        }while(numPlayers > 1);

        long end = System.currentTimeMillis();

        threadPool.shutdown();

        System.out.println("\n\n----------------END GAME----------------");
        System.out.println("The winner is " + players.get(0).name + " using the hand gesture " + players.get(0).handGesture);
        System.out.println("It took a total of " + (end-start) + "ms...");
    }
}

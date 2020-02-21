import java.util.*;
import java.util.concurrent.*;

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

        long start = System.currentTimeMillis();
        do {
            CyclicBarrier cyclicBarrier = new CyclicBarrier(numPlayers, new WinnerThread2(players));
            players.clear();

            System.out.println("----------------BEGINNING A RPS SESSION----------------");

            //Pick the gestures for the round
            for (int i = 0; i < numPlayers; i++) {
                RPSThread2 rpsThread = new RPSThread2(i, cyclicBarrier);
                players.add(rpsThread);

                threadPool.execute(rpsThread);
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

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please Enter the amount of players: ");
        int numPlayers = scanner.nextInt();

        System.out.println("Number of players = " + numPlayers);

        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(processors + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");

        ArrayList<RPSThread> players = new ArrayList<>();

        ExecutorService threadPool = Executors.newFixedThreadPool(processors);

        for(int i = 0; i < numPlayers; i++) {
            RPSThread rpsThread = new RPSThread();
            players.add(rpsThread);
            threadPool.submit(new RPSThread());
        }

        System.out.println(players.size());

        threadPool.shutdown();
    }
}

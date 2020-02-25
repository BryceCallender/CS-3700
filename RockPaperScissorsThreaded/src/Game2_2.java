import java.util.Scanner;
import java.util.concurrent.*;

public class Game2_2 {
    public static void main(String[] args) throws InterruptedException {
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
        BlockingQueue<RPSThread2Game2> readyPlayers = new ArrayBlockingQueue<>(numPlayers);

        CountDownLatch countDownLatch = new CountDownLatch(numPlayers);

        long start = System.currentTimeMillis();

        for(int i = 0; i < numPlayers; i++) {
            RPSThread2Game2 rpsThread = new RPSThread2Game2(i, readyPlayers, countDownLatch);
            threadPool.execute(rpsThread);
        }

        countDownLatch.await();

        Thread winnerThread = new Thread(new WinnerBarrierAction(readyPlayers));

        winnerThread.start();
        winnerThread.join();

        long end = System.currentTimeMillis();

        System.out.println("\n\n------------------------END GAME------------------------");
        assert readyPlayers.peek() != null;
        System.out.println(readyPlayers.peek().name + " is the victor of the tournament!!!");
        System.out.println("It took a total of " + (end-start) + "ms...");

        threadPool.shutdown();
    }
}

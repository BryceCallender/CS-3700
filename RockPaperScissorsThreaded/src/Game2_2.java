import java.util.Scanner;
import java.util.concurrent.*;

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
        BlockingQueue<RPSThread2Game2> readyPlayers = new ArrayBlockingQueue<>(numPlayers);
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            try {
                if(readyPlayers.size() == 1) {
                    System.out.println(readyPlayers.peek().name + " is the victor of the tournament!!!");
                    //System.out.println("It took a total of " + (end-start) + "ms...");
                }

                RPSThread2Game2 firstPlayer = readyPlayers.take();
                RPSThread2Game2 secondPlayer = readyPlayers.take();

                System.out.println(firstPlayer.name + " vs " + secondPlayer.name);
                RPSThread2Game2 winner = calculateWinner(firstPlayer, secondPlayer);

                System.out.println(winner.name + " is victorious... Adding to the back of the queue");

                addPlayerAndAwait(readyPlayers, winner);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long start = System.currentTimeMillis();

        for(int i = 0; i < numPlayers; i++) {
            RPSThread2Game2 rpsThread = new RPSThread2Game2(i, readyPlayers, barrier);
            threadPool.execute(rpsThread);
        }

        long end = System.currentTimeMillis();

        System.out.println("\n\n------------------------END GAME------------------------");
//        assert readyPlayers.peek() != null;
//        System.out.println(readyPlayers.peek().name + " is the victor of the tournament!!!");
        System.out.println("It took a total of " + (end-start) + "ms...");

        threadPool.shutdown();
    }

    public static void addPlayerAndAwait(BlockingQueue<RPSThread2Game2> readyPlayers, RPSThread2Game2 winner) {
        try {
            readyPlayers.put(winner);
            System.out.println("there are " + winner.barrier.getNumberWaiting() + " waiting");
            winner.awaitOpponent();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static RPSThread2Game2 calculateWinner(RPSThread2Game2 myself, RPSThread2Game2 opponent) {
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

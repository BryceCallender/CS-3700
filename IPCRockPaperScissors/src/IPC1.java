import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

class Player1 {
    final int portNumber = 8000;
    ServerSocket serverSocket;

    final int player2Port = 8001;
    final int player3Port = 8002;

    int numGames;
    int points;
    Random random;

    String[] values = { "Rock", "Paper", "Scissors" };

    Player1(int numGames) {
        this.numGames = numGames;
        this.points = 0;
        this.random = new Random();

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startUp() {
        for(int i = 0; i < numGames; i++) {
            if(i > 0) {
                System.out.println();
                System.out.println();
            }

            System.out.println("Playing Game " + (i+1));

            String hand = values[random.nextInt(values.length)];

            System.out.println("Player1 picked: " + hand);

            try {
//                Socket player2 = new Socket();
//                boolean socketIsConnected = false;
//                while(!socketIsConnected) {
//                    try {
//                        player2 = new Socket(InetAddress.getLocalHost(), player2Port);
//                        socketIsConnected = true;
//                    }
//                    catch (IOException e) {
//                        //System.out.println("Failed connection trying again...");
//                    }
//                }
//
//                System.out.println("Sending player1's hand to player2...");
//
//                PrintWriter player2Out = new PrintWriter(player2.getOutputStream(), true);
//
//                player2Out.println(hand);
//
//
//                Socket player3 = new Socket();
//                socketIsConnected = false;
//                while(!socketIsConnected) {
//                    try {
//                        player3 = new Socket(InetAddress.getLocalHost(), player3Port);
//                        socketIsConnected = true;
//                    }
//                    catch (IOException e) {
//                        //System.out.println("Failed connection trying again...");
//                    }
//                }
//
//                System.out.println("Sending player1's hand to player3...");
//
//                PrintWriter player3Out = new PrintWriter(player3.getOutputStream(), true);
//
//                player3Out.println(hand);

                Socket socket1 = serverSocket.accept(); //player2 response
                Socket socket2 = serverSocket.accept(); //player3 response

                System.out.println("Sending player1's hand to player2...");
                PrintWriter player2Out = new PrintWriter(socket1.getOutputStream(), true);
                player2Out.println(hand);

                System.out.println("Sending player1's hand to player3...");
                PrintWriter player3Out = new PrintWriter(socket2.getOutputStream(), true);
                player3Out.println(hand);

                BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

                String player2Hand = in1.readLine();
                String player3Hand = in2.readLine();

                System.out.println("Player2 played: " + player2Hand);
                System.out.println("Player3 played: " + player3Hand);

                if(checkGestures(hand, player2Hand) && checkGestures(hand, player3Hand)) {
                    points += 2;
                }
                else if((checkGestures(hand, player2Hand) && checkGestures(player3Hand, player2Hand))
                || checkGestures(hand, player3Hand) && checkGestures(player2Hand, player3Hand)) {
                    points++;
                }

                System.out.println("Round finished...");
                System.out.println("Player1 points: " + points);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkGestures(String myGesture, String opponentGesture) {
        switch (myGesture) {
            case "Rock":
                switch (opponentGesture) {
                    case "Rock":
                    case "Paper":
                        return false;
                    case "Scissors":
                        return true;
                }
                break;
            case "Paper":
                switch (opponentGesture) {
                    case "Rock":
                        return true;
                    case "Paper":
                    case "Scissors":
                        return false;
                }
                break;
            case "Scissors":
                switch (opponentGesture) {
                    case "Rock":
                    case "Scissors":
                        return false;
                    case "Paper":
                        return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }
}

public class IPC1 {
    public static void main(String[] args) {
        if(args.length > 0) {
            int numGames = Integer.parseInt(args[0]);

            Player1 player1 = new Player1(numGames);
            player1.startUp();
        } else {
            System.err.println("You must specify a number of games to play");
            System.exit(1);
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Random;

class Player2 {
    final int portNumber = 8001;
    ServerSocket serverSocket;

    final int player1Port = 8000;
    final int player3Port = 8002;

    int numGames;
    int points;
    Random random;

    String[] values = { "Rock", "Paper", "Scissors" };

    Player2(int numGames) {
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

            System.out.println("Player2 picked: " + hand);

            try {
                Socket player1 = new Socket();
                boolean socketIsConnected = false;
                while(!socketIsConnected) {
                    try {
                        player1 = new Socket(InetAddress.getLocalHost(), player1Port);
                        socketIsConnected = true;
                    }
                    catch (IOException e) {
                        //System.out.println("Failed connection trying again...");
                    }
                }

                System.out.println("Sending player2's hand to player1...");

                PrintWriter player1Out = new PrintWriter(player1.getOutputStream(), true);

                player1Out.println(hand);


                Socket player3 = new Socket();
                socketIsConnected = false;
                while(!socketIsConnected) {
                    try {
                        player3 = new Socket(InetAddress.getLocalHost(), player3Port);
                        socketIsConnected = true;
                    }
                    catch (IOException e) {
                        //System.out.println("Failed connection trying again...");
                    }
                }

                System.out.println("Sending player2's hand to player3...");

                PrintWriter player3Out = new PrintWriter(player3.getOutputStream(), true);

                player3Out.println(hand);

                Socket socket1 = serverSocket.accept(); //player1 response
                Socket socket2 = serverSocket.accept(); //player3 response

                BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

                String player1Hand = in1.readLine();
                String player3Hand = in2.readLine();

                System.out.println("Player1 played: " + player1Hand);
                System.out.println("Player3 played: " + player3Hand);

                if(checkGestures(hand, player1Hand) && checkGestures(hand, player3Hand)) {
                    points += 2;
                }
                else if((checkGestures(hand, player1Hand) && checkGestures(player3Hand, player1Hand))
                        || checkGestures(hand, player3Hand) && checkGestures(player1Hand, player3Hand)) {
                    points++;
                }

                System.out.println("Round finished...");
                System.out.println("Player2 points: " + points);
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

public class IPC2 {
    public static void main(String[] args) {
        if(args.length > 0) {
            int numGames = Integer.parseInt(args[0]);

            Player2 player2 = new Player2(numGames);
            player2.startUp();
        } else {
            System.err.println("You must specify a number of games to play");
            System.exit(1);
        }
    }
}

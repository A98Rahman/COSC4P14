import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Server {
    static final int port = 4000;
    public static void main(String[] args) {

//        ServerSocket serverSocket
        Socket playerSocket1 = null;
        Socket playerSocket2 = null;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                System.out.println("Client hit me!");
                playerSocket1 = new Socket();
                playerSocket1 = serverSocket.accept();
                System.out.println("Player 1 connected, waiting for player 2");

                playerSocket2 = new Socket();
                playerSocket2 = serverSocket.accept();
                playerSocket2 = serverSocket.accept();
                System.out.println("Player 2 connected, starting game...");

                OutputStream toPLayer1 = playerSocket1.getOutputStream();
                OutputStream toPLayer2 = playerSocket2.getOutputStream();

                BufferedReader fromPlayer1 = new BufferedReader(new InputStreamReader(playerSocket1.getInputStream()));
                BufferedReader fromPlayer2 = new BufferedReader(new InputStreamReader(playerSocket2.getInputStream()));

                GameLogic game = new GameLogic();

                while(playerSocket1 !=null && playerSocket2 !=null){
                   // game.start();
                    // header: |String gameboard|enum playerID|int win|int validMove|String message|
                    Scanner input = new Scanner(System.in);
                    GameLogic.PLAYERID currID = GameLogic.PLAYERID.RED;
                    boolean isGameOver = false;
                        while (!isGameOver){
                            int validMoveFlag = game.playerMove(currID,input,game); //valid move To get the
                            String invalidMoveMsg = GameLogic.validationMessage[validMoveFlag];// inValid move error message
                            isGameOver = game.isWinningMove(currID);//win flag
                            String board = game.printGameBoard(); //gameboard


                            currID = currID==GameLogic.PLAYERID.RED?GameLogic.PLAYERID.BLUE:GameLogic.PLAYERID.RED; //Last thing that happens in the loop
                            }
                }

            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

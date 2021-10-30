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

                System.out.println("Player 2 connected, starting game...");

                GameLogic game = new GameLogic();
                Player playerRed = null;
                Thread redPlayer = null;
                Player playerBlue = null;
                Thread bluePlayer = null;

                while (playerSocket1 == null) { //Wait until a Player/socket is connected.
                    playerSocket1 = serverSocket.accept(); //Red

                    playerRed = new Player(GameLogic.PLAYERID.RED, playerSocket1, game);
                    redPlayer = new Thread(playerRed);
                    synchronized (redPlayer) {
                        redPlayer.start();
                        redPlayer.wait();
                    }
                }

                while (playerSocket2 == null) { //Wait until Player blue is also connected.
                    playerSocket2 = serverSocket.accept();
                    playerBlue = new Player(GameLogic.PLAYERID.BLUE, playerSocket2, game);
                    bluePlayer = new Thread(playerBlue);
                    synchronized (bluePlayer) {
                        bluePlayer.start();
                        bluePlayer.wait();
                    }
                }
                Thread curr = redPlayer;


                while(playerSocket1 !=null && playerSocket2 !=null){ //if the players are not connected then stop the progression
                    //game.start();
                    // header: |String gameboard|String playerID|int win|int validMove|String message|
//                    Scanner input = new Scanner(System.in);
                    GameLogic.PLAYERID currID = GameLogic.PLAYERID.RED;
                    Player currPlayer = playerRed;

                    boolean isGameOver = false;
                        while (!isGameOver) {

                     //       ConnectHeader ch = null;

                            try {
                                if (currID == GameLogic.PLAYERID.BLUE) {
                                    synchronized (bluePlayer) {
                                        bluePlayer.wait();
                                        bluePlayer.notify(); //This makes the red players thread run
                                    }
                                } else{
                                synchronized (redPlayer) {
                                    redPlayer.wait();
                                    redPlayer.notify();
                                }
                            }
                        }catch (InterruptedException ie){
                            ie.printStackTrace();
                    }

                            isGameOver = game.isWinningMove(currID);//win flag
                            String board = game.printGameBoard(); //gameboard
                            String newMessage= "Awaiting opponents turn";
                            if(currID == GameLogic.PLAYERID.RED){
                            }else{

                            }

                            currID = currID==GameLogic.PLAYERID.RED?GameLogic.PLAYERID.BLUE:GameLogic.PLAYERID.RED; // Changes the current player
                        }
                }

            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void printHeader (ConnectHeader c) {
        System.out.println(c.getgB());
        System.out.print(c.getpID()+", "+c.getwF()+", "+c.getvF()+", "+c.getM());
        System.out.println();
    }

    public static byte[] pack (ConnectHeader ch) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(ch);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                baos.close();
                oos.close();
            } catch (IOException ioe) {
                System.out.println("Issue on pack resource close");
            }
        }
        return baos.toByteArray();
    }

    public static ConnectHeader unpack (byte[] pkg) {
        ByteArrayInputStream bais = new ByteArrayInputStream(pkg);
        ObjectInput objIn = null;
        ConnectHeader cH = null;
        try {
            objIn = new ObjectInputStream(bais);
            cH = (ConnectHeader) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
                objIn.close();
            } catch (IOException ioException) {
                System.out.println("Issue on unpack resource close");
            }
        }
        return cH;
    }
}

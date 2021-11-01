import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Server {

    public static void main(String[] args) {
        Server server = new Server();
    }

    static final int port = 4000;
    private int numConnections = 0;
    private ServerPlayerConnection redPlayer;
    private ServerPlayerConnection bluePlayer;
    private String redID = GameLogic.PLAYERID.RED.toString();
    private String blueID = GameLogic.PLAYERID.BLUE.toString();
    private GameLogic game;
    GameLogic.PLAYERID currentPlayer;

    public Server () {
        Socket playerSocket1 = null;
        Socket playerSocket2 = null;
        currentPlayer = GameLogic.PLAYERID.RED;
        game = new GameLogic();

        try (
                ServerSocket serverSocket = new ServerSocket(port)
        ) {
            ///while (true) {
            System.out.println("---Server open for connections---");

            // accept connections from 2 clients
            try {
                do {
                    Socket newSocket = serverSocket.accept();
                    numConnections++;
                    ServerPlayerConnection p = null;
                    switch (numConnections) {
                        case 1:
                            p = new ServerPlayerConnection(GameLogic.PLAYERID.RED,newSocket,game);
                            redPlayer = p;
                            redPlayer.msgPlayer(infoMsgOnly(redID,"You are the RED player, you go first"));
                            break;
                        case 2:
                            p = new ServerPlayerConnection(GameLogic.PLAYERID.BLUE,newSocket,game);
                            bluePlayer = p;
                            bluePlayer.msgPlayer(infoMsgOnly(blueID,"You are the BLUE player, you go second"));
                            break;
                        default:
                            System.out.println("error in connecting new player");
                    }
                    System.out.printf("Player %d connected%n",numConnections);
//                        Thread playerThread = new Thread(p);
//                        playerThread.start();
                } while (numConnections < 2);
                System.out.println("Players connected, starting game");
                redPlayer.msgPlayer(infoMsgOnly(redID,"Both players ready, your turn!"));
                bluePlayer.msgPlayer(infoMsgOnly(redID,"Both players ready, red's move"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            //Start game
            Thread redThread = new Thread(redPlayer);
            redThread.start();
            Thread blueThread = new Thread(bluePlayer);
            blueThread.start();

//            while(playerSocket1 !=null && playerSocket2 !=null){ //if the players are not connected then stop the progression
//                //game.start();
//                // header: |String gameboard|String playerID|int win|int validMove|String message|
////                    Scanner input = new Scanner(System.in);
//                GameLogic.PLAYERID currID = GameLogic.PLAYERID.RED;
////                    Player currPlayer = playerRed;
//
//                boolean isGameOver = false;
//                while (!isGameOver) {
//
//                    //       ConnectHeader ch = null;
//
//                    try {
//                        if (currID == GameLogic.PLAYERID.BLUE) {
//                            synchronized (game) {
//                                blueThread.wait();
//                                blueThread.notify(); //This makes the red players thread run
//                            }
//                        } else{
//                            synchronized (game) {
//                                redThread.wait();
//                                redThread.notify();
//                            }
//                        }
//                    }catch (InterruptedException ie){
//                        ie.printStackTrace();
//                    }
//
//                    isGameOver = game.isWinningMove(currID);//win flag
//                    if(isGameOver){
//                    break;
//                    }
//                    String board = game.printGameBoard(); //gameboard
//                    String newMessage= "Awaiting opponents turn";
//                    if(currID == GameLogic.PLAYERID.RED){
//                    }else{
//
//                    }
//
//                    currID = currID==GameLogic.PLAYERID.RED?GameLogic.PLAYERID.BLUE:GameLogic.PLAYERID.RED; // Changes the current player
//                }
//            }


        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private ConnectHeader infoMsgOnly (String playerID, String msg) {
        return new ConnectHeader ("",playerID,-1,-1,msg);
    }

    private class ServerPlayerConnection implements Runnable {

        Socket playerSocket;
        ObjectInputStream input;
        ObjectOutputStream output;

        public GameLogic.PLAYERID playerID;
        GameLogic game;

        public ServerPlayerConnection(GameLogic.PLAYERID playerID, Socket playerSocket, GameLogic game) throws IOException {

            //initialize resources
            this.playerID = playerID;
            this.playerSocket = playerSocket;
            this.game = game;

            //get streams
            try {
                this.output = new ObjectOutputStream(playerSocket.getOutputStream());
                this.input = new ObjectInputStream(playerSocket.getInputStream());
            } catch (IOException ioe) {
                System.out.println("IOException on Player.run()");
                ioe.printStackTrace();
            }

            System.out.printf("Connected %s on port: %d%n",playerID.toString(),playerSocket.getPort());
        }

        public void run(){
            try {
                if (playerID == GameLogic.PLAYERID.RED) {
                    msgPlayer(new ConnectHeader(game.getGameBoard(),redID,0,-1,"Your turn RED"));
                }


                while (true) {
                    ConnectHeader msgIn = (ConnectHeader) input.readObject();
                    if (playerID == GameLogic.PLAYERID.RED) {
                        System.out.println(msgIn.getM());
                        bluePlayer.msgPlayer(new ConnectHeader(game.getGameBoard(),blueID,0,-1,"Your turn BLUE"));
                    } else {
                        System.out.println(msgIn.getM());
                        redPlayer.msgPlayer(new ConnectHeader(game.getGameBoard(),redID,0,-1,"Your turn RED"));
                    }
                }
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        public void msgPlayer (ConnectHeader header) {
            try {
                output.writeObject(header);
                output.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
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

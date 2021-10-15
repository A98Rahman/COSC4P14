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
                playerSocket1 = serverSocket.accept(); //Red
                System.out.println("Player 1 connected, waiting for player 2");
                ObjectOutputStream toPLayer1 = new ObjectOutputStream(playerSocket1.getOutputStream());
                ObjectInputStream fromPlayer1 = new ObjectInputStream(playerSocket1.getInputStream());

                toPLayer1.writeObject(new ConnectHeader(null,null,0,-1,"You are RED, waiting for BLUE"));

                playerSocket2 = new Socket();
                playerSocket2 = serverSocket.accept(); //Blue
                //playerSocket2 = serverSocket.accept();
                System.out.println("Player 2 connected, starting game...");

                ObjectOutputStream toPLayer2 = new ObjectOutputStream(playerSocket2.getOutputStream());
                ObjectInputStream fromPlayer2 = new ObjectInputStream(playerSocket2.getInputStream());

                toPLayer1.writeObject(new ConnectHeader(null,null,0,-1,"BLUE connected, starting game"));
                toPLayer2.writeObject(new ConnectHeader(null,null,0,-1,"You are BLUE, RED is ready, starting game...RED starts"));
//                BufferedReader fromPlayer1 = new BufferedReader(new InputStreamReader(playerSocket1.getInputStream()));
//                BufferedReader fromPlayer2 = new BufferedReader(new InputStreamReader(playerSocket2.getInputStream()));

                GameLogic game = new GameLogic();

                while(playerSocket1 !=null && playerSocket2 !=null){
                    //game.start();
                    // header: |String gameboard|String playerID|int win|int validMove|String message|
//                    Scanner input = new Scanner(System.in);
                    GameLogic.PLAYERID currID = GameLogic.PLAYERID.RED;
                    ObjectInputStream currPlayerInput;
                    ObjectOutputStream currPlayerOutput;
                    boolean isGameOver = false;
                        while (!isGameOver){
                            //String move = fromPlayer1.readLine();
                           // currPlayerInput = (currID==GameLogic.PLAYERID.RED)?fromPlayer1:fromPlayer2;
                            if (currID == GameLogic.PLAYERID.RED) {
                                currPlayerInput = fromPlayer1;
                                currPlayerOutput = toPLayer1;
                            } else {
                                currPlayerInput = fromPlayer2;
                                currPlayerOutput = toPLayer2;
                            }
                            int validMoveFlag=0;
                            ConnectHeader clientResponse = null;
                            String message = "Your turn";
                            String invalidMsg="";
                            do{ //Keeps asking for moves until a valid move is played
                                        //Sending the packet
                                        ConnectHeader serverResponse = new ConnectHeader(game.getGameBoard(),currID.toString(),0,-1,"Your turn "+invalidMsg);
                                        currPlayerOutput.writeObject(serverResponse);
                                        clientResponse = (ConnectHeader)currPlayerInput.readObject();
                                        validMoveFlag = game.validateAndPlay(clientResponse.getM(),currID);
                                        invalidMsg = (validMoveFlag>0?GameLogic.validationMessage[validMoveFlag]:"");
                                        // -1: valid, 0: invalid index, 1: invalid move
                              }
                              while (validMoveFlag > -1);


                            //int validMoveFlag = game.playerMove(currID,currPlayerInput,game); //valid move To get the
//                            String invalidMoveMsg = GameLogic.validationMessage[validMoveFlag];// inValid move error message
                            isGameOver = game.isWinningMove(currID);//win flag
                            String board = game.printGameBoard(); //gameboard
                            String newMessage= "Player "+currID.toString()+"'s turn...";
                            ConnectHeader ServerResponse2 = new ConnectHeader(board,currID.toString(),(isGameOver?1:0),validMoveFlag,newMessage);
                            //byte[] pack = pack(ch);
                            currPlayerOutput.writeObject(ServerResponse2);

                            //if(currID == GameLogic.PLAYERID.RED){
                            //public ConnectHeader (String gameBoard, String playerID, int winFlag, int validationFlag, String message)
                            //    toPLayer1.writeObject(ch);
                            //}else{
                            //    toPLayer2.writeObject(ch);
                            //}

                            currID = currID==GameLogic.PLAYERID.RED?GameLogic.PLAYERID.BLUE:GameLogic.PLAYERID.RED; //Last thing that happens in the loop
                        }
                }

            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }catch(ClassNotFoundException c){
            System.err.println("Server exception: FATAL ERROR " + c.getMessage());
        }

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

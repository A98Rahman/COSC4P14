import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerPlayer implements Runnable {

    public GameLogic.PLAYERID playerID;
    Socket playerSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    GameLogic game;

    public ServerPlayer(GameLogic.PLAYERID playerID, Socket playerSocket, GameLogic game) throws IOException {

        this.playerID = playerID;
        this.playerSocket = playerSocket;
        this.game = game;
        try {
            this.output = new ObjectOutputStream(playerSocket.getOutputStream());
            this.input = new ObjectInputStream(playerSocket.getInputStream());
        } catch (IOException ioe) {
            System.out.println("IOException on Player.run()");
            ioe.printStackTrace();
        }

        System.out.println("Connected on port: "+playerSocket.getPort());
    }

    public void run(){
        try {
//            ConnectHeader ch = null;
//            while (ch==null) {
//                if (playerID == GameLogic.PLAYERID.RED)
//                    this.send("You are red, waiting for blue", -1, -1);
//                else
//                    this.send("You are blue, reds turn", -1, -1);
//                ch = getResponse("Your turn ");
//                System.out.println("DEBUG HERE");
//            }
            output.writeObject(playerID);
            output.flush();

            while(true) {
                if (playerID == GameLogic.PLAYERID.RED) {

                } else {

                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
//        catch(ClassNotFoundException c){
//            c.printStackTrace();
//        }
    }

    public void updateGameState (GameLogic update) {
        this.game = update;
    }

    public void gameWinner(String message , ObjectOutputStream output, GameLogic game) throws IOException {
        ConnectHeader ServerResponse2 = new ConnectHeader(game.printGameBoard(),playerID.toString(),(1),0,message);
        output.writeObject(ServerResponse2);

    }

    public void send(String message, int winFlag , int validationFlag) throws IOException {
        ConnectHeader headerToClient = new ConnectHeader(game.getGameBoard(), playerID.toString(),winFlag,validationFlag,message);
        output.writeObject(headerToClient);
        output.flush();
    }

//    public ConnectHeader getResponse() throws IOException, ClassNotFoundException {
//
//        ConnectHeader headerFromClient = (ConnectHeader)input.readObject();
//        return headerFromClient;
//    }

    public GameLogic.PLAYERID getPlayerID(){
        return  this.playerID;
    }


    public synchronized ConnectHeader getResponse(String message) throws IOException, ClassNotFoundException {
        //String message = "Your turn";
        String invalidMsg="";
        String move;
        ConnectHeader headerFromClient;
        int validMoveFlag = -1;
        do{ //Keeps asking for moves until a valid move is played
            //Sending the packet to the client initially, each iteration a new packet is sent to the client with flags and a message
            if(validMoveFlag<0)
                send(message,0,validMoveFlag);
            else
                send("Invalid move try again.",0,validMoveFlag);

            //            ConnectHeader headerToClient = new ConnectHeader(game.getGameBoard(),playerID.toString(),0,-1,"Your turn "+invalidMsg);
//            output.writeObject(headerToClient);
//            output.flush();
//            output.reset();

            headerFromClient = (ConnectHeader)input.readObject(); //Input waiting
//                              //Response from the client

            validMoveFlag = game.validateAndPlay(headerFromClient.getM(),playerID);
            invalidMsg = (validMoveFlag>=0?GameLogic.validationMessage[validMoveFlag]:""); //The syntax error for the command
            // -1: valid, 0: invalid index, 1: invalid move
        }
        while (validMoveFlag > -1);

        return headerFromClient;
    }


}



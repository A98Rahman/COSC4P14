import java.io.*;
import java.net.*;
import java.util.Date;
public class Client {
    public static void main(String[] args) {

        int port = 4000;
        ObjectOutputStream outputToServer;
        ObjectInputStream inputFromServer;
        Socket clientSocket = null;
        String playerID = null;
        boolean gameOver = false;

        try {
            clientSocket = new Socket("127.0.0.1",port);
            System.out.println("Client Connection Succesful");

            //BufferedReader fromServerReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Gets the server output as input to the client.
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in)); //Accept input from the user
            inputFromServer = new ObjectInputStream(clientSocket.getInputStream());
            outputToServer = new ObjectOutputStream(clientSocket.getOutputStream()); //Gets the output stream that outputs the data to the server

//            PrintWriter writer = new PrintWriter(outputToServer, true); //This writer object will write and send the data to the server.

            String message = "";

            while (!gameOver) {
                ConnectHeader gameHeader = (ConnectHeader)inputFromServer.readObject();
//                System.out.println("Header data");
//                System.out.println(gameHeader.getgB());
//                System.out.println(gameHeader.getpID());
//                System.out.println(gameHeader.getwF());
//                System.out.println(gameHeader.getvF());
//                System.out.println(gameHeader.getM());

                if (gameHeader.getwF() == 1) {
                    System.out.println(gameHeader.getgB());
                    System.out.println(gameHeader.getM());
                    gameOver = true;
                }
                else if (gameHeader.getwF() == -1) {
                    if (playerID == null) playerID = gameHeader.getpID();
                    System.out.println(gameHeader.getM());
//                    System.out.println("set player id");
                }
                else if (gameHeader.getpID().equals(playerID)) {
//                    System.out.println("reached your turn");
                    System.out.println(gameHeader.getgB());
                    System.out.println(gameHeader.getM());
//                    System.out.println("waiting for user input");
                    message = userInputReader.readLine();
                    if (!message.equals("quit")) { //Had to exit the loop. The connection is first terminated from server side.
                        outputToServer.writeObject(
                                new ConnectHeader("", "", 0, -1, message)
                        );
                        outputToServer.flush();outputToServer.reset();
                    }
                }
                else {
                    System.out.println("Something went wrong");
                    System.out.println(gameHeader.getgB());
                    System.out.println(gameHeader.getpID());
                    System.out.println(gameHeader.getwF());
                    System.out.println(gameHeader.getvF());
                    System.out.println(gameHeader.getM());
                }
            }




               // ConnectHeader cH = unpack(fromServerReader.readLine());
//               ConnectHeader cH = (ConnectHeader)inputFromServer.readObject();
//               System.out.println(cH.getgB());
//               String[] msgs = cH.getM().split(",");
//               for(String msg : msgs){
//                    System.out.println(msg);
//               }
//
//                //String response = fromServerReader.readLine();
//                //System.out.println(response);
//                message = userInputReader.readLine();
//                //writer.println(message);
//
//                ConnectHeader clientResponse = new ConnectHeader(cH.getgB(),cH.getpID(),cH.getwF(),cH.getvF(),message);
//                outputToServer.writeObject(clientResponse);
//
//                outputToServer.flush();

            //String response = fromServerReader.readLine();
            //System.out.println(response + '\t' +"Connection closed by the host");

            userInputReader.close();
            //fromServer.close();
            outputToServer.close();
            clientSocket.close();


        } catch (IOException ex) {
            System.out.println("Input exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException c){
            System.err.println("Server exception: FATAL ERROR " + c.getMessage());

        }
    }

//    public static byte[] pack (ConnectHeader ch) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = null;
//        try {
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(ch);
//            oos.flush();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
//            try {
//                baos.close();
//                oos.close();
//            } catch (IOException ioe) {
//                System.out.println("Issue on pack resource close");
//            }
//        }
//        return baos.toByteArray();
//    }
//
//    public static ConnectHeader unpack (byte[] pkg) {
//        ByteArrayInputStream bais = new ByteArrayInputStream(pkg);
//        ObjectInput objIn = null;
//        ConnectHeader cH = null;
//        try {
//            objIn = new ObjectInputStream(bais);
//            cH = (ConnectHeader) objIn.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bais.close();
//                objIn.close();
//            } catch (IOException ioException) {
//                System.out.println("Issue on unpack resource close");
//            }
//        }
//        return cH;
//    }

}


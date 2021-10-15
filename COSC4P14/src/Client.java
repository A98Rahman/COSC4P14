import java.io.*;
import java.net.*;
import java.util.Date;
public class Client {
    public static void main(String[] args) {

        int port = 4000;
        ObjectOutputStream outputToServer;
        ObjectInputStream inputFromServer;
        Socket clientSocket = null;
        String playerID;
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

            ConnectHeader cH = (ConnectHeader)inputFromServer.readObject();
            playerID = cH.getpID();
            System.out.println(cH.getM());

            while (!gameOver) {
                cH = (ConnectHeader)inputFromServer.readObject();
                System.out.println();

                if (cH.getwF() == 1) {
                    System.out.println(cH.getgB());
                    System.out.println(cH.getM());
                    gameOver = true;
                }
                else if (cH.getpID().equals(playerID)) {
                    System.out.println(cH.getgB());
                    System.out.println(cH.getM());
                    message = userInputReader.readLine();
                    if (!message.equals("quit")) { //Had to exit the loop. The connection is first terminated from server side.
                        outputToServer.writeObject(
                                new ConnectHeader("", "", 0, -1, message)
                        );
                        outputToServer.flush();
                    }
                }
                else {
                    System.out.println("Something went wrong");
                    System.out.println(cH.getgB());
                    System.out.println(cH.getpID());
                    System.out.println(cH.getwF());
                    System.out.println(cH.getvF());
                    System.out.println(cH.getM());
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


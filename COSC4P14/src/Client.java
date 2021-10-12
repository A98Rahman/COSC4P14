import java.io.*;
import java.net.*;
import java.util.Date;
public class Client {
    public static void main(String[] args) {

        int port = 4000;
        DataOutputStream output = null;
        DataInputStream input = null;
        Socket clientSocket = null;

        try {
            clientSocket = new Socket("127.0.0.1",port);
            System.out.println("Client Connection Succesful");

            BufferedReader serverInputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Gets the server output as input to the client.
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

            output = new DataOutputStream(clientSocket.getOutputStream()); //Gets the output stream that outputs the data to the server
            PrintWriter writer = new PrintWriter(output, true); //This writer object will write and send the data to the server.

            String message = "";

            while(!message.equals("quit")){ //Had to exit the loop. The connection is first terminated from server side.

                message = inputReader.readLine();
                writer.println(message);
                String response = serverInputReader.readLine();
                System.out.println(response );

                output.flush();
            }

            String response = serverInputReader.readLine();
            System.out.println(response + '\t' +"Connection closed by the host");

            inputReader.close();
            serverInputReader.close();
            output.close();
            clientSocket.close();


        } catch (IOException ex) {
            System.out.println("Input exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}


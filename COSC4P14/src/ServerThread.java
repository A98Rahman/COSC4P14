import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

public class ServerThread extends Thread{
    public Socket socket;

    public ServerThread(Socket client) {
        this.socket = client; // Instantiate the socket object we get from MultiServer class
    }


    public void run() {

        try {

            OutputStream output = socket.getOutputStream();

            PrintWriter writer = new PrintWriter(output, true);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Gets the input from clients
            String message = "";
            while (!message.equals("quit")) { // Keep going until the client sends a quit command
                message = inputReader.readLine();
                System.out.println(message.toUpperCase() + '\t' + this.socket.getPort());
                writer.println("Message form server: " + message.toUpperCase() + '\t' + this.socket.getPort());
            }

            //These lines run when the client send a quit command to the server.
            writer.println(new Date().toString());
            inputReader.close();
            output.close();
            this.socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}

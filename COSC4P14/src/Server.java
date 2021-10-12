import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static final int port = 4000;
    public static void main(String[] args) {


        Socket socket = null;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                System.out.println("Client hit me!");

                socket = serverSocket.accept();
                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

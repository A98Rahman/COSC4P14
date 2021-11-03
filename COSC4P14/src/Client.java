import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }

    private ClientNetworking cNet;
    private String playerID;
    private boolean yourTurn;

    public Client () throws IOException {
        String playerID = null;
        boolean gameOver = false;
        byte[] ipAddr = new byte[]{127,0,0,1};
        byte[] buf= new byte[1024];


        System.out.println("---Connecting to server---");
        boolean connect2Udp = true;
        Scanner input = new Scanner(System.in);
        do{
            System.out.println("Press 1 to play \n press 2 to download game history");
            int in = input.nextInt();

            if(in == 2){
//                byte[] pingBuf = "ping52".getBytes();
                DatagramSocket udpSocket = new DatagramSocket();
                RDTReciever rcvr = new RDTReciever(udpSocket);
                Thread rcvThread = new Thread(rcvr);
                rcvThread.start();
//                udpSocket.close();
            }else{
                cNet = new ClientNetworking();
                yourTurn = false;
            }
        }while(connect2Udp);


//            PrintWriter writer = new PrintWriter(outputToServer, true); //This writer object will write and send the data to the server.

        String message = "";

//        while (!gameOver) {
//            ConnectHeader gameHeader = (ConnectHeader)inputFromServer.readObject();
////                System.out.println("Header data");
////                System.out.println(gameHeader.getgB());
////                System.out.println(gameHeader.getpID());
////                System.out.println(gameHeader.getwF());
////                System.out.println(gameHeader.getvF());
////                System.out.println(gameHeader.getM());
//
//            if (gameHeader.getwF() == 1) { //If the player has won
//                System.out.println(gameHeader.getgB());
//                System.out.println(gameHeader.getM());
//                gameOver = true;
//            }
//            else if (gameHeader.getwF() == -1) { //Pre game conditions when WF = -1
//                if (playerID == null) playerID = gameHeader.getpID();
//                System.out.println(gameHeader.getM());
////                    System.out.println("set player id");
//            }
//            else if (gameHeader.getpID().equals(playerID)) { //If its our turn
////                    System.out.println("reached your turn");
//                System.out.println(gameHeader.getgB()); //Print the gameBoard to the client
//                System.out.println(gameHeader.getM());
////                    System.out.println("waiting for user input");
//                message = userInputReader.readLine();
//                if (!message.equals("quit")) { //Had to exit the loop. The connection is first terminated from server side.
//                    outputToServer.writeObject(
//                            new ConnectHeader("", "", 0, -1, message)
//                    );
//                    outputToServer.flush();outputToServer.reset();
//                }
//            }
//            else {
//                System.out.println("Something went wrong");
//                System.out.println(gameHeader.getgB());
//                System.out.println(gameHeader.getpID());
//                System.out.println(gameHeader.getwF());
//                System.out.println(gameHeader.getvF());
//                System.out.println(gameHeader.getM());
//            }
//        }

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

//            userInputReader.close();
//            //fromServer.close();
//            outputToServer.close();
//            clientSocket.close();
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
    private class ClientNetworking {
        private int port = 4000;
        private ObjectOutputStream outputToServer;
        private ObjectInputStream inputFromServer;
        private Socket clientSocket = null;

        public ClientNetworking () {
            try {
                clientSocket = new Socket("127.0.0.1",port);
                System.out.println("Connected on port: " + clientSocket.getPort());

                BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in)); //Accept input from the user
                inputFromServer = new ObjectInputStream(clientSocket.getInputStream()); //Gets the input stream that receives data from the server.
                outputToServer = new ObjectOutputStream(clientSocket.getOutputStream()); //Gets the output stream that outputs the data to the server

                //UDP part must be here.

                //Connection confirmation
                ConnectHeader cnctCon = (ConnectHeader) inputFromServer.readObject();
                playerID = cnctCon.getpID();
                System.out.printf("%s%n",cnctCon.getM());

                //Game start notification
                ConnectHeader gsn = (ConnectHeader) inputFromServer.readObject();
                playerID = gsn.getpID();
                System.out.printf("%s%n",gsn.getM());
                
                //RED starts
                if (playerID.equals("R")) yourTurn = true;

                while (true) {
                    ConnectHeader header = (ConnectHeader) inputFromServer.readObject();
                    System.out.println(header.getgB());
                    System.out.println(header.getM());
                    String move = userInputReader.readLine();
                    outputToServer.writeObject(new ConnectHeader(header.getgB(),playerID,header.getwF(),header.getvF(),move));
                    outputToServer.flush();
                }

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Input exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        public void sendMsg () {
            ConnectHeader ch = new ConnectHeader(null,playerID,-1,-1,"ping");
            try {
                outputToServer.writeObject(ch);
                outputToServer.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    public class RDTReciever implements  Runnable{
        DatagramSocket socket = null;
        public byte[] ipAddr = new byte[]{127,0,0,1};
        int ackCtr;
        byte[] rcvbuf = new byte[256];
        FileOutputStream fos;// = new FileInputStream("RcvdFile.txt");
        ObjectOutputStream oos;//= new ObjectInputStream(fis);
        File rcvdFile;
        PrintWriter writer;//= new PrintWriter(rcvdFile);
        public RDTReciever(DatagramSocket socket) throws IOException {
            this.socket = socket;
            fos = new FileOutputStream("RcvdFile.txt");
            oos = new ObjectOutputStream(fos);
            ackCtr = 0;
        }

        public void rcvAll() throws IOException, ClassNotFoundException {
            while(socket!=null){
                DatagramPacket pkt = getPkt();
                socket.receive(pkt);
                rcvdFile = new File("rcvdFile.txt");
                writer = new PrintWriter(new FileWriter(rcvdFile,true));
                if(pkt.getData().length>0){
//                    ackCtr++;
                    handleData(pkt);
                    //Write the buffer to file stream
                }
                writer.close();
            }
        }


        public void sendAck() throws UnknownHostException {
            String ack = "ACK,"+ ackCtr;
            DatagramPacket pkt = new DatagramPacket(ack.getBytes(), ack.getBytes().length,InetAddress.getByAddress(ipAddr),5100);
            try {
                socket.send(pkt);
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("ERROR WHILE SENDING AN ACK");
            }
        }

        public DatagramPacket getPkt(){
            return new DatagramPacket(rcvbuf, rcvbuf.length);
        }
        public void handshake() throws IOException {
            DatagramPacket pkt = new DatagramPacket("Hello".getBytes(), "HELLO".getBytes().length,InetAddress.getByAddress(ipAddr),5100);
            socket.send(pkt);
        }

        public void handleData(DatagramPacket pkt) throws IOException, ClassNotFoundException {
            byte[] dat = pkt.getData();
            byte[] seg = new byte[16];
            ByteArrayInputStream bais = new ByteArrayInputStream(dat);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RDTSegment rdt = null;
            //if(ois.available()>0) {
                rdt = (RDTSegment) ois.readObject();
                System.out.println(ackCtr+ '\t' +rdt.seq);
            //}else{return;}
            if(rdt!=null && rdt.seq>=ackCtr) {
                System.out.println("Packet Recieved: Seq# = "+rdt.seq+" Ack#: "+ackCtr);
//                oos.write(rdt.data);
                System.out.println("hit");
                System.out.println(new String(rdt.data));
                writer.append(new String(rdt.data));
                ackCtr++;
                sendAck();
            }else{
                System.out.println("Duplicate sequence recieved: "+rdt.seq);
            }//Else Drop the packet
            rcvbuf = new byte[256];
        }

        @Override
        public void run() {
            try {
                handshake();
                rcvAll();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
}


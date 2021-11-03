import java.io.*;
import java.net.*;
import java.util.*;

public class Rdt implements Runnable{
    Queue<RDTSegment> allSegments;
    private int currentAck;
    DatagramSocket UDPSocket;
    byte[] buf;
    File file;
    FileInputStream fis;

    public Rdt() throws IOException {

        this.UDPSocket = new DatagramSocket(5100);
        this.file = new File("Koftarecipe.txt");
        buf = new byte[256];
        loadFile(file);
        allSegments = new LinkedList<RDTSegment>();
        System.out.println("UDPSocket created.");
        /**DatagramPacket pkt = new DatagramPacket(buf,buf.length);
        UDPSocket.receive(pkt); //Receive packets from UDP sockets
        //printData(pkt.getData());
        File txtfile = new File("Koftarecipe.txt");
//                        ObjectInputStream os = new ObjectInputStream(new FileInputStream(txtfile));
        FileInputStream fs = new FileInputStream(txtfile);
        buf = new byte[1024];
        buf = fs.readAllBytes();

        if(pkt.getData().length > 1){ //If there is any data in the packet that means we have recieved a message
            System.out.println(pkt.getData().toString());//
            DatagramPacket pkt2 = new DatagramPacket(buf,buf.length,pkt.getSocketAddress());
//                            DatagramPacket pkt = new DatagramPacket();
            respond(pkt2); //respond to the client
            isAlive = false;
        }

         */

    }

    private boolean loadFile(File file) throws IOException {
        fis = new FileInputStream(file);

        if(fis.available()>0){
            return true;
        }
        return false;
    }

    private DatagramPacket recieve(DatagramPacket pkt) throws IOException {
        UDPSocket.receive(pkt);
        return pkt;
    }

    private void respond(RDTSegment segment, SocketAddress sa) throws IOException {
        if(allSegments.size() <= 0){
            handshake(); //If we do not have any segments then assume this is the first packet received and initiate a handshake.
        }else{
            send(segment, sa); // Send the next segment to the user.
        }

    }

    private void handshake(){
        //for later
    }

    public void send(RDTSegment segment, SocketAddress sa) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(segment);
        oos.flush();
        byte[] data = baos.toByteArray();
        System.out.println("Segment "+ segment.seq+" Length is :" + data.length + " bytes");

        DatagramPacket pkt = new DatagramPacket(data,data.length,sa);

        UDPSocket.send(pkt);
    }

    public void createSegments() throws IOException {
        byte[] data=null;
        if(fis.available()>0){
            data = fis.readAllBytes();
        }

        for (int i = 0; i < data.length/16; i++) {
            byte[] segment = Arrays.copyOfRange(data,i*16,(i+1)*16);
            allSegments.add(new RDTSegment(i,256,segment));
//            allSegments
        }
        if((data.length/16)+16 <data.length){
            int i = data.length/16;
            byte[] segment = Arrays.copyOfRange(data,i*16,data.length);
            allSegments.add(new RDTSegment(((data.length/16)+1),256,segment));
        }
        System.out.println();

    }

    public void sendAll(SocketAddress sa) throws IOException {
        for (RDTSegment seg:
             allSegments) {
            send(seg,sa);
            UDPSocket.setSoTimeout(2000); // Time out for a second
            byte[] rcvbuf = new byte[32];
            DatagramPacket pkt = new DatagramPacket(rcvbuf, rcvbuf.length);
            System.out.println("Sent a packet#: "+ seg.seq);
            try {
                recieve(pkt);
                if(pkt.getData().length>0){
                    System.out.println(new String(pkt.getData()));
                }
            }catch(SocketTimeoutException ste){
                ste.printStackTrace();
                retransmission(seg,sa);
            }
        }
    }

    public void retransmission(RDTSegment seg, SocketAddress sa) {
        try {
            send(seg, sa);

            byte[] rcvbuf = new byte[16];
            DatagramPacket pkt = new DatagramPacket(rcvbuf, rcvbuf.length);
            try {
                recieve(pkt);
            } catch (SocketTimeoutException ste) {
                ste.printStackTrace();
                retransmission(seg, sa);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[1024];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            recieve(pkt);
            if(pkt.getData().length>0){
                System.out.println("Packet received by the server.");
                createSegments();
                sendAll(pkt.getSocketAddress());
                UDPSocket.close();
            }
            //createSegments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


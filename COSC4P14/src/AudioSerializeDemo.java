import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

public class AudioSerializeDemo {
    private byte[] buf;

    public AudioSerializeDemo(){

    }

    public byte[] serializer() throws FileNotFoundException, IOException{
        File audioWav = new File("262267__gowlermusic__radio-static.wav");

        FileInputStream fs = new FileInputStream(audioWav);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        buf = new byte[1024];
        int totalbytes=0;
        for (int readNum; (readNum = fs.read(buf)) != -1;) {
            bs.write(buf, 0, readNum); //no doubt here is 0
            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            totalbytes += readNum;
            System.out.println("read " + readNum + " bytes,");
        }
        System.out.println(totalbytes + "\t total bytes");
        return bs.toByteArray();

    }


    public static void main (String args[]){
        AudioSerializeDemo asd = new AudioSerializeDemo();
        try {
            byte[] dat = asd.serializer();
            AudioPlayer ap = new AudioPlayer();
            ap.deSerialize(dat);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.awt.*;
import java.io.*;

public class AudioPlayer {
    public File audioFile;

    public AudioPlayer(){
        audioFile = new File("sound.wav");
    }
    public void setAudioFile(File audioFile){};
    public void playAudioFile() throws IOException {
        Desktop.getDesktop().open(audioFile);
    };

    public void deSerialize(byte[] bis) throws FileNotFoundException, IOException {
        byte[] buf = new byte[1024];
        FileOutputStream fos= new FileOutputStream(audioFile);
        System.out.println(buf.toString());
//        while(){
            fos.write(bis);
//        }

    }
}

import java.io.Serializable;

public class RDTSegment implements Serializable {
    int seq;
    int ack;
    int windowSize;
    int length;
    byte[] data;

    public RDTSegment(int seq, int windowSize, byte[] data) {
        this.seq = seq;
        this.windowSize = windowSize;
        this.ack = this.seq + 1;
        this.data = data;
        this.length = data.length;
    }


}
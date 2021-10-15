// Package not detected, please report project structure on CodeTogether's GitHub Issues
import java.io.Serializable;
import java.nio.ByteBuffer;

public class ConnectHeader implements Serializable {

    private String gB;
    private String pID;
    private int wF;
    private int vF;
    private String m;

    public ConnectHeader (String gameBoard, String playerID, int winFlag, int validationFlag, String message) {
        this.gB = gameBoard;
        this.pID = playerID;
        this.wF = winFlag;
        this.vF = validationFlag;
        this.m = message;
    }

    public String getgB() {
        return gB;
    }

    public void setgB(String gB) {
        this.gB = gB;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public int getwF() {
        return wF;
    }

    public void setwF(int wF) {
        this.wF = wF;
    }

    public int getvF() {
        return vF;
    }

    public void setvF(int vF) {
        this.vF = vF;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}

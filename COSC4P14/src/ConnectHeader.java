// Package not detected, please report project structure on CodeTogether's GitHub Issues
import java.nio.ByteBuffer;

public class ConnectHeader {

    String gB;
    String pID;
    int wF;
    int vF;
    String m;

    public ConnectHeader (String gameBoard, String playerID, int winFlag, int validationFlag, String message) {
        this.gB = gameBoard;
        this.pID = playerID;
        this.wF = winFlag;
        this.vF = validationFlag;
        this.m = message;
    }

    

}

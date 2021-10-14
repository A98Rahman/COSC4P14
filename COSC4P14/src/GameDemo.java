import java.util.Scanner;
public class GameDemo {
    public GameLogic.PLAYERID RED;
    public GameLogic.PLAYERID BLUE;

    public static void main (String[] args){
        GameLogic logic = new GameLogic();

        logic.printGameBoard();
//        logic.moveValidation("F7", GameLogic.PLAYERID.RED);
//        logic.printGameBoard();
        Scanner input = new Scanner(System.in);
        String move = " ";
        while(!move.equalsIgnoreCase("q")){
            move = input.next();
            logic.validateAndPlay(move, GameLogic.PLAYERID.RED);
            logic.printGameBoard();
        }
    }
}

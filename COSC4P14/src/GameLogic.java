public class GameLogic {
    static enum PLAYERID{
        RED("R"),
        BLUE("B");

        public String label;

        PLAYERID(String label){
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
    static char[] rowLabel = {'A','B','C','D','E','F'};

    public PLAYERID[][] gameBoard = new PLAYERID[6][7];//6 rows and 7 columns
    public boolean playerID; //True = Blue, False = red

    public GameLogic(){
       initializeGameBoard();
    }
    public boolean validateAndPlay(String move, PLAYERID pID){ //Rows go from A to F and columns from 1 to 7
        int r,c;
        if(move.length() !=2){
            System.out.println("Invalid command");
            return false;
        }
        if(isValidRow(move.charAt(0)) >=0 && isValidColumn(move.substring(1))>0 ){
            r = isValidRow(move.charAt(0));
            c = isValidColumn(move.substring(1));
            if(gameBoard[r][c-1] != null){
                System.out.println("This spot is already taken");
                return false;
            }
            makeMove(r,c,pID); //make the move
        }else {
            System.out.println("Invalid Row or Column index");
            return false;}

        return true;
    }

    public void makeMove(int r, int c, PLAYERID pID){
        if(pID == PLAYERID.BLUE)
            gameBoard[r][c-1] = pID;
        else
            gameBoard[r][c-1] = pID;

        isWinningMove(pID);
    }
    public PLAYERID[][] getGameBoard(){
        return this.gameBoard;
    }
    public void resetGameBoard(){
        initializeGameBoard();
        System.out.println("Game board Reset");
    }

    /**
     * Returns true if the game is over.
     */
    public boolean evaluateBoard(){
        return false;
    }
    public PLAYERID getWinner(){
        return PLAYERID.BLUE;
    }

    public void initializeGameBoard(){
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                gameBoard[row][col] = null;
            }
        }
    }

//    public int isValidRow(char r){
//        for (char c: rowLabel) {
//            if(r==c)
//                return rowLabel;
//        }
//        return -1;
//    }

    public int isValidRow(char r){
        for (int i = 0; i < rowLabel.length; i++) {
            if(r == rowLabel[i])
                return i;
        }
        return -1;
    }

    public int isValidColumn(String c){
        try{
            int col =  Integer.parseInt(c);
            if(col>=0 && col <=7)
                return col;
            else
                return -1;
        }catch (NumberFormatException n){
            System.out.println("Please enter a valid number");
            //  n.printStackTrace();
            return -1;
        }
    }
    public void printGameBoard(){
        for (int row = 0; row < 6; row++) {
            System.out.print(Character.toString(rowLabel[row]) + '\t');
            for (int col = 0; col < 7; col++) {
                System.out.print(((gameBoard[row][col]) == null ?"-":((gameBoard[row][col]).toString()) )+ '\t');
            }
            System.out.println();
        }
        System.out.print('\t');
        for (int i = 0; i < 7; i++) {
            System.out.print(i+1);
            System.out.print('\t');

        }
        System.out.println();
    }

    public boolean isWinningMove(PLAYERID pID){
        if(verticalCombination(pID) || horizontalCombination(pID)){
            System.out.println("GAMEOVER");
            return true;
        }
        return false;
    }

    public boolean verticalCombination(PLAYERID pID){
        int consec = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if(gameBoard[row][col] == pID){
                    consec++;
                    if(consec==4){return true;}
                }else{consec=0;}
            }
        }
        return false;
    }

    public boolean horizontalCombination(PLAYERID pID){
        int consec = 0;
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                if(gameBoard[row][col] == pID){
                    consec++;
                    if(consec==4){return true;}
                }else{consec=0;}
            }
        }
        return false;
    }
//    public boolean diagonalCombination(PLAYERID pID){
//        int consec = 0;
//        for (int row = 0; row < 3; row++) {
//            for (int col = 0; col < 4; col++) {
//                 if(gameBoard[row][col] == pID || gameBoard[row][col] == pID ){
//                    consec++;
//                    if(consec==4){return true;}
//                }else{consec=0;}
//            }
//        }
//        return false;
//    }
}

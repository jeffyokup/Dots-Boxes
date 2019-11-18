import java.util.Scanner;

import static java.lang.System.out;

class Game {

    private final Board board;

    private final int depth = 6;


    public Game(final int boxesPerRow, final int boxesPerCol, final int depth, final boolean movesRandomized){
        board = new Board(boxesPerRow, boxesPerCol);
        StickAI.setMovesRandomized(movesRandomized);
        StickAI.setDepth(depth);
    }

    /**
     * Plays a game and returns the winner.
     * @return The Player that won the game.
     */
    public Player startGame(){

        while(!board.isGameOver()){
            printBoard();
            getPlayerMove();
            board.switchTurn();
        }
        Player winningPlayer = getWinner();
        String champ;
        if(winningPlayer == Player.PLAYER_ONE){
            champ = "Player One";
        }else if(winningPlayer == Player.PLAYER_TWO){
            champ = "Player Two";
        }else{
            champ = "No one";
        }
        out.println("The Game is Over. " + champ + " won!");
        return winningPlayer;
    }

    /**
     * Returns the move of the player with the current turn.
     */
    private void getPlayerMove(){
        PlayerMove move;
        Scanner scan = new Scanner(System.in);

        while(true){
            if(board.getPlayerTurn() == Player.PLAYER_ONE){
                System.out.println("Enter a move " + board.getPlayerTurn() + ": ");
                String input = scan.nextLine();
                String[] moveArray = input.split(" ");
                if(!isValidMove(moveArray)){
                    out.println("Invalid Move.");
                    continue;
                }
                int row = Integer.parseInt(moveArray[0]);
                int col = Integer.parseInt(moveArray[1]);
                Side side = charToSide(moveArray[2].charAt(0));
                move = new PlayerMove(row, col, side, Player.PLAYER_ONE);
            }else{
                move = StickAI.getMove(board);
            }
            System.out.println(board.getPlayerTurn() + " move: " + move);

            boolean boxCompleted = board.setEdge(move.getRow(), move.getCol(), board.getPlayerTurn(), move.getSide()); // 1 1 r    1 1 l   0 1 r   0 1 b
                                                                                                                       // 1 0 r    1 1 r   1 1 b   1 0 l
            if(boxCompleted){
                printBoard();
                if(board.isGameOver())
                    return;
                continue;
            }
            break;
        }
    }

    private boolean isValidMove(final String[] move){
        try {
            int row = Integer.parseInt(move[0]);
            int col = Integer.parseInt(move[1]);
            char c = Character.toLowerCase(move[2].charAt(0));
            if(row < 0 || col < 0 || row >= board.getNumberOfRows() || col >= board.getNumberOfCols()){
                return false;
            }
            Side side = charToSide(c);
            return (board.getBox(row,col).getOwnerOfSide(side) == Player.NO_PLAYER);
        }catch (Exception e){
            return false;
        }
    }

    private Player getWinner(){
        int Player_One_Score = 0;
        int Player_Two_Score = 0;

        for(int row = 0; row < board.getNumberOfRows(); row++){
            for(int col = 0; col < board.getNumberOfCols(); col++){
                if(board.getBox(row,col).getBoxOwner() == Player.PLAYER_ONE)
                    Player_One_Score++;
                else if(board.getBox(row,col).getBoxOwner() == Player.PLAYER_TWO)
                    Player_Two_Score++;
                else
                    throw new RuntimeException("Error determining winner.");
            }
        }
        if(Player_One_Score == Player_Two_Score)
            return Player.NO_PLAYER;
        return (Player_One_Score > Player_Two_Score) ? Player.PLAYER_ONE : Player.PLAYER_TWO;
    }

    private void printBoard(){
        // Print first row.
        Box[] boxRow = board.getBoxRow(0);
        // Number of boxes on one row.
        char edge;
        out.print("*");
        for(Box box : boxRow){
            Player p = box.getOwnerOfSide(Side.TOP);
            if(p != Player.NO_PLAYER){
                edge = '-';
            }else{
                edge = ' ';
            }
            out.print(edge + "*");
        }
        out.println();

        // Print rest.
        for(int row = 0; row < board.getNumberOfRows(); row++){
            boxRow = board.getBoxRow(row);
            // Number of boxes on one row.

            // do 1st here.
            Box initialBox = boxRow[0];
            if(initialBox.getOwnerOfSide(Side.LEFT) != Player.NO_PLAYER){
                out.print("|");
            }else{
                out.print(" ");
            }

            for (Box box : boxRow) {
                char middle;
                Player p = box.getBoxOwner();
                if (p == Player.PLAYER_ONE) {
                    middle = '1';
                } else if (p == Player.PLAYER_TWO) {
                    middle = '2';
                } else {
                    middle = ' ';
                }

                p = box.getOwnerOfSide(Side.RIGHT);
                if (p != Player.NO_PLAYER) {
                    edge = '|';
                } else {
                    edge = ' ';
                }
                out.print(middle + "" + edge);
            }
            out.println();

            out.print("*");
            for (Box box : boxRow) {
                Player p = box.getOwnerOfSide(Side.BOTTOM);
                if (p != Player.NO_PLAYER) {
                    edge = '-';
                } else {
                    edge = ' ';
                }
                out.print(edge + "*");
            }
            out.println();
        }
        out.println();
    }

    private Side charToSide(final char c){
        switch (c){
            case 't':
                return Side.TOP;
            case 'l':
                return Side.LEFT;
            case 'b':
                return Side.BOTTOM;
            case 'r':
                return Side.RIGHT;
            default:
                throw new RuntimeException("Invalid move character.");
        }
    }
}

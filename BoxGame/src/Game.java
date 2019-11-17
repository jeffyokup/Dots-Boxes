import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

public class Game {

    private Board board;

    private final int depth = 9;


    public Game(int boxesPerRow, int boxesPerCol){
        board = new Board(boxesPerRow, boxesPerCol);
    }

    /**
     * Plays a game and returns the winner.
     * @return
     */
    public Player startGame(){

        while(!board.isGameOver()){
            printBoard();
            getPlayerMove();
            board.switchTurn();
        }
        Player winningPlayer = getWinner();
        String champ = "";
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
     * @return Row Col
     */
    private void getPlayerMove(){
        PlayerMove move = null;
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
                move = StickAI.getMove(board, depth);
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

    private boolean isValidMove(String[] move){
        if(move.length != 3 || move[2].length() > 1){
            throw new RuntimeException("Invalid Input.");
        }
        try {
            int row = Integer.parseInt(move[0]);
            int col = Integer.parseInt(move[1]);
            char c = Character.toLowerCase(move[2].charAt(0));
            if(row < 0 || col < 0 || row >= board.getNumberOfRows() || col >= board.getNumberOfCols()){
                return false;
            }
            Side side = charToSide(c);
            return (board.getBox(row,col).getOwnerOfSide(side) == Player.NO_PLAYER) ? true : false;
        }catch (Exception e){
            return false;
        }
    }

    public Player getWinner(){
        int Player_One_Score = 0;
        int Player_Two_Score = 0;

        for(int row = 0; row < board.getNumberOfRows(); row++){
            for(int col = 0; col < board.getNumberOfCols(); col++){
                if(board.getBox(row,col).getBoxOwner() == Player.PLAYER_ONE)
                    Player_One_Score++;
                else if(board.getBox(row,col).getBoxOwner() == Player.PLAYER_TWO)
                    Player_Two_Score++;
                else
                    throw new RuntimeException("Ayy. Lmao. Ya fucked up.");
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
        char edge = 'z';
        out.print("*");
        for(int col = 0; col < boxRow.length; col++){
            Box box = boxRow[col];
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
            edge = 'z';

            // do 1st here.
            Box initialBox = boxRow[0];
            if(initialBox.getOwnerOfSide(Side.LEFT) != Player.NO_PLAYER){
                out.print("|");
            }else{
                out.print(" ");
            }

            for(int col = 0; col < boxRow.length; col++){
                Box box = boxRow[col];

                char middle = 'z';
                Player p = box.getBoxOwner();
                if(p == Player.PLAYER_ONE){
                    middle = '1';
                }else if(p == Player.PLAYER_TWO){
                    middle = '2';
                }else{
                    middle = ' ';
                }


                p = box.getOwnerOfSide(Side.RIGHT);
                if(p != Player.NO_PLAYER){
                    edge = '|';
                }else{
                    edge = ' ';
                }
                out.print(middle + "" + edge);
            }
            out.println();

            out.print("*");
            for(int col = 0; col < boxRow.length; col++){
                Box box = boxRow[col];
                Player p = box.getOwnerOfSide(Side.BOTTOM);
                if(p != Player.NO_PLAYER){
                    edge = '-';
                }else{
                    edge = ' ';
                }
                out.print(edge + "*");
            }
            out.println();
        }
        out.println();
    }

    private Side charToSide(char c){
        Side side = null;
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

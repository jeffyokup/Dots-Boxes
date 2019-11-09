import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

public class Game {

    private Player playerTurn = Player.PLAYER_ONE;

    private Board board;


    public Game(int boxesPerRow, int boxesPerCol){
        board = new Board(boxesPerRow, boxesPerCol);
    }

    /**
     * Plays a game and returns the winner.
     * @return
     */
    public Player startGame(){

        while(!isGameOver()){
            printBoard();
            getPlayerMove();
            switchTurn();
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
        while(true){
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a move " + playerTurn + ": ");
            String move = scan.nextLine();
            String[] moveArray = move.split(" ");
            if(!isValidMove(moveArray)){
                out.println("Invalid Move.");
                continue;
            }
            int row = Integer.parseInt(moveArray[0]);
            int col = Integer.parseInt(moveArray[1]);
            Side side = charToSide(moveArray[2].charAt(0));
            boolean boxCompleted = board.getBox(row,col).setSide(playerTurn,side);
            boolean neighborBoxCompleted = setNeighborBoxSide(row, col, playerTurn, side);
            if(boxCompleted || neighborBoxCompleted){
                printBoard();
                if(isGameOver())
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

    private void switchTurn(){
        if(playerTurn == Player.PLAYER_ONE){
            playerTurn = Player.PLAYER_TWO;
        }else{
            playerTurn = Player.PLAYER_ONE;
        }
    }


    public boolean isGameOver(){
        for(int row = 0; row < board.getNumberOfRows(); row++){
            for(int col = 0; col < board.getNumberOfCols(); col++){
                if(board.getBox(row,col).getBoxOwner() == Player.NO_PLAYER)
                    return false;
            }
        }
        return true;
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

    private boolean setNeighborBoxSide(int row, int col, Player player, Side side){
        if((side == Side.TOP && row == 0) || (side == Side.LEFT && col == 0) || (side == Side.BOTTOM && row == (board.getNumberOfRows()-1)) || (side == Side.RIGHT && col == board.getNumberOfCols()-1) ){
            return false;
        }

        //Get the new row/col of the neighbor box.
        int newRow = row;
        int newCol = col;

        if(side == Side.TOP){
            newRow--;
        }else if(side == Side.LEFT){
            newCol--;
        }else if(side == Side.BOTTOM){
            newRow++;
        }else if(side == Side.RIGHT){
            newCol++;
        }else{
            throw new RuntimeException("Enum Exception");
        }
        Side newSide = null;

        // Grab the other side of the neighbor box.
        switch (side){
            case TOP:
                newSide = Side.BOTTOM;
                break;
            case LEFT:
                newSide = Side.RIGHT;
                break;
            case BOTTOM:
                newSide = Side.TOP;
                break;
            case RIGHT:
                newSide = Side.LEFT;
                break;
            default:
                throw new RuntimeException("Error with enum.");
        }
        return board.getBox(newRow,newCol).setSide(player, newSide);
    }

    public ArrayList<PlayerMove> getAvailableMoves(){
        ArrayList<PlayerMove> availableMoves = new ArrayList<PlayerMove>();
        Side[] sides = {Side.TOP, Side.LEFT, Side.BOTTOM, Side.RIGHT};

        for(int row = 0; row < board.getNumberOfRows(); row++){
            Box[] boxRow = board.getBoxRow(row);
            for(int col = 0; col< board.getNumberOfCols(); col++){
                Box currentBox = boxRow[col];

                // Iterate through all available sides.
                for(int i = 0; i < 4; i++){
                    Side side = sides[i];
                    Player owner = currentBox.getOwnerOfSide(side);
                    if(owner != Player.NO_PLAYER){
                        availableMoves.add(new PlayerMove(row, col, side));
                    }
                }
            }
        }

        // TODO - Now remove duplicate moves.
        // TODO - Now remove duplicate moves.
        // TODO - Now remove duplicate moves.
        // TODO - Now remove duplicate moves.
        return availableMoves;
    }

}

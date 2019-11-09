public class Board {


    /**
     * The state of the game board. Each index is a dot.
     *
     *
     *  *  *  *  *  *  *
     *  *  *  *  *  *  *
     *  *  *  *  *  *  *
     *  *  *  *  *  *  *
     *  *  *  *  *  *  *
     *  *--*  *  *  *  *
     *  |  |
     *  *--*  *  *  *  *
     *
     *
     *
     *  *-*
     *
     */
    private Box[][] board;

    public Board(int boxesPerRow, int boxesPerCol){
        board = new Box[boxesPerRow][boxesPerCol];

        for(int row = 0; row < boxesPerRow; row++){
            for(int col = 0; col < boxesPerCol; col++){
                board[row][col] = new Box();
            }
        }
    }

    /**
     * Returns player one score - player two score.
     * @return
     */
    public int getValueOfBoardState(){
        int playerOneScore = 0;
        int playerTwoScore = 0;

        for(int row = 0; row < getNumberOfRows(); row++){
            for(int col = 0; col < getNumberOfCols(); col++){
                Box box = board[row][col];
                if(box.getBoxOwner() == Player.PLAYER_ONE){
                    playerOneScore++;
                }else if(box.getBoxOwner() == Player.PLAYER_TWO){
                    playerTwoScore++;
                }
            }
        }
        return playerOneScore - playerTwoScore;
    }

    public int getNumberOfRows(){
        return board.length;
    }

    public int getNumberOfCols(){
        return board[0].length;
    }

    public Box getBox(int row, int col){
        return board[row][col];
    }

    public Box[] getBoxRow(int row){
        return board[row];
    }
}

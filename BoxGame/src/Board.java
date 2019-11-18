import java.util.ArrayList;

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
    private final Box[][] board;

    private int edgesLeft = 0;

    private Player playerTurn = Player.PLAYER_ONE;

    public Board(final int boxesPerRow, final int boxesPerCol){
        board = new Box[boxesPerRow][boxesPerCol];

        for(int row = 0; row < boxesPerRow; row++){
            for(int col = 0; col < boxesPerCol; col++){
                board[row][col] = new Box();
            }
        }

        // Calculate number of edges.
        edgesLeft += boxesPerCol; // Calculate top edges in first row.
        int edgesPerRowExcludingTop = (boxesPerCol * 2) + 1;
        edgesLeft += (edgesPerRowExcludingTop * boxesPerRow);
    }

    private Board(final Board board){
        this.edgesLeft = board.getEdgesLeft();
        this.playerTurn = board.getPlayerTurn();

        this.board = new Box[board.getNumberOfRows()][board.getNumberOfCols()];
        for(int row = 0; row < board.getNumberOfRows(); row++){
            for(int col = 0; col < board.getNumberOfCols(); col++){
                this.board[row][col] = board.getBox(row, col).clone();
            }
        }

    }

    /**
     * Returns player one score - player two score.
     * @return the value of the board state.
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

        int numBoxesAlmostCompleted = 0;
        for(int row = 0; row < getNumberOfRows(); row++){
            for(int col = 0; col < getNumberOfCols(); col++){
                Box box = board[row][col];
                if(box.oneMoreSideLeft()){
                    numBoxesAlmostCompleted++;
                }
            }
        }

        int boardValue = playerTwoScore - playerOneScore;
        boardValue = (playerTurn == Player.PLAYER_ONE) ? (boardValue - numBoxesAlmostCompleted) : (boardValue + numBoxesAlmostCompleted);
        return boardValue;
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

    public Board clone(){
        return new Board(this);
    }

    public ArrayList<PlayerMove> getAvailableMoves(){
        ArrayList<PlayerMove> availableMoves = new ArrayList<>();

        // Check top row.
        Box[] boxRow = getBoxRow(0);
        for(int col = 0; col < getNumberOfCols(); col++){
            Box currentBox = boxRow[col];
            Player owner = currentBox.getOwnerOfSide(Side.TOP);
            if(owner == Player.NO_PLAYER){
                availableMoves.add(new PlayerMove(0, col, Side.TOP, Player.PLAYER_TWO));
            }
        }

        // Check left column.
        for(int row = 0; row < getNumberOfRows(); row++){
            Box currentBox = board[row][0];
            Player owner = currentBox.getOwnerOfSide(Side.LEFT);
            if(owner == Player.NO_PLAYER){
                availableMoves.add(new PlayerMove(row, 0, Side.LEFT, Player.PLAYER_TWO));
            }
        }

        Side[] sides = {Side.BOTTOM, Side.RIGHT};

        for(int row = 0; row < getNumberOfRows(); row++){
            boxRow = getBoxRow(row);
            for(int col = 0; col< getNumberOfCols(); col++){
                Box currentBox = boxRow[col];

                // Iterate through all available sides.
                for(int i = 0; i < 2; i++){
                    Side side = sides[i];
                    Player owner = currentBox.getOwnerOfSide(side);
                    if(owner == Player.NO_PLAYER){
                        availableMoves.add(new PlayerMove(row, col, side, Player.PLAYER_TWO));
                    }
                }
            }
        }
        return availableMoves;
    }

    public boolean setEdge(int row, int col, Player player, Side side){
        edgesLeft = Math.max(0, edgesLeft - 1);
        boolean originalBoxCompleted = getBox(row, col).setSide(player, side);
        boolean neighborBoxCompleted = setNeighborBoxSide(row, col, player, side);
        return (originalBoxCompleted || neighborBoxCompleted);
    }

    private boolean setNeighborBoxSide(int row, int col, Player player, Side side){
        if((side == Side.TOP && row == 0) || (side == Side.LEFT && col == 0) || (side == Side.BOTTOM && row == (getNumberOfRows()-1)) || (side == Side.RIGHT && col == getNumberOfCols()-1) ){
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
        Side newSide;

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
        return getBox(newRow, newCol).setSide(player, newSide);
    }

    public boolean isGameOver(){
        return (edgesLeft == 0);
    }

    private int getEdgesLeft(){ return edgesLeft; }

    public boolean executeMove(PlayerMove move){
        return setEdge(move.getRow(), move.getCol(), move.getPlayer(), move.getSide());
    }

    public void switchTurn(){
        if(playerTurn == Player.PLAYER_ONE){
            playerTurn = Player.PLAYER_TWO;
        }else{
            playerTurn = Player.PLAYER_ONE;
        }
    }

    public Player getPlayerTurn(){
        return playerTurn;
    }
}

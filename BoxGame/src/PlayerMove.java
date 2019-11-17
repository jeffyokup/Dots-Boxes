public class PlayerMove {

    private int row, col, boardValue;
    private Side side;
    private Player player;

    public PlayerMove(int row, int col, Side side, Player player){
        this.row = row;
        this.col = col;
        this.side = side;
        this.player = player;
    }

    public PlayerMove(PlayerMove move){
        this(move.getRow(), move.getCol(), move.getSide(), move.getPlayer());
        setBoardValue(move.getBoardValue());
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Side getSide(){
        return side;
    }

    public Player getPlayer(){ return player; }

    public void setBoardValue(int boardValue){ this.boardValue = boardValue; }

    public int getBoardValue(){ return boardValue; }

    public PlayerMove clone(){
        return new PlayerMove(this);
    }

    @Override
    public String toString(){
        return "Row: " + row + " Col: " + col + " Side: " + side;
    }
}

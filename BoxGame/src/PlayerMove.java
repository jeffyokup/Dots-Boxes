public class PlayerMove {

    private int row, col;
    private Side side;

    public PlayerMove(int row, int col, Side side){
        this.row = row;
        this.col = col;
        this.side = side;
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

    @Override
    public String toString(){
        return "Row: " + row + " Col: " + col + " Side: " + side;
    }
}

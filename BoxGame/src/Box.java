import java.util.Arrays;

public class Box {
    /*
    * 0 -> Top
    * 1 -> Left
    * 2 -> Bottom
    * 3 -> Right
     */
    private Player[] sides = new Player[4];

    private Player owner = Player.NO_PLAYER;

    public Box(){
        Arrays.fill(sides, Player.NO_PLAYER);
    }

    public Box(Player owner, Player[] sides){
        System.arraycopy(sides, 0, this.sides, 0, sides.length);
        this.owner = owner;
    }

    /**
     * Fills in the given side of this box.
     * @param player
     * @param side
     * @return true if the move completed the box. False otherwise.
     */
    public boolean setSide(Player player, Side side){
        if(player == null || side == null || player == Player.NO_PLAYER || owner != Player.NO_PLAYER){
            throw new IllegalArgumentException("Invalid argument to setSide:" + player + " " + side + Arrays.toString(sides));
        }

        switch (side){
            case TOP:
                if(sides[0] == Player.NO_PLAYER){
                    sides[0] = player;
                }else{
                    throw new RuntimeException("Bad side placement.");
                }
                break;
            case LEFT:
                if(sides[1] == Player.NO_PLAYER){
                    sides[1] = player;
                }else{
                    throw new RuntimeException("Bad side placement.");
                }
                break;
            case BOTTOM:
                if(sides[2] == Player.NO_PLAYER){
                    sides[2] = player;
                }else{
                    throw new RuntimeException("Bad side placement.");
                }
                break;
            case RIGHT:
                if(sides[3] == Player.NO_PLAYER){
                    sides[3] = player;
                }else{
                    throw new RuntimeException("Bad side placement.");
                }
                break;
            default:
                break;

        }
        return checkBoxCompletion(player);
    }

    public Player getOwnerOfSide(Side side){
        switch (side){
            case TOP:
                return sides[0];
            case LEFT:
                return sides[1];
            case BOTTOM:
                return sides[2];
            case RIGHT:
                return sides[3];
            default:
                throw new RuntimeException("Error in getOwnerOfSide");
        }
    }

    public Player getBoxOwner(){
        return owner;
    }


    /**
     *
     * Returns true if the box just got completed.
     * False otherwise.
     * @param player
     * @return True if box got completed.
     */
    public boolean checkBoxCompletion(Player player){
        int sidesCompleted = 0;
        for(int i = 0; i < 4; i++){
            if(sides[i] != Player.NO_PLAYER) {
                sidesCompleted++;
            }
        }
        if(sidesCompleted == 4){
            owner = player;
            return true;
        }
        return false;
    }

    public boolean oneMoreSideLeft(){
        int sidesFilled = 0;
        for(int i = 0; i < 4; i++){
            if(sides[i] != Player.NO_PLAYER){
                sidesFilled++;
            }
        }
        return (sidesFilled == 3);
    }

    public Box clone(){
        return new Box(owner, sides);
    }

}

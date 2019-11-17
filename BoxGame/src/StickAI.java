import java.util.ArrayList;
import java.util.Random;

public class StickAI {

    public StickAI(){

    }

    private static Random rand = new Random();

    public static PlayerMove getMove(Board board, int depth){
        Board boardClone = board.clone();
        return getMove(boardClone, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, null);
    }

    private static PlayerMove getMove(Board node, int depth, int alpha, int beta, boolean maximizingPlayer, PlayerMove move){
        ArrayList<PlayerMove> potentialMoves = node.getAvailableMoves();
        if(depth == 0 || node.isGameOver() || potentialMoves.size() == 0){
            move.setBoardValue(node.getValueOfBoardState());
            return move; // return node.
        }
        if(maximizingPlayer){
            int value = Integer.MIN_VALUE;
            PlayerMove bestMove = null;
            for(PlayerMove nextMove : potentialMoves){ // For each child node.
                Board newBoard = node.clone();
                boolean completedBox = newBoard.executeMove(nextMove.clone());

                boolean willMaxPlayerGo = false;
                if(completedBox){
                    // Player completed a box.
                    willMaxPlayerGo = true;
                }else{
                    newBoard.switchTurn();
                }
                int boardValue = getMove(newBoard, depth-1, alpha, beta, willMaxPlayerGo, nextMove.clone()).getBoardValue();
                if(boardValue > value){
                    value = boardValue;
                    nextMove.setBoardValue(boardValue);
                    bestMove = nextMove;
                }
                alpha = Math.max(alpha, value);
                if(alpha >= beta){
                    break;
                }
            }
            return bestMove;
        }else{
            int value = Integer.MAX_VALUE;
            PlayerMove bestMove = null;
            for(PlayerMove nextMove : potentialMoves){ // For each child node.
                Board newBoard = node.clone();
                boolean completedBox = newBoard.executeMove(nextMove);
                boolean willMaxPlayerGo = true;
                if(completedBox){
                    // Player completed a box.
                    willMaxPlayerGo = false;
                }else{
                    newBoard.switchTurn();
                }
                int boardValue = getMove(newBoard, depth-1, alpha, beta, willMaxPlayerGo, nextMove).getBoardValue();
                if(boardValue < value){
                    value = boardValue;
                    bestMove = nextMove;
                }
                beta = Math.min(beta, value);
                if(alpha >= beta){
                    break;
                }
            }
            return bestMove;
        }
    }
}

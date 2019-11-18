import java.util.ArrayList;
import java.util.Random;

class StickAI {

    private static Random rand = new Random();

    private static boolean movesRandomized;

    private static int alpha_beta_depth;

    public static PlayerMove getMove(final Board board){
        Board boardClone = board.clone();
        return getMove(boardClone, alpha_beta_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, null);
    }

    private static PlayerMove getMove(final Board node, final int depth, int alpha, int beta, final boolean maximizingPlayer, final PlayerMove move){
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

    public static void setMovesRandomized(final boolean randomize){
        movesRandomized = randomize;
    }

    public static void setDepth(final int depthToSet){
        alpha_beta_depth = depthToSet;
    }
}

/* Evaluates a given board state based on the ratio of pieces
   the player has out of the total alive pieces on the board */

public class Evaluator00 implements Evaluator {

    public double evaluate(CheckersGameState s, int player){
      CheckersGameState3 gs = (CheckersGameState3) s;
      return gs.pieceRatio(player);
    }

}

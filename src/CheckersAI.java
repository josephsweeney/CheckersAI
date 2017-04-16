public class CheckersAI{


    Evaluator eval;

    public CheckersAI(Evaluator e, int player){
        this.eval = e;
    }

    public Move minimax(CheckersGameState s, int ply){
        double alpha = Double.POSITIVE_INFINITY;
        double beta = Double.NEGATIVE_INFINITY;
        double v = Double.NEGATIVE_INFINITY;
        double check;
        Move max;
        for(Move a: s.actions()){
            check = minValue(s.result(a), alpha, beta, ply, depth + 1);
            if(check > v){
                v = check;
                max = a;
            }
            if(v > beta){
                return max;
            }
            alpha = Math.max(alpha, v);
        }
        returm max;
    }

   private double maxValue(CheckersGameState s, double alpha, double beta, int ply, int depth){

      if(s.isTerminal() || depth == ply){
         return eval.evaluate(s); // if terminal, piece ratio should be infinite
        }
        double v = Double.NEGATIVE_INFINITY;
        double check;
        for(Move a: s.actions()){
            check = minValue(s.result(a), alpha, beta, ply, depth + 1);
            if(check > v){
                v = check;
            }
            if (v >= beta){
               return v;
            }
            alpha  = Math.max(alpha, v);
            }
        return v;
    }

   private double minValue(CheckersGameState s, double alpha, double beta, int ply, int depth){

       if(s.isTerminal() || depth == ply){
           return eval.evaluate(s);
        }
       double v = Double.POSITIVE_INFINITY;
       double check;
       for(Move a: s.actions()){
           check = maxValue(s.result(a), alpha, beta, ply, depth + 1);
           if(check < v){
               v = check;
            }
           if( v <= alpha){
               return v;
            }
           beta = Math.min(beta, v);
        }
       return v;
    }


}

public class CheckersAI{


    Evaluator eval;
    int player;

    public CheckersAI(Evaluator e, int player){
        this.eval = e;
        this.player = player;
    }

    public Move minimax(CheckersGameState s, int ply){
        int depth = 0;
        if(s.isTerminal() || depth == ply){
            return null;
        }
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double v = Double.NEGATIVE_INFINITY;
        double check;
        Move max = null;
        System.out.println(s.actions().size());
        for(Move a: s.actions()){
            check = minValue(s.result(a), alpha, beta, ply, depth + 1);
            if(check > v){
                v = check;
                max = a;
            }
            alpha = Math.max(alpha, v);
            if(alpha >= beta){
                return max;
            }
        }
        return max;
    }

   private double maxValue(CheckersGameState s, double alpha, double beta, int ply, int depth){

        if(s.isTerminal() || depth == ply){
         return eval.evaluate(s, this.player); // if terminal, piece ratio should be infinite
        }
        double v = Double.NEGATIVE_INFINITY;
        double check;
        for(Move a: s.actions()){
            check = minValue(s.result(a), alpha, beta, ply, depth + 1);
            if(check > v){
                v = check;
            }
            alpha  = Math.max(alpha, v);
            if (alpha >= beta){
               return v;
            }
        }
        return v;
    }

   private double minValue(CheckersGameState s, double alpha, double beta, int ply, int depth){

       if(s.isTerminal() || depth == ply){
           return eval.evaluate(s, this.player);
        }
       double v = Double.POSITIVE_INFINITY;
       double check;
       for(Move a: s.actions()){
           check = maxValue(s.result(a), alpha, beta, ply, depth + 1);
           if(check < v){
               v = check;
            }
           beta = Math.min(beta, v);
           if( beta <= alpha){
               return v;
            }
        }
       return v;
    }


}

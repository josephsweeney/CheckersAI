import java.util.Arrays;

/* holds minimax code */
public class CheckersAI{


    Evaluator eval;
    int player;

    public CheckersAI(Evaluator e, int player){
        this.eval = e;
        this.player = player;
    }

    public void setPlayer(int player){
        this.player = player;
    }

    public int getPlayer(){
        return this.player;
    }

    private boolean stop(CheckersGameState state, boolean jumped, int depth, int min_ply){
        CheckersGameState3 s = (CheckersGameState3) state;
        if(depth < min_ply){
            return false;
        }
        else if(depth == min_ply && !s.canJump() && !jumped && !s.canExchange()){
           return true;
        }
        else if(depth == min_ply +1 && !s.canJump() && !s.canExchange() && depth == 4){
            return true;
        }
        else if(depth > min_ply +1 && depth < min_ply + 10  && !s.canJump()){
            return true;
        }
        else if(depth > min_ply +10 && depth < min_ply + 20 && s.twoKings()){
            return true;
        }
        else if(depth >=  min_ply + 20){
            return true;
        }
        else{
            return false;
        }
    }

    public Move minimax(CheckersGameState s, int min_ply){
        int depth = 0;
        if(s.isTerminal()){
            return null;
        }
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double v = Double.NEGATIVE_INFINITY;
        double check;
        Move max = null;
        for(Move a: s.actions()){
	    if(s.actions().size() == 1) {
		return a;
	    }
            check = minValue(s.result(a), alpha, beta, depth + 1, a.isJump(), min_ply);
            if(check > v){
                v = check;
                max = a;
            }
            alpha = Math.max(alpha, v);
            if(alpha >= beta){
                return max;
            }
        }
        max.setValue(v);
        return max;
    }

   private double maxValue(CheckersGameState s, double alpha, double beta, int depth, boolean jumped, int min_ply){
        if(s.isTerminal() || stop(s, jumped, depth, min_ply)){
             return eval.evaluate(s, this.player); // if terminal, piece ratio should be infinite
        }
        double v = Double.NEGATIVE_INFINITY;
        double check;
        for(Move a: s.actions()){
            check = minValue(s.result(a), alpha, beta, depth + 1, a.isJump(), min_ply);
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

   private double minValue(CheckersGameState s, double alpha, double beta, int depth, boolean jumped, int min_ply){
       if(s.isTerminal() || stop(s, jumped, depth, min_ply)){
           return eval.evaluate(s, this.player);
        }
       double v = Double.POSITIVE_INFINITY;
       double check;
       for(Move a: s.actions()){
           check = maxValue(s.result(a), alpha, beta, depth + 1, a.isJump(), min_ply);
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

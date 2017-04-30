
public class EndEvaluator extends LearningEvaluator {


    public EndEvaluator(String file) {
	super(file);
    }
    
    public double evaluate(CheckersGameState s, int player) {
	double endVal = 100;

	 if(s.isTerminal()){
           if(s.winner() == player){
               return endVal; 
           }
           else if(s.winner() == 0){
	       return 0;
	   }
	   else {
               return -endVal; 
           }
        }
        double[] params = s.getFeatures(player);
	double value = dot(this.weights, params);
	if(value > endVal) {
	    value = endVal - 1;
	} else if(value < -endVal) {
	    value = -endVal + 1;
	}
        return value;

    }
    
}

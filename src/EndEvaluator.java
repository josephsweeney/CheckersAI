
public class EndEvaluator extends LearningEvaluator {


    public EndEvaluator(String file) {
	super(file);
    }
    
    public double evaluate(CheckersGameState s, int player) {
	double endVal = 100;

	 if(s.isTerminal()){
           if(s.winner() == player){
               return endVal; // what should this be?
           }
           else{
               return -endVal; // assuming only positive evalutions
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

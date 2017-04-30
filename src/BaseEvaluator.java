
public class BaseEvaluator implements Evaluator{

    // meant for beta to use
    // when beta should use alpha's weights, have alpha commit to beta.csv and then call refreshWeights()

    protected WeightsParser wp;
    protected String file;
    protected double[] weights;

    public BaseEvaluator(String file){
        this.wp = new WeightsParser();
        this.file = file;
        this.weights = this.wp.getWeights(file);
    }

    public double dot(double[] a1, double[] a2){ // function for dot product
        double res = 0;
        for(int i = 0; i < a1.length; i++){
            res += (a1[i] * a2[i]);
        }
        return res;
    }

    public double evaluate(CheckersGameState s, int player){
        //if(s.isTerminal()){
        //    if(s.winner() == player){
        //        return 1000; // what should this be?
        //    }
        //    else{
        //        return -1000; // assuming only positive evalutions
        //    }
        //}
        double[] params = s.getFeatures(player);
        return dot(this.weights, params);
    }

    public void refreshWeights(){
        this.weights = this.wp.getWeights(this.file);
    }
    public void commitWeights(String path){
        this.wp.writeWeights(path, this.weights); // method to commit weights to beta. provide path to beta csv
    }



}


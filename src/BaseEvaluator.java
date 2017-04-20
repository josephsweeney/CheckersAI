
public class BaseEvaluator implements Evaluator{

    WeightsParser wp;
    String file;
    double[] weights;

    public BaseEvaluator(String file){
        this.wp = new WeightsParser();
        this.file = file;
        this.weights = this.wp.getWeights(file);
    }

    private double dot(double[] a1, double[] a2){
        double res = 0;
        for(int i = 0; i < a1.length; i++){
            res += (a1[i] * a2[i]);
        }
        return res;
    }

    public double evaluate(CheckersGameState s, int player){
        double[] params = s.getFeatures(player);
        return dot(this.weights, params);
    }


}


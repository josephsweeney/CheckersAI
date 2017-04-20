import java.util.ArrayList;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class LearningEvaluator extends BaseEvaluator{

    ArrayList<double[]> params;
    ArrayList<Double> values;
    double alpha; // learning parameter, higher alpha means weights are closer to the regression output
                  // alpha of 1 is directly setting weights to be regression weights
                  // ideally we start at 1 and lower alpha to get a convergence

    public LearningEvaluator(String file, double alpha){
        super(file);
        params = new ArrayList<double[]>();
        values = new ArrayList<Double>();
        this.alpha = alpha;

    }

    public void setAlpha(double a){
        alpha = a;
    }

    public void add_data(double[] features, double value){
        values.add(value);
        params.add(features);
    }

    public void commitWeights(String path){
        this.wp.writeWeights(path, this.weights); // method to commit weights to beta. provide path to beta csv
    }




}

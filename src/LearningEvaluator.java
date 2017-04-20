import java.util.ArrayList;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class LearningEvaluator extends BaseEvaluator{

    ArrayList<double[]> params;
    ArrayList<Double> values;
    OLSMultipleLinearRegression reg; // performs linear regression (ordinary least squares)
    double alpha; // learning parameter, higher alpha means weights are closer to the regression output
                  // alpha of 1 is directly setting weights to be regression weights
                  // ideally we start at 1 and lower alpha to get a convergence

    public LearningEvaluator(String file, double alpha){
        super(file);
        params = new ArrayList<double[]>();
        values = new ArrayList<Double>();
        reg = new OLSMultipleLinearRegression();
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

    public void updateWeights(){
        double[] vals = new double [values.size()];
        for(int i = 0; i < values.size(); i++){
            vals[i] = values.get(i);
        }
        values.clear();
        double[][] pars = new double[params.size()][];
        for(int i=0; i < params.size(); i++){
            pars[i] = params.get(i);
        }
        params.clear();
        reg.newSampleData(vals, pars);
        reg.setNoIntercept(true);
        double[] new_weights = reg.estimateRegressionParameters();
        for(int i = 0; i < this.weights.length; i++){
            this.weights[i] = this.weights[i] + alpha * (new_weights[i] - this.weights[i]);
        }
        commitWeights(this.file);

    }



}

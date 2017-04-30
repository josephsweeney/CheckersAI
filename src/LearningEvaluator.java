import java.util.ArrayList;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import java.util.Arrays;

public class LearningEvaluator extends BaseEvaluator{

    ArrayList<double[]> params;
    ArrayList<Double> values;
    // need to download jar and set classpath to import and run
    OLSMultipleLinearRegression reg; // performs linear regression (ordinary least squares)
    double alpha; // learning parameter, higher alpha means weights are closer to the regression output
                  // alpha of 1 is directly setting weights to be regression weights

    public LearningEvaluator(String file){
        super(file);
        params = new ArrayList<double[]>();
        values = new ArrayList<Double>();
        reg = new OLSMultipleLinearRegression();

    }

    public void addData(double[] features, double value){
        values.add(value);
        params.add(features);
    }

    public void updateWeights(double alpha){
        double[] vals = new double [values.size()]; //converting arraylist to array
        System.out.println("printing values");
        for(int i = 0; i < values.size(); i++){
            vals[i] = values.get(i);
            System.out.println(values.get(i));
        }
        //    System.out.println(vals);
            System.out.println("printing params");
        double[][] pars = new double[params.size()][]; //converting 2d arraylist to array
        for(int i=0; i < params.size(); i++){
            pars[i] = params.get(i);
            System.out.println(Arrays.toString(params.get(i)));
        }
        //System.out.println(pars);
        try {
            reg.newSampleData(vals, pars); //add data
            reg.setNoIntercept(true);
            double[] new_weights = reg.estimateRegressionParameters(); //get parameters
            for(double x: new_weights){
                if(Math.abs(x) > 1000000000){
                    System.out.println("bad data, not updating");
                    return;
                }
            }
            for(int i = 0; i < this.weights.length; i++){
              this.weights[i] = this.weights[i] + alpha * (new_weights[i] - this.weights[i]);
            }
            System.out.println("updated weights " + Arrays.toString(this.weights));
            commitWeights(this.file);
        } catch(SingularMatrixException e) {
            System.out.println("Matrix was singular, not updating weights");
        } catch(MathIllegalArgumentException e){
            System.out.println("Not enough data, not updating end game weights");
        }
        values.clear();
        params.clear();

    }



}

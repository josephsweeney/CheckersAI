import java.util.ArrayList;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.linear.SingularMatrixException;
import java.util.Arrays;

public class LearningEvaluator extends BaseEvaluator{

    ArrayList<double[]> params;
    ArrayList<Double> values;
    // need to download jar and set classpath to import and run
    OLSMultipleLinearRegression reg; // performs linear regression (ordinary least squares)
    double alpha; // learning parameter, higher alpha means weights are closer to the regression output
                  // alpha of 1 is directly setting weights to be regression weights

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

    public void addData(double[] features, double value){
        values.add(value);
        params.add(features);
    }

    public void commitWeights(String path){
        this.wp.writeWeights(path, this.weights); // method to commit weights to beta. provide path to beta csv
    }

    public void updateWeights(){
        // NEED TO CHANGE THIS METHOD
        // using least squares might be a bad idea
        // get a lot of singular matrices
        // we could do samuel's method or come up with another function to modify the coefficients
//        int curr_in = 0;
 //       int data_sz = params.get(0).length + 1;   // need to do regression with data sets of size 10, so each iteration of loop uses 10 lines of data
        //while(params.size() - curr_in > data_sz){
            double[] vals = new double [values.size()]; //converting arraylist to array
            System.out.println("printing values");
            int j = 0;
            for(int i = 0; i < values.size(); i++){
                vals[i] = values.get(i);
                System.out.println(values.get(i));
                j++;
            }
            System.out.println(vals);
            System.out.println("printing params");
            double[][] pars = new double[params.size()][]; //converting 2d arraylist to array
            j=0;
            for(int i=0; i < params.size(); i++){
                pars[i] = params.get(i);
                System.out.println(Arrays.toString(params.get(i)));
                j++;
            }
            System.out.println(pars);
            reg.newSampleData(vals, pars); //add data
            reg.setNoIntercept(true);
            try {
              double[] new_weights = reg.estimateRegressionParameters(); //get parameters
              for(int i = 0; i < this.weights.length; i++){
                  this.weights[i] = this.weights[i] + alpha * (new_weights[i] - this.weights[i]);
              }
              System.out.println("updated weights " + Arrays.toString(this.weights));
              commitWeights(this.file);
            } catch(SingularMatrixException e) {
              System.out.println("Matrix was singular, not updating weights");
            }
            //curr_in += data_sz;
        //}

        //values = new ArrayList<Double>(values.subList(curr_in, values.size()));
        //params = new ArrayList<double[]>(params.subList(curr_in, params.size()));
        values.clear();
        params.clear();

    }



}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class WeightsParser{

    public double[] getWeights(String path){
        BufferedReader br = null;

        try{

            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            String nextline = br.readLine();
            while(nextline != null){
                line = nextline;
                nextline = br.readLine();
            }
            String[] w = line.split(",");
            double[] weights = new double[w.length];
            for(int i = 0; i < weights.length; i++){
                weights[i] = Double.parseDouble(w[i]);
            }
            return weights;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void writeWeights(String path, double[] weights){
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new FileWriter(path, true));
            String res = "";
            for(double w: weights){
                res += w + ", ";
            }
            res = res.substring(0, res.length() -2);
            res += "\n";
            bw.write(res);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                bw.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}



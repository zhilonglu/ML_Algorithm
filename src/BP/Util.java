package BP;

import BP.DataSet;
public class Util {
    public static double dot(double[] X,double[] Y) throws Exception {
        int m=X.length;
        int n=Y.length;
        if(m!=n)
            throw new Exception("error");
        double sum=0;
        for(int i=0;i<m;i++)
            sum+=X[i]*Y[i];
        return sum;
    }
    public static DataSet convertDataSet(String text){
        return null;
    }
}

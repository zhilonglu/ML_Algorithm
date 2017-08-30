package rf2_old;

import rf2_old.DataSet;
import rf2_old.Evaluation;
public class MainTest {
    public static void main(String[] args) {
        // for RandomForest
        System.out.println("for RandomForest");
        String rootpath = "C:\\Users\\NLSDE\\Downloads\\RandomForest_AdaBoost-master\\";
        String[] dataPaths = new String[]{"breast-cancer.data", "segment.data"};
        for (String path : dataPaths) {
            DataSet dataset = new DataSet(rootpath+path);
            // conduct 10-cv 
            Evaluation eva = new Evaluation(dataset, "RandomForest");
            eva.crossValidation();
            // print mean and standard deviation of accuracy
            System.out.println("Dataset:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean() + "," + eva.getAccStd());
        }
        // for AdaBoost
        System.out.println("\nfor AdaBoost");
        for (String path : dataPaths) {
            DataSet dataset = new DataSet(rootpath+path);
            // conduct 10-cv 
            Evaluation eva = new Evaluation(dataset, "AdaBoost");
            eva.crossValidation();
            // print mean and standard deviation of accuracy
            System.out.println("Dataset:" + path + ", mean and standard deviation of accuracy:" + eva.getAccMean() + "," + eva.getAccStd());
        }
    }
}

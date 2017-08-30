package adaboost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import adaboost.DataSet;
import adaboost.Evaluation;
public class MainTest {
	
	public static void main(String[] args) {
		boolean[] isCategory = null;//��Ҫ��ָ��������������ɢ�Ļ��������ģ�1��ʾ��ɢ�ģ�0��ʾ�����ġ�
		double[][] features = null;
		double[][] labels = null;
		int numAttributes = 0;
		int numInstnaces = 0;
		System.out.println("for AdaBoost");
		String rootpath = "C:\\Users\\NLSDE\\Downloads\\RandomForest_AdaBoost-master\\";
		String[] dataPaths = new String[]{"test.data"};
		for (String path : dataPaths) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(rootpath+path)));
				String[] attInfo = reader.readLine().split(","); // attributes info
				numAttributes = attInfo.length - 30;//�˴����õ���60�����ԣ�ÿ��ѵ��ʱ��ֻ��֤һ��71��ֵ
				isCategory = new boolean[numAttributes+1];
				for (int i = 0; i < isCategory.length; i++) {
					isCategory[i] = Integer.parseInt(attInfo[i]) == 1 ? true : false;
				}
				numInstnaces = 0;
				while (reader.readLine() != null) {
					numInstnaces++;
				}
				features = new double[numInstnaces][numAttributes];
				labels = new double[30][numInstnaces];//Ԥ��ֵ����30��,ÿһ����һ��Ԥ�������Ԥ��������ʵֵ
				System.out.println("reading " + numInstnaces + " exmaples with " + numAttributes + " attributes");
				reader = new BufferedReader(new FileReader(new File(rootpath+path)));
				reader.readLine();
				String line;
				int ind = 0;
				while ((line = reader.readLine()) != null) {
					String[] atts = line.split(",");
					for (int i = 0; i < atts.length-30; i++) {
						features[ind][i] = Double.parseDouble(atts[i]);
					}
					for(int i=atts.length-30;i<atts.length;i++)
						labels[i-atts.length+30][ind] = Double.parseDouble(atts[i]);
					ind++;
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int idx=0;idx<30;idx++){
				DataSet dataset = new DataSet(isCategory,features,labels[idx],numAttributes,numInstnaces);
				Evaluation eva = new Evaluation(dataset, "AdaBoost");
				System.out.println(idx+" time,error is :"+eva.crossValidation());
			}
		}
	}
}

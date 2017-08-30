package rf3;
import java.util.List;

public class Main
{
	public static void main(String [] args)
	{
		//CART��ȫ����������ʾ��
		DecisionTree dt = new DecisionTree();
		String trainFile = "C:\\Users\\NLSDE\\Desktop\\hw3_train.dat";
		String testFile = "C:\\Users\\NLSDE\\Desktop\\hw3_test.dat";
		List<DataItem> dataList = Utility.loadData(trainFile);
		List<DataItem> testList = Utility.loadData(testFile);
		//�����������ݴ���CART������
		dt.buildFullCartTree(dataList);
		//���۽��������
		double err = dt.evaluate(testList);
		System.out.println(String.format("ErrorRate: %f", err));
		//���ɭ�ֵ���ʾ��
		RandomForest rf = new RandomForest();
		//���ò�����ɭ�������ĸ����ͳ�������
		int treeCount = 300;
		double sampleRate = 1.0d;
		rf.buildFullCartForest(dataList, treeCount, sampleRate);
		//���۽��������
		err = rf.evaluate(testList);
		System.out.println(String.format("ErrorRate: %f", err));
	}
}

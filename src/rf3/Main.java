package rf3;
import java.util.List;

public class Main
{
	public static void main(String [] args)
	{
		//CART完全决策树调用示例
		DecisionTree dt = new DecisionTree();
		String trainFile = "C:\\Users\\NLSDE\\Desktop\\hw3_train.dat";
		String testFile = "C:\\Users\\NLSDE\\Desktop\\hw3_test.dat";
		List<DataItem> dataList = Utility.loadData(trainFile);
		List<DataItem> testList = Utility.loadData(testFile);
		//根据已有数据创建CART决策树
		dt.buildFullCartTree(dataList);
		//评价结果错误率
		double err = dt.evaluate(testList);
		System.out.println(String.format("ErrorRate: %f", err));
		//随机森林调用示例
		RandomForest rf = new RandomForest();
		//设置参数，森林中树的个数和抽样比例
		int treeCount = 300;
		double sampleRate = 1.0d;
		rf.buildFullCartForest(dataList, treeCount, sampleRate);
		//评价结果错误率
		err = rf.evaluate(testList);
		System.out.println(String.format("ErrorRate: %f", err));
	}
}

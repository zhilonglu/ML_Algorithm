package rf3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;



/**
 * 随机森林模型
 * @author Baogege
 *
 */
public class RandomForest
{
	/**树的数量*/
	protected int treeCount = 300;
	/**抽样个数*/
	protected int bagCount;
	protected static String trainData = "C:\\Users\\NLSDE\\Desktop\\hw3_train.dat";
	
	protected static String testData = "C:\\Users\\NLSDE\\Desktop\\hw3_test.dat";
	
	protected List<DecisionTree> forest;
	
	protected static List<DataItem> globalTrainSet;
	
	protected static List<DataItem> globalTestSet;
	
	static
	{
		globalTrainSet = Utility.loadData(trainData);
		
		globalTestSet = Utility.loadData(testData);
	}
	
	public RandomForest()
	{
		
		forest = new ArrayList<DecisionTree>();
		bagCount = globalTrainSet.size();
	}
	
	public RandomForest(int treeCount, double sampleRate)
	{
		this.treeCount = treeCount;
		this.bagCount = (int)((double)globalTrainSet.size() * sampleRate);
	}
	
	
	
	public int getTreeCount()
	{
	
		return treeCount;
	}



	public void setTreeCount(int treeCount)
	{
	
		this.treeCount = treeCount;
	}



	public List<DataItem> sampleData()
	{
		return sampleData(RandomForest.globalTrainSet, 1);
	}
	
	/**
	 * 按照比例有放回抽样
	 * @param dataList 原始数据
	 * @param sampleRate 抽样比例
	 * @return 抽样结果
	 */
	public List<DataItem> sampleData(List<DataItem> dataList, double sampleRate)
	{
		bagCount = (int)(sampleRate * (double)dataList.size());
		Random random = new Random(System.currentTimeMillis());
		List<DataItem> sampleData = new ArrayList<DataItem>();
		for(int i = 0; i < bagCount; i++)
		{
			int randomIndex = random.nextInt(dataList.size());
			sampleData.add(dataList.get(randomIndex));
		}
		
		return sampleData;
	}
	
	
	public void buildCompletePrunedForest()
	{
		forest = new ArrayList<DecisionTree>();
		for(int i = 0; i < treeCount; i++)
		{
			List<DataItem> dataList = sampleData();
			DecisionTree dt = new DecisionTree();
			dt.buildCompletePrunedTree(dataList);
			forest.add(dt);
		}
	}
	
	
	public void buildFullCartForest()
	{
		buildFullCartForest(RandomForest.globalTrainSet, this.treeCount, 1);
	}
	
	/**
	 * 创建随机森林
	 * @param trainData 数据
	 * @param treeCount 决策树的个数
	 * @param sampleRate 抽样比例
	 */
	public void buildFullCartForest(List<DataItem> trainData, int treeCount, double sampleRate)
	{
		forest = new ArrayList<DecisionTree>();
		for(int i = 0; i < treeCount; i++)
		{
			List<DataItem> dataList = sampleData(trainData, sampleRate);
			DecisionTree dt = new DecisionTree();
			dt.buildFullCartTree(dataList);
			forest.add(dt);
		}
	}
	
	public void buildFullCartForest(int tc)
	{
		forest = new ArrayList<DecisionTree>();
		for(int i = 0; i < tc; i++)
		{
			List<DataItem> dataList = sampleData();
			DecisionTree dt = new DecisionTree();
			dt.buildFullCartTree(dataList);
			forest.add(dt);
		}
	}
	
	public int predict(DataItem sample)
	{
		int result = -1;
		Map<Integer, Integer> predictCount = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < forest.size(); i++)
		{
			DecisionTree dt = forest.get(i);
			int curResult = dt.predict(sample);
			if(predictCount.containsKey(curResult))
				predictCount.put(curResult, predictCount.get(curResult) + 1);
			else
				predictCount.put(curResult, 1);
		}
		
		Iterator<Entry<Integer, Integer>> iter = predictCount.entrySet().iterator();
		int maxCount = 0;
		while(iter.hasNext())
		{
			Entry<Integer, Integer> e = iter.next();
			int count = e.getValue();
			if(count > maxCount)
			{
				maxCount = count;
				result = e.getKey();
			}
			
		}
		return result;
	}
	
	public double evaluate(List<DataItem> dataList)
	{
		double err = 0d;
		int errCount = 0;
		for(DataItem di : dataList)
		{
			int result = predict(di);
			if(result != di.getLabel())
				errCount++;
		}
		err = (double)errCount / (double)dataList.size();
		
		return err;
	}
	
	
	public static double testAllTrees(int treeCount, List<DataItem> dataList)
	{
		double avgErr = 0d, errSum = 0d;
		RandomForest rf = new RandomForest();
		rf.buildFullCartForest(treeCount);
		for(DecisionTree dt : rf.forest)
		{
			errSum += dt.evaluate(dataList);
		}
		avgErr = errSum / (double)treeCount;
		return avgErr;
	}
	
	public static EvaluateMatrix testFullCartForest(int testCount)
	{
		EvaluateMatrix e = new EvaluateMatrix();
		double sumEin = 0d, sumEout = 0d;
		for(int i = 0; i < testCount; i++)
		{
			System.out.println(String.format("Test Random Forest %d", i + 1));
			RandomForest rf = new RandomForest();
			rf.buildFullCartForest();
			sumEin += rf.evaluate(globalTrainSet);
			sumEout += rf.evaluate(globalTestSet);
		}
		double Ein = sumEin / (double)(testCount);
		double Eout = sumEout / (double)testCount;
		e.setEin(Ein);
		e.setEout(Eout);
		return e;
	}
	
	public static EvaluateMatrix testCompletePrunedForest(int testCount)
	{
		EvaluateMatrix e = new EvaluateMatrix();
		double sumEin = 0d, sumEout = 0d;
		for(int i = 0; i < testCount; i++)
		{
			System.out.println(String.format("Test Random Forest %d", i + 1));
			RandomForest rf = new RandomForest();
			rf.buildCompletePrunedForest();
			sumEin += rf.evaluate(globalTrainSet);
			sumEout += rf.evaluate(globalTestSet);
		}
		double Ein = sumEin / (double)(testCount);
		double Eout = sumEout / (double)testCount;
		e.setEin(Ein);
		e.setEout(Eout);
		return e;
	}
	
	
	public static void main(String [] args)
	{
		double tEin = testAllTrees(1, globalTrainSet);
		
		double sEin, sEout;
		EvaluateMatrix sE = testFullCartForest(100);
		sEin = sE.getEin();
		sEout = sE.getEout();
		//sEout = testFullCartForest(100, globalTestSet);
		
		double pEin, pEout;
		EvaluateMatrix pE = testCompletePrunedForest(100);
		pEin = pE.getEin();
		pEout = pE.getEout();
		//pEout = testCompletePrunedForest(100, globalTestSet);
		
		System.out.println(String.format("AvgTree:%f\nFull:%f,%f\nPruned:%f,%f", tEin, sEin, sEout, pEin, pEout));
		
		
	}
	
	
	
	
	
	
	
	
	
}

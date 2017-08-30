package gbdt2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.spi.TimeZoneNameProvider;

public class Tree {
	private Tree leftTree=new Tree();
	private Tree rightTree=new Tree();
	private double loss=-1;
	private int attributeSplit=0;
	private String attributeSplitType="";
	boolean isLeaf;
	double leafValue;
	private ArrayList<Integer> leafNodeSet=new ArrayList<>();
	
	public ArrayList<String> getAttributeSet(ArrayList<ArrayList<String>> trainData,int idx)
	{
		HashSet<String> mySet=new HashSet<>();
		ArrayList<String> ans =new ArrayList<>();
		for(int i=0;i<trainData.size();i++)
		{
			mySet.add(trainData.get(i).get(idx));
		}
		
		Iterator<String> it=mySet.iterator();
		
		while(it.hasNext())
		{
			ans.add(it.next());
		}
		
		return ans;
	}
	public boolean myCmpLess(String str1,String str2)
	{
		if(Integer.parseInt(str1.trim())<=Integer.parseInt(str2.trim()))
			return true;
		else return false;
		
	}
	public double computeLoss(ArrayList<Double> values)
	{
		double loss=0;
		for(int i=0;i<values.size();i++)
		{
			loss+=values.get(i);
		}
		double mean=loss/values.size();
		loss=0;
		for(int i=0;i<values.size();i++)
		{
			loss+=Math.pow(values.get(i)-mean,2);
		}
		return Math.sqrt(loss);
	}
	public double getPredictValue(int K, ArrayList<Integer> subIdx,ArrayList<Double> target) {
		double ans=0;
		double sum=0,sum1=0;
		for(int i=0;i<subIdx.size();i++)
		{
			sum+=target.get(subIdx.get(i));
		}
		for(int i=0;i<subIdx.size();i++)
		{
			sum1+=target.get(subIdx.get(i))*(1-target.get(subIdx.get(i)));
		}
		ans=(K-1)/K*sum/sum1;
		return ans;
	}
	public double getPredictValue(Tree root)
	{
		return root.leafValue;
	}
	public double getPredictValue(Tree root,ArrayList<String> instance,Boolean isDigit[])
	{
		
		if(root.isLeaf)
			return root.leafValue;
		else if(isDigit[root.attributeSplit])
		{
			if(myCmpLess(instance.get(root.attributeSplit).trim(),root.attributeSplitType))
				return getPredictValue(root.leftTree, instance, isDigit);
			return getPredictValue(root.rightTree, instance, isDigit);
		}
		else
		{
			if(instance.get(root.attributeSplit).trim().equals(root.attributeSplitType))
				return getPredictValue(root.leftTree, instance, isDigit);
			return getPredictValue(root.rightTree, instance, isDigit);
		}
		
	}
	public Tree constructTree(ArrayList<ArrayList<Integer>> leafNodes,ArrayList<Double> leafValues,int K,int splitPoints, Boolean isDigit[],ArrayList<Integer> subIdx,ArrayList<ArrayList<String>> trainData,ArrayList<Double> target,int maxDepth[],int depth)
	{
		
		int n=trainData.size();
		int dim=trainData.get(0).size();
		ArrayList<Integer> leftTreeIdx=new ArrayList<>();
		ArrayList<Integer> rightTreeIdx=new ArrayList<>();
		
		if(depth<maxDepth[0])
		{
			/*
			 * 从所有的attribute中选取最佳的attribute，并且attribute中最佳的分割点，对数据进行分割
			 * */
			double loss=-1;
			ArrayList<Integer> leftNodes=new ArrayList<>();
			ArrayList<Integer> rightNodes=new ArrayList<>();
			int attributeSplit=0;
			String attributeSplitType="";
			
			for(int i=0;i<dim;i++)//遍历所有的attribute
			{
				//得到该attribute下所有的distinct的值
				ArrayList<String> myAttributeSet=new ArrayList<>();
				ArrayList<String> subDigitAttribute=new ArrayList<>();
				myAttributeSet=getAttributeSet(trainData, i);
				if(isDigit[i])//如果是数字，就从数组中随机选取splitpoints个节点，代表这个属性可以在这splitpoints下进行分割
				{
					while(subDigitAttribute.size()<splitPoints)
					{
						Random r=new Random();
						int tmp=r.nextInt(myAttributeSet.size());
						subDigitAttribute.add(myAttributeSet.get(tmp));
						myAttributeSet.clear();
						myAttributeSet=subDigitAttribute;
					}
				}
				for(int j=0;j<myAttributeSet.size();j++)
				{
					for(int k=0;k<subIdx.size();k++)
					{
						if((!isDigit[i]&&trainData.get(subIdx.get(k)).get(i).trim().equals(myAttributeSet.get(j)))||(isDigit[i]&&myCmpLess(trainData.get(subIdx.get(k)).get(i),myAttributeSet.get(j))))
						{
							leftTreeIdx.add(subIdx.get(k));
						}
						else
						{
							rightTreeIdx.add(subIdx.get(k));
						}
					}
					ArrayList<Double> leftTarget=new ArrayList<>();
					ArrayList<Double> rightTarget=new ArrayList<>();
					for(int k=0;k<leftTreeIdx.size();k++)
						leftTarget.add(target.get(leftTreeIdx.get(k)));
					for(int k=0;k<rightTreeIdx.size();k++)
						rightTarget.add(target.get(rightTreeIdx.get(k)));
					double lossTmp=computeLoss(leftTarget)+computeLoss(rightTarget);	
					if(loss<0||loss<lossTmp)
					{
						leftNodes.clear();
						rightNodes.clear();
						for(int k=0;k<leftTreeIdx.size();k++)
							leftNodes.add(leftTreeIdx.get(k));
						for(int k=0;k<rightTreeIdx.size();k++)
							rightNodes.add(rightTreeIdx.get(k));
						attributeSplit=i;
						attributeSplitType=myAttributeSet.get(j);
					}
					
				}
						
			}
			
			Tree tmpTree=new Tree();
			tmpTree.attributeSplit=attributeSplit;
			tmpTree.attributeSplitType=attributeSplitType;
			tmpTree.loss=loss;
			tmpTree.isLeaf=false;
			tmpTree.leftTree=constructTree(leafNodes,leafValues,K,splitPoints, isDigit, leftNodes, trainData, target, maxDepth, depth+1);
			tmpTree.leftTree=constructTree(leafNodes,leafValues,K,splitPoints, isDigit, rightNodes, trainData, target, maxDepth, depth+1);
			return tmpTree;
			
		}
		else
		{
			Tree tmpTree=new Tree();
			tmpTree.isLeaf=true;
			tmpTree.leafValue=getPredictValue(K, subIdx, target);
			for(int i=0;i<subIdx.size();i++)
				tmpTree.leafNodeSet.add(subIdx.get(i));
			leafNodes.add(subIdx);
			leafValues.add(tmpTree.leafValue);
			return tmpTree;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tree aTree=new Tree();
	}

}

package gbdt;
import java.io.IOException;

public class GradientBoostedTrees {
	//n_tree��ʾ���ĸ���,learn_rate��ʾѧϰ��
	int n_tree;
	double learn_rate;
	int layercount;
	String loss;
	GradientBoostedTreesModel model;
	public GradientBoostedTrees()
	{
	}
	public GradientBoostedTrees(int n_tree,double learn_rate,int layercount,String loss)
	{
		this.n_tree=n_tree;
		this.learn_rate=learn_rate;
		this.layercount=layercount;
		this.loss=loss;
	}
	public  double getInitPredictValue(series[] traindata)
	{
		double init_sum=0;
		for (int i=0;i<traindata.length;i++)
		{
			init_sum=init_sum+traindata[i].getY();
		}
		return init_sum/traindata.length;
	}
	public series[] getNegGradientSeries(series[] true_data,series[] predict_data)
	{
		//��ʱ���ݶ�Ϊ yi-F(xi)
		if(loss.equals("ls"))
		{
			for (int i=0;i<true_data.length;i++)
			{
				predict_data[i].setY(true_data[i].getY()-predict_data[i].getY());
			}
		}
		return predict_data;
	}
	public GradientBoostedTreesModel fit(series[] traindata)
	{
		GradientBoostedTreesModel model=new GradientBoostedTreesModel(learn_rate,loss);
		double init_y=getInitPredictValue(traindata);
		model.addInitModel(init_y);
		for (int i=0;i<n_tree;i++)
		{
			System.out.println("��"+i+"�����ڹ���");
			series[] predict=model.predict(traindata);
			series[] negGradientSeries=getNegGradientSeries(traindata,predict);
			cartRegressionTree treeModel=new cartRegressionTree(negGradientSeries);
			treeModel.build(layercount);
			model.addCartTreeModel(treeModel);
		}
		this.model=model;
		return model;
	}
	//����Ԥ��
	public series[] predict(series[] predict_series)
	{
		return model.predict(predict_series);
	}
	//����ģ�͵�Ӳ��
	public void saveModelToDisk(String filename)throws IOException
	{
		model.saveModelToDisk(filename);
	}
	//��Ӳ���ж�ȡģ��
	public void loadModelFromDisk(String filename)throws IOException
	{
		this.model=new GradientBoostedTreesModel();
		this.model.loadModelFromDisk(filename);
	}
	public static void main(String[] args)throws IOException
	{
		long begin=System.currentTimeMillis();
		series[] traindata=cartRegressionTree.getTrainData();
		int count=6;
		series[] newl=new series[count];
		for (int i=0;i<count;i++)
		{
			newl[i]=traindata[i];
		}
		System.out.println("����������"+traindata.length);
		GradientBoostedTrees gbdt=new GradientBoostedTrees(1000,0.1,5,"ls");
		gbdt.fit(traindata);
		long end=System.currentTimeMillis();
		System.out.println("ʱ�䣺"+(end-begin));
		gbdt.saveModelToDisk("gbdt.model");
		/*
        GradientBoostedTrees gbdt=new GradientBoostedTrees();
        gbdt.loadModelFromDisk("gbdt.model");
        newl=gbdt.predict(newl);
        for (int i=0;i<newl.length;i++)
        {
            System.out.println(newl[i]);
        }
		 */
	}
}

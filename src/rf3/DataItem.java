package rf3;
import rf3.structure.SparseVector;



/**
 * 数据样本
 * @author Baogege
 *
 */
public class DataItem
{
	protected SparseVector features;
	
	protected int featureCount = 2;
	
	protected int label;
	
	
	public double getFeature(int index)
	{
		return features.getValue(index);
	}
	
	public int getLabel()
	{
		return label;
	}

	public SparseVector getFeatures()
	{
	
		return features;
	}

	public void setFeatures(SparseVector features)
	{
	
		this.features = features;
	}
	
	public int getFeatureCount()
	{
	
		return featureCount;
	}

	public void setFeatureCount(int featureCount)
	{
		this.featureCount = featureCount;
		this.features = new SparseVector(featureCount);
	}

	public void setFeature(int index, double feature)
	{
		if(features == null)
			features = new SparseVector(featureCount);
		features.setValue(index, feature);
	}

	public void setLabel(int label)
	{
	
		this.label = label;
	}
	
	
	
	
}

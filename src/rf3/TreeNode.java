package rf3;
/**
 * 决策树结点
 * @author Baogege
 *
 */
public class TreeNode
{
	protected int featureIndex = 0;
	
	protected double value;
	
	protected TreeNode leftChild;
	
	protected TreeNode rightChild;
	
	protected int label = 0;

	public int getFeatureIndex()
	{
	
		return featureIndex;
	}

	public void setFeatureIndex(int featureIndex)
	{
	
		this.featureIndex = featureIndex;
	}

	public double getValue()
	{
	
		return value;
	}

	public void setValue(double value)
	{
	
		this.value = value;
	}

	public TreeNode getLeftChild()
	{
	
		return leftChild;
	}

	public void setLeftChild(TreeNode leftChild)
	{
	
		this.leftChild = leftChild;
	}

	public TreeNode getRightChild()
	{
	
		return rightChild;
	}

	public void setRightChild(TreeNode rightChild)
	{
	
		this.rightChild = rightChild;
	}

	public int getLabel()
	{
	
		return label;
	}

	public void setLabel(int label)
	{
	
		this.label = label;
	}
	
	public void copy(TreeNode node)
	{
		this.featureIndex = node.getFeatureIndex();
		this.label = node.getLabel();
		this.leftChild = node.getLeftChild();
		this.rightChild = node.getRightChild();
		this.value = node.getValue();
	}
	
}

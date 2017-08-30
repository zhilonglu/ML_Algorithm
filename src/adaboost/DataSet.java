package adaboost;
public class DataSet {
	private boolean[] isCategory;//��Ҫ��ָ��������������ɢ�Ļ��������ģ�1��ʾ��ɢ�ģ�0��ʾ�����ġ�
	private double[][] features;
	private double[] labels;
	private int numAttributes;
	private int numInstnaces;
	public DataSet(boolean[] para_isCategory,double[][] para_features,double[] para_labels,
			int para_numAttributes,int para_numInstnaces) {
		this.isCategory = para_isCategory;
		this.features = para_features;
		this.labels = para_labels;
		this.numAttributes = para_numAttributes;
		this.numInstnaces = para_numInstnaces;
	}
	public boolean[] getIsCategory() {
		return isCategory;
	}
	public double[][] getFeatures() {
		return features;
	}
	public double[] getLabels() {
		return labels;
	}
	public int getNumAttributes() {
		return numAttributes;
	}
	public int getNumInstnaces() {
		return numInstnaces;
	}
}
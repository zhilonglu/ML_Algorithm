package RF;
import java.util.ArrayList;

public class Node {
	public ArrayList<Node> Child;//�ڵ�ĺ���
	public ArrayList<Double> value;//�ڵ㺢�Ӷ�Ӧ������ֵ
	public int test_cond; //�ڵ��Ӧ�ĸ�����ֵ���л���
	public int label; //�ڵ��ǩ
	public double test_value; //��Ӧ���������ԵĻ��ֵ��ֵ
	public Node()
	{
		this.Child = new ArrayList<Node>();
		this.value = new ArrayList<Double>();
		this.test_cond = -1;
		this.label = -1;//-1��ʾ�����
		this.test_value = -1;//-1��ʾ��ɢ��
	}
	
}

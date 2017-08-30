package gbdt;
public class TreeNode {
    //�ýڵ���ʣ�µ���������
    int[] left;
    //��Ǹýڵ��Ƿ���Ҷ�ӽڵ�
    int flag;
    //�����Ҷ�ӽڵ㣬��Ҷ�ӽڵ��ʾ�Ļع�ֵ
    double regression;
    //�ýڵ�ķ�������
    int split_index;
    //�ýڵ�ķ������ԵĻ���ֵ
    double split_value;
    //��Ҫ���浽ģ��������ֵ����
    int value_to_model_save=4;
    //�ýڵ���minsum��ֵ
    double minsum=0;
    //�ýڵ�λ�ڵ���һ���minsum���ܺ�
    double layerminsum=0;
    //�չ��캯��
    public TreeNode()
    {

    }
    //���ļ��У�ʹ���ַ����������ڵ�
    public TreeNode(String str)
    {
        loadFromModelStr(str);
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("\t�Ƿ���Ҷ�ӽڵ�: "+flag+"\t");
        stringBuilder.append("\t��Ҷ�ӽڵ����������Ŀ: "+left.length+"\t");
        stringBuilder.append("\t������������: "+split_index+"\t");
        stringBuilder.append("\t��������ֵ: "+split_value+"\t");
        stringBuilder.append("\t�ع�ֵ: "+regression);
        stringBuilder.append("\t�������ֵ: "+minsum);
        stringBuilder.append("\t�ò����ֵ: "+layerminsum);

        return stringBuilder.toString();
    }
    public String saveToModelStr()
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("{"+flag+","+regression+","+split_index+","+split_value+"}");
        return stringBuilder.toString();
    }
    //�����str��ʽ������{} ���ָ�ʽ
    public void loadFromModelStr(String str)
    {
        if(!str.startsWith("{")||!str.endsWith("}"))
        {
            System.out.println("���ڵ��ʽ����");
        }
        else
        {
            String s=str.substring(1,str.length()-1);
            String[] var=s.split(",");
            if(var.length!=value_to_model_save)
            {
                System.out.println("���ڵ��ʽ����");
            }
            else
            {
                try
                {
                    this.flag=Integer.valueOf(var[0]);
                    this.regression=Double.valueOf(var[1]);
                    this.split_index=Integer.valueOf(var[2]);
                    this.split_value=Double.valueOf(var[3]);
                }
                catch (Exception e)
                {
                    System.out.println("���ڵ��ʽ����");
                }
            }
        }
    }
}

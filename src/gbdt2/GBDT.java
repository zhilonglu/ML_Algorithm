package gbdt2;
import java.rmi.server.SkeletonNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
 
 
public class GBDT {
     
    private ArrayList<ArrayList<String>> datas=new ArrayList<ArrayList<String>>();
    private ArrayList<String> labelSets=new ArrayList<>();
    private ArrayList<ArrayList<Double>> F=new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<Double>> residual=new ArrayList<ArrayList<Double>>();
    private ArrayList<ArrayList<String>> trainData=new ArrayList<ArrayList<String>>();
    private ArrayList<Integer> labelTrainData=new ArrayList<Integer>();
    private int K;
    private Boolean isDigit[];
    private int dim;
    private int n;
    private double learningRate;
     
    private ArrayList<ArrayList<Tree>> trees=new ArrayList<ArrayList<Tree>>(); //存放所有的树
     
    private int max_iter;
    private double sampleRate;
    private int maxDepth;
    private int splitPoints;
 
    public void computeResidual(ArrayList<Integer> subId)
    {
        for(int i=0;i<subId.size();i++)
        {
            int idx=subId.get(i);
            int y=0;
            if(this.labelTrainData.get(idx)==-1) y=0;
            else y=1;
            double sum=Math.exp(this.F.get(idx).get(0))+Math.exp(this.F.get(idx).get(1));
            double p1=Math.exp(this.F.get(idx).get(0))/sum,p2=Math.exp(this.F.get(idx).get(1))/sum;
            this.residual.get(idx).set(0, y-p1);
            this.residual.get(idx).set(1, y-p2);
        }
    }
    public ArrayList<Integer> myrandom(int maxNum,int num)
    {
        ArrayList<Integer> ans=new ArrayList<>();
        Set<Integer> mySet=new HashSet<>();
        while(mySet.size()<num)
        {
            Random r=new Random();
            int tmp=r.nextInt(maxNum);
            mySet.add(tmp);
        }
        Iterator<Integer> it=mySet.iterator();
        while(it.hasNext())
        {
            ans.add(it.next());
        }
        return ans;
    }
     
    public GBDT()
    {
        this.max_iter=50;
        this.sampleRate=0.8;
        this.K=2;//2分类问题
        this.maxDepth=6;
        this.splitPoints=3;
        this.learningRate=0.01;
        getData();
    }
     
    public void train()
    {
        for(int i=0;i<max_iter;i++)
        {
            ArrayList<Integer> subSet=new ArrayList<>();
            int numSubset=(int)(this.n*this.sampleRate);
            subSet=myrandom(this.n,numSubset);
            computeResidual(subSet);
            ArrayList<Double> target=new ArrayList<>();
            ArrayList<Tree> tmpTree=new ArrayList<>();
            int maxdepths[]={this.maxDepth};
            for(int j=0;j<this.K;j++)
            {
                target.clear();
                for(int k=0;k<subSet.size();k++)
                {
                    target.add(residual.get(subSet.get(k)).get(j));
                }
                ArrayList<ArrayList<Integer>> leafNodes=new ArrayList<ArrayList<Integer>>();
                ArrayList<Double> leafValues=new ArrayList<>();
                Tree treeSub=new Tree();
                Tree iterTree=treeSub.constructTree(leafNodes,leafValues,K,splitPoints, isDigit, subSet, trainData, target,maxdepths,0);
                tmpTree.add(iterTree);
                updateFvalue(isDigit, subSet,leafNodes,leafValues,j,iterTree);
            }
             
            trees.add(tmpTree);
        }
    }
     
    public void updateFvalue(Boolean isDigit[], ArrayList<Integer> subIdx,ArrayList<ArrayList<Integer>> leafNodes,ArrayList<Double> leafValues,int label,Tree root)
    {
        ArrayList<Integer> remainIdx=new ArrayList<>();
        int arr[]=new int[this.n];
        for(int i=0;i<this.n;i++)
            arr[i]=i;
        for(int i=0;i<subIdx.size();i++)
        {
            arr[subIdx.get(i)]=-1;
        }
        //求出不是用来训练树的余下集合
        for(int i=0;i<this.n;i++)
        {
            if(arr[i]!=-1)
                remainIdx.add(i);
        }
        for(int i=0;i<leafNodes.size();i++)
        {
            for(int j=0;j<leafNodes.get(i).size();j++)
            {
                this.F.get(leafNodes.get(i).get(j)).set(label, this.F.get(leafNodes.get(i).get(j)).get(label)+this.learningRate*root.getPredictValue(root));
            }
        }
        for(int i=0;i<remainIdx.size();i++)
        {
            double leafV=root.getPredictValue(root,this.trainData.get(remainIdx.get(i)),isDigit);
            this.F.get(remainIdx.get(i)).set(label, this.F.get(remainIdx.get(i)).get(label)+this.learningRate*leafV);
        }
         
         
    }
     
    public boolean checkDigit(String str) {
        for(int i=0;i<str.length();i++)
        {
            if(!(str.charAt(i)>='0'&&str.charAt(i)<='9'))
            {
                return false;
            }
        }
        return true;
    }
     
    public void getData() {
        Data d =new Data();
        this.datas=d.getTrainData();
        this.dim=this.datas.get(0).size()-1;
        this.isDigit=new Boolean[this.dim];
        //遍历所有样本，去掉中间含有不是正常的数据
        for(int i=0;i<this.datas.get(0).size()-1;i++)
            labelSets.add(this.datas.get(0).get(i));
        //保证数据的第一行是正确的，来判断，特征哪些纬度是数字，哪些纬度是字符串
        for(int i=0;i<this.dim;i++)
        {
            if(checkDigit(this.datas.get(0).get(i)))
                this.isDigit[i]=true;
            else this.isDigit[i]=false;
        }
        //如果字符串==？说明是异常数据，这里做数据的清理
        for(int i=1;i<this.datas.size();i++)
        {
            ArrayList<String> tmp=new ArrayList<>();
            boolean flag=true;
            for(int j=0;j<this.dim;j++)
            {
                if(datas.get(i).get(j).trim().equals("?"))
                {
                    flag=false;
                    break;
                }
            }
            if(!flag) continue;
            if(datas.get(i).get(this.dim).trim().equals("?")) continue;
            trainData.add(tmp);
            if(datas.get(i).get(this.dim).trim().equals("<=50K"))
                labelTrainData.add(-1);
            else
                labelTrainData.add(1);
             
        }
        this.n=this.labelTrainData.size();
         
        for(int i=0;i<this.datas.get(0).size()-1;i++)
            labelSets.add(this.datas.get(0).get(i));
         
        //初始化F矩阵为全0,F矩阵是n*2，是2分类问题，如果要多分类，改下这里就可以了
        for(int i=0;i<this.n;i++)
        {
            ArrayList<Double> arrTmp=new ArrayList<Double>();
            for(int j=0;j<2;j++)
            {
                arrTmp.add(0.0);
            }
            this.F.add(arrTmp);
            this.residual.add(arrTmp);
        }
         
                             
    }
     
    public static void main(String[] args) {
        GBDT dGbdt=new GBDT();
        dGbdt.getData();
        System.err.println(dGbdt.n);
         
    }
}
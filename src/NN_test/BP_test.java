package NN_test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import NN.BP3;
import NN_test.process;
public class BP_test{
	public double[][] layer;//神经网络各层节点
	public double[][] layerErr;//神经网络各节点误差
	public double[][][] layer_weight;//各层节点权重
	public double[][][] layer_weight_delta;//各层节点权重动量
	public double mobp;//动量系数
	public double rate;//学习系数

	public BP_test(int[] layernum, double mobp,double rate){
		this.mobp = mobp;
		this.rate = rate;
		layer = new double[layernum.length][];
		layerErr = new double[layernum.length][];
		layer_weight = new double[layernum.length][][];
		layer_weight_delta = new double[layernum.length][][];
		Random random = new Random();
		for(int l=0;l<layernum.length;l++){
			layer[l]=new double[layernum[l]];
			layerErr[l]=new double[layernum[l]];
			if(l+1<layernum.length){
				layer_weight[l]=new double[layernum[l]+1][layernum[l+1]];
				layer_weight_delta[l]=new double[layernum[l]+1][layernum[l+1]];
				for(int j=0;j<layernum[l]+1;j++)
					for(int i=0;i<layernum[l+1];i++){
						double real = random.nextDouble();
						layer_weight[l][j][i] = random.nextDouble() > 0.5 ? real : -real;//随机初始化权重
					}
			}   
		}
	}
	//逐层向前计算输出
	public double[] computeOut(double[] in){
		for(int l=1;l<layer.length;l++){
			for(int j=0;j<layer[l].length;j++){
				double z=layer_weight[l-1][layer[l-1].length][j];
				for(int i=0;i<layer[l-1].length;i++){
					layer[l-1][i]=l==1?in[i]:layer[l-1][i];
					z+=layer_weight[l-1][i][j]*layer[l-1][i];
				}
				layer[l][j]= relu(z);//此处不需要利用sigmod函数，利用relu函数
			}
		}
		return layer[layer.length-1];
	}
	//逐层反向计算误差并修改权重
	public double updateWeight(double[] tar){
		int l=layer.length-1;
		double error = 0;
		for(int j=0;j<layerErr[l].length;j++){
			if(layer[l][j]>0)
				layerErr[l][j]=1*sign(tar[j]-layer[l][j])/tar[j];
			else
				layerErr[l][j]=0*sign(tar[j]-layer[l][j])/tar[j];//此处根据损失函数计算误差
			error += Math.abs(tar[j]-layer[l][j])/tar[j];
		}
		while(l-->0){
			for(int j=0;j<layerErr[l].length;j++){
				double z = 0.0;
				for(int i=0;i<layerErr[l+1].length;i++){
					z=z+l>0?layerErr[l+1][i]*layer_weight[l][j][i]:0;
					layer_weight_delta[l][j][i]= mobp*layer_weight_delta[l][j][i]+rate*layerErr[l+1][i]*layer[l][j];//隐含层动量调整
					layer_weight[l][j][i]+=layer_weight_delta[l][j][i];//隐含层权重调整
					if(j==layerErr[l].length-1){
						layer_weight_delta[l][j+1][i]= mobp*layer_weight_delta[l][j+1][i]+rate*layerErr[l+1][i];//截距动量调整
						layer_weight[l][j+1][i]+=layer_weight_delta[l][j+1][i];//截距权重调整
					}
				}
				if(z>0)
					layerErr[l][j] = z;
				else
					layerErr[l][j] = 0;
			}
		}
		return error;
	}
	//符号函数
	public double sign(double x){
		if(x==0)
			return 0;
		else if(x>0)
			return 1;
		else
			return -1;
	}
	private double relu(double val){
		return val>0?val:0;
	}
	public double train(double[] in, double[] tar){
		double[] out = computeOut(in);
		return updateWeight(tar);
	}
	public static void main(String[] args){
		BP3 obj = new BP3(60,120,30);
		HashMap<String,Double[]> link_Value = new HashMap<String,Double[]>();
		process.readJson();
		process.initOutputKey();
		HashMap<String,String[]> link_key = process.link_key;
		String rootpath =  "C:\\Users\\NLSDE\\Desktop\\GZ_kdd\\tensorData4\\";
		for(int tf=1;tf<133;tf++){
			Date nowTime = new Date();
			System.out.println(nowTime);
			System.out.println("link:"+String.valueOf(tf)+" is running......");
			process.loadData(rootpath+String.valueOf(tf)+"\\tensor_fill.csv");
			double[][] data = process.trainX;
			double[][] targetData = process.trainY;
			for(int n=0;n<10000;n++){
				obj.trainnum=n;
				for(int i=0;i<data.length;i++){
					obj.train(data[i], targetData[i]);
				}
			}
			double[][] preX = process.preX;
			Double[] value = new Double[210];
			for(int i=0;i<preX.length;i++){
				double[] result = obj.test(preX[i]);
				for(int k=0;k<result.length;k++)
					value[i*30+k] = result[k];
			}
			link_Value.put(String.valueOf(tf),value);
			System.out.println("link:"+String.valueOf(tf)+" finished......");
		}
		BufferedWriter bw=null;
		try {
			bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\NLSDE\\Desktop\\bpnn500.txt")));
			for(Map.Entry<String, String[]> entry:link_key.entrySet()){
				String key = entry.getKey();
				String[] lKey = entry.getValue();
				Double[] lValue = link_Value.get(key);
				for(int i=0;i<lKey.length;i++)
					bw.write(lKey[i]+"#"+lValue[i]+"\n");
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

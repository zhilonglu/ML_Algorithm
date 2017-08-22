package BP;

import BP.DataGroup;
import BP.DataSet;
import BP.ActivateFunction;

import java.util.ArrayList;
import java.util.Random;

public class BPNN {
	public static Random random=new Random(System.currentTimeMillis());
	Layer[] layers;
	int layerNumber;
	public double trainError;
	public double testError;
	int maxIter;
	double minError;
	String netStructure;
	double speed,momentum;
	public DataSet dataSet;
	public int xnumber,ynumber;
	public int nmaxtrainnumber=0;
	public ArrayList trainErrorlist;
	public ArrayList testErrorlist;
	public int testrate=7;
	public ActivateFunction activateFunction;
	public BPNN(int maxIter, double minError, String netStructure, double speed, double momentum, ActivateFunction activateFunction) {
		this.maxIter = maxIter;
		this.minError = minError;
		this.netStructure = netStructure;
		this.speed = speed;
		this.momentum = momentum;
		this.trainErrorlist =new ArrayList();
		this.testErrorlist=new ArrayList();
		trainError =Double.MAX_VALUE;
		testError=Double.MAX_VALUE;
		this.activateFunction=activateFunction;
		divideStructure(netStructure);
	}
	void divideStructure(String netStructure){
		String[] ss= netStructure.split("-");
		layerNumber=ss.length;
		layers=new Layer[layerNumber];
		int iNumber= Integer.parseInt(ss[0]);
		layers[0]=new InputLayer(iNumber,0);
		if(activateFunction==null){
			for(int i=1;i<layerNumber;i++){
				int n=Integer.parseInt(ss[i]);
				layers[i]=new CalculateableLayer(n,i,layers[i-1].nodeNumber);
			}
		}else{
			for(int i=1;i<layerNumber;i++){
				int n=Integer.parseInt(ss[i]);
				layers[i]=new CalculateableLayer(n,i,layers[i-1].nodeNumber,activateFunction);
			}
		}
		xnumber=iNumber;
		ynumber=Integer.parseInt(ss[layerNumber-1]);
	}
	public void train(DataSet dataSet) throws Exception {
		for(int iter=0;iter<maxIter;iter++){
			if(trainError <=minError)
				break;
			clearAverageError();
			trainError =0;
			for(int datacount = 0; datacount< dataSet.trainGroups.size(); datacount++){
				double[] dX=dataSet.trainGroups.get(datacount).inputs;
				for(int i=0;i<dX.length;i++){
					InputNode node = (InputNode) layers[0].nodes[i];
					node.x=dX[i];
				}
				_forward();
				trainError +=calculateSingleError(datacount,dataSet.trainGroups);
				setOutputAverageError(datacount);
				backward();
				refreshNet();
			}
			trainError /=(dataSet.trainGroups.size() *2.0);
			trainErrorlist.add(trainError);
			testError=0;
			for(int datacount=0;datacount<dataSet.testGroups.size();datacount++){
				double[] dX=dataSet.testGroups.get(datacount).inputs;
				for(int i=0;i<dX.length;i++){
					InputNode node = (InputNode) layers[0].nodes[i];
					node.x=dX[i];
				}
				_forward();
				testError +=calculateSingleError(datacount,dataSet.testGroups);
			}
			testError/=(dataSet.testGroups.size()*2.0);
			testErrorlist.add(testError);
			nmaxtrainnumber++;
		}
	}
	public double[] predict(double[] X) throws Exception {
		int nX=layers[0].nodeNumber;
		int nY=layers[layerNumber-1].nodeNumber;
		if(X.length!=nX)
			return null;
		double[] Y=new double[nY];
		double[] newX=X.clone();
		newX=generateOne(newX);
		for(int i=0;i<newX.length;i++){
			InputNode node = (InputNode) layers[0].nodes[i];
			node.x=newX[i];
		}
		for(int layercount=1;layercount<layerNumber;layercount++){
			CalculateableLayer layer=(CalculateableLayer) layers[layercount];
			Layer lastlayer=layers[layercount-1];
			double[] stempX=lastlayer.getOutput();
			for(int nodecount=0;nodecount<layer.nodeNumber;nodecount++){
				CalculateableNode node= (CalculateableNode) layer.nodes[nodecount];
				node.activate(stempX);
			}
		}
		Layer outputlayer=layers[layerNumber-1];
		for(int i=0;i<outputlayer.nodeNumber;i++){
			Y[i]=outputlayer.nodes[i].x;
		}
		degenerateOne(Y);
		return Y;
	}
	public double[] generateOne(double[] X){
		for(int i=0;i<X.length;i++){
			X[i]=(X[i]-dataSet.minX[i])/(dataSet.maxX[i]-dataSet.minX[i])*(dataSet.omaxX-dataSet.ominX)+dataSet.ominX;
		}
		return X;
	}
	public double[] degenerateOne(double[] Y){
		for(int i=0;i<Y.length;i++){
			Y[i]=(Y[i]-dataSet.ominY)/(dataSet.omaxY-dataSet.ominY)*(dataSet.maxY[i]-dataSet.minY[i])+dataSet.minY[i];
		}
		return Y;
	}
	public void _forward() throws Exception {
		for(int layercount=1;layercount<layerNumber;layercount++){
			CalculateableLayer layer=(CalculateableLayer) layers[layercount];
			Layer lastlayer=layers[layercount-1];
			double[] X=lastlayer.getOutput();
			for(int nodecount=0;nodecount<layer.nodeNumber;nodecount++){
				CalculateableNode node= (CalculateableNode) layer.nodes[nodecount];
				node.activate(X);
			}
		}
	}
	double calculateSingleError(int dataCount, ArrayList<DataGroup> groups){
		double sumError=0;
		Layer outputLayer=layers[layerNumber-1];
		for(int i=0;i<outputLayer.nodeNumber;i++){
			double dy=groups.get(dataCount).outputs[i];
			double oy=outputLayer.nodes[i].x;
			sumError+=Math.pow(dy-oy,2.0);
		}
		return sumError;
	}
	void backward(){
		for(int layercount=layerNumber-2;layercount>0;layercount--){
			CalculateableLayer layer= (CalculateableLayer) layers[layercount];

			for(int nodecount=0;nodecount<layer.nodeNumber;nodecount++){
				//计算均差
				calculateHiddenAverageError(layercount,nodecount);
			}
		}
	}
	double calculateSingleOutputAverageError(int dataCount,int nodeNumber){
		Layer outputLayer=layers[layerNumber-1];
		double dy=dataSet.trainGroups.get(dataCount).outputs[nodeNumber];
		CalculateableNode ynode= (CalculateableNode) outputLayer.nodes[nodeNumber];
		double oy=ynode.x;
		return (dy-oy)*ynode.derivative;
	}
	void clearAverageError(){
		for(int laycount=1;laycount<layerNumber;laycount++){
			Layer layer=layers[laycount];
			for(int nodecount=0;nodecount<layer.nodeNumber;nodecount++){
				CalculateableNode node= (CalculateableNode) layer.nodes[nodecount];
				node.averageError=0;
			}
		}
	}
	void setOutputAverageError(int datacount){
		Layer outputLayer=layers[layerNumber-1];
		for(int nodeCount=0;nodeCount<outputLayer.nodeNumber;nodeCount++){
			CalculateableNode node = (CalculateableNode) outputLayer.nodes[nodeCount];
			node.averageError=(calculateSingleOutputAverageError(datacount,nodeCount)/ dataSet.trainGroups.size());
		}
	}
	double calculateHiddenAverageError(int layercount,int nodecount){
		CalculateableLayer layer= (CalculateableLayer) layers[layercount];
		CalculateableLayer nextlayer= (CalculateableLayer) layers[layercount+1];
		CalculateableNode node= (CalculateableNode) layer.nodes[nodecount];
		double sum=0;
		for(int nnodec=0;nnodec<nextlayer.nodeNumber;nnodec++){
			CalculateableNode nnode= (CalculateableNode) nextlayer.nodes[nnodec];
			sum+=(nnode.averageError*nnode.W[nodecount]);
		}
		node.averageError=sum*node.derivative/ dataSet.trainGroups.size();
		return node.averageError;
	}
	void refreshNet(){
		for(int layercount=1;layercount<layerNumber;layercount++){
			CalculateableLayer layer= (CalculateableLayer) layers[layercount];
			Layer lastlayer= layers[layercount-1];
			for(int nodecount=0;nodecount<layer.nodeNumber;nodecount++){
				CalculateableNode node= (CalculateableNode) layer.nodes[nodecount];
				for(int i=0;i<node.weightNumber;i++){
					double change=node.averageError*lastlayer.nodes[i].x;
					node.W[i]+=(speed*change+momentum*node.incrementW[i]);
					node.incrementW[i]=change;
				}
				double change=node.averageError;
				node.b+=(speed*change+momentum*node.incrementB);
				node.incrementB=change;
			}
		}
	}
	public void getpredictTest() throws Exception {
		for(int i=0;i<dataSet.testGroups.size();i++){
			dataSet.testPredicts.add((predict(dataSet.oldtestGroups.get(i).inputs)));
		}
	}
}
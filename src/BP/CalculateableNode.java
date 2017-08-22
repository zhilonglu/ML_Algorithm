package BP;

import BP.Util;
import BP.ActivateFunction;
import BP.Sigmord;
import BP.Tanh;
public class CalculateableNode extends Node{
    public int weightNumber;
    public double[] W;
    public double b=0;
    public double averageError;
    public double[] incrementW;
    public double incrementB=0.0;
    ActivateFunction activateFunction;
    public double derivative;
    public CalculateableNode(int nodeNumber, int weightNumber,ActivateFunction activateFunction) {
        this(nodeNumber,weightNumber);
        this.activateFunction=activateFunction;
    }
    public CalculateableNode(int nodeNumber, int weightNumber) {
        super(nodeNumber);
        ActivateFunction sigmord=new Sigmord();
        ActivateFunction tanh=new Tanh();
        this.activateFunction=tanh;
        this.weightNumber = weightNumber;
        averageError=Double.MAX_VALUE;
        W=new double[weightNumber];
        for(int i=0;i<weightNumber;i++){
            W[i]=BPNN.random.nextDouble();
        }
        incrementW=new double[weightNumber];
        for(int i=0;i<weightNumber;i++){
            incrementW[i]=0;
        }
    }
    public double activate(double[] X) throws Exception {
        double h= Util.dot(X,W);
        h+=b;
        this.x=activateFunction.calculate(h);
        this.derivative=activateFunction.derivativeCalculate(h);
        return this.x;
    }
}

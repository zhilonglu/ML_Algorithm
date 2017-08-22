package BP;

import BP.ActivateFunction;

public class CalculateableLayer extends Layer {
    ActivateFunction activateFunction;
    public CalculateableLayer(int nodeNumber, int layerCount,int weightNumber) {
        super(nodeNumber, layerCount);
        nodes=new CalculateableNode[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            nodes[i]=new CalculateableNode(nodeNumber,weightNumber);
        }
    }
    public CalculateableLayer(int nodeNumber, int layerCount,int weightNumber,ActivateFunction activateFunction) {
        super(nodeNumber, layerCount);
        this.activateFunction=activateFunction;
        nodes=new CalculateableNode[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            nodes[i]=new CalculateableNode(nodeNumber,weightNumber,activateFunction);
        }
    }
}

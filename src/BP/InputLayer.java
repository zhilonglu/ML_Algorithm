package BP;
public class InputLayer extends Layer{
    public InputLayer(int nodeNumber, int layerCount) {
        super(nodeNumber, layerCount);
        nodes=new InputNode[nodeNumber];
        for(int i=0;i<nodeNumber;i++){
            nodes[i]=new InputNode(layerCount);
        }
    }
}

package randomForest;

import java.util.Random;
import java.util.Vector;
public class BootStrapping {
	public static Vector<Instance> getBootStrappedData(Vector<Instance> oriData, 
			Random random) {
		Vector<Instance> sampledVec = new Vector<Instance>();
		int dataSize = oriData.size();
		for (int i = 0; i < dataSize; i++) {
			int nextIndex = random.nextInt(dataSize);
			Instance temp = oriData.get(nextIndex);
			sampledVec.add(temp);
		}		
		return sampledVec;
	}
}

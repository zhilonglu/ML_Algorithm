package rf2;

import java.util.Random;
public class Evaluation {
	private String clsName;
	private DataSet dataset;
	public Evaluation() {
	}
	public Evaluation(DataSet dataset, String clsName) {
		this.dataset = dataset;
		this.clsName = clsName;
	}
	public double crossValidation() {
		int fold = 10;
		double avg_error = 0;
		Random random = new Random(20130000);
		int[] permutation = new int[10000];
		for (int i = 0; i < permutation.length; i++) {
			permutation[i] = i;
		}
		for (int i = 0; i < 10 * permutation.length; i++) {
			int repInd = random.nextInt(permutation.length);
			int ind = i % permutation.length;
			int tmp = permutation[ind];
			permutation[ind] = permutation[repInd];
			permutation[repInd] = tmp;
		}
		int[] perm = new int[dataset.getNumInstnaces()];
		int ind = 0;
		for (int i = 0; i < permutation.length; i++) {
			if (permutation[i] < dataset.getNumInstnaces()) {
				perm[ind++] = permutation[i];
			}
		}
		int share = dataset.getNumInstnaces() / fold;
		boolean[] isCategory = dataset.getIsCategory();
		double[][] features = dataset.getFeatures();
		double[] labels = dataset.getLabels();
		boolean isClassification = false;
		double[] measures = new double[fold];
		Classifier c = null;
		try {
			c = (Classifier) Class.forName("rf2." + clsName).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int f = 0; f < fold; f++) {
			int numTest = f < fold - 1 ? share : dataset.getNumInstnaces() - (fold - 1) * share;
			double[][] trainFeatures = new double[dataset.getNumInstnaces() - numTest][dataset.getNumAttributes()];
			double[] trainLabels = new double[dataset.getNumInstnaces() - numTest];
			double[][] testFeatures = new double[numTest][dataset.getNumAttributes()];
			double[] testLabels = new double[numTest];
			int indTrain = 0, indTest = 0;
			for (int j = 0; j < dataset.getNumInstnaces(); j++) {
				if ((f < fold - 1 && (j < f * share || j >= (f + 1) * share)) || (f == fold - 1 && j < f * share)) {
					System.arraycopy(features[perm[j]], 0, trainFeatures[indTrain], 0, dataset.getNumAttributes());
					trainLabels[indTrain] = labels[perm[j]];
					indTrain++;
				} else {
					System.arraycopy(features[perm[j]], 0, testFeatures[indTest], 0, dataset.getNumAttributes());
					testLabels[indTest] = labels[perm[j]];
					indTest++;
				}
			}
			c.train(isCategory, trainFeatures, trainLabels);
			double error = 0;
			for (int j = 0; j < testLabels.length; j++) {
				double prediction = c.predict(testFeatures[j]);
				if (isClassification) {
					if (prediction != testLabels[j]) {
						error = error + 1;
					}
				} else {
					error = error + Math.abs(prediction - testLabels[j])/testLabels[j];
				}
			}
			if (isClassification) {
				measures[f] = 1 - error / testLabels.length;//accuracy = 1 - error
			} else {
				measures[f] = error / testLabels.length;
			}
//			System.out.println("第"+f+"次交叉验证MAPE为："+measures[f] );
		}
		for(int i=0;i<measures.length;i++)
			avg_error += measures[i];
		return avg_error/measures.length;
	}
}
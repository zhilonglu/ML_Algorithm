package NN;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.text.*;
import java.util.*;

/**
 *
 * @author dhruv
 */
public class BP4 {

	boolean isTrained = false;
	DecimalFormat df;
	Random rand = new Random();
	ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	Neuron bias = new Neuron();
	int[] layers;
	int randomWeightMult = 1;
	double epsilon = 0.00000000001;
	final double eta = 0.9f;
	double momentum = 0.7f;
	//    final double inputs[][] = {{0.1, 0.11}, {0.1, 0}, {0, 0}};
	//    final double expectedOutputs[][] = {{0.2}, {0.1}, {0}};
	final double inputs[][] = {{1, 1}, {1, 2}, {3, 3}};
	final double expectedOutputs[][] = {{3}, {4}, {9}};
	double resultOutputs[][] = {{-1}, {-1}, {-1}, {-1}};
	double output[];
	HashMap<String, Double> weightUpdate = new HashMap<String, Double>();
	public static void main(String args[]) {
		BP4 nn = new BP4(2, 10, 1);
		nn.run(500000, 0.001);
	}
	public BP4(int input, int hidden, int output) {
		this.layers = new int[]{input, hidden, output};
		df = new DecimalFormat("#.5#####");
		for (int i = 0; i < layers.length; i++) {
			if (i == 0) {
				for (int j = 0; j < layers[i]; j++) {
					Neuron n = new Neuron();
					inputLayer.add(n);
				}
			} else if (i == 1) {
				for (int j = 0 ; j < layers[i]; j++) {
					Neuron n = new Neuron();
					n.addInConnections(inputLayer);
					n.addBiasConnection(bias);
					hiddenLayer.add(n);
				}
			} else if (i == 2) {
				for (int j = 0; j < layers[i]; j++) {

					Neuron n = new Neuron();
					n.addInConnections(hiddenLayer);
					n.addBiasConnection(bias);
					outputLayer.add(n);
				}
			}
			else {
				System.out.println("ERROR");
			}
		}
		Neuron.neuronCount = 0;
		Connection.connCount = 0;
		for (Neuron n : hiddenLayer) {
			ArrayList<Connection> c = n.getAllInConnections();
			for (Connection k : c) {
				k.setWeight(getRandom());
			}
		}
		for (Neuron n : outputLayer) {
			ArrayList<Connection> c = n.getAllInConnections();
			for (Connection k : c) {
				k.setWeight(getRandom());
			}
		}
		if (isTrained) {
			trainedWeights();
			updateWeights();
		}
	}
	double getRandom() {
		return randomWeightMult * (rand.nextDouble() * 2 - 1);
	}
	public void initInputs(double in[]) {
		for (int i = 0; i < inputLayer.size(); i++) 
			inputLayer.get(i).setOutput(in[i]);
	}
	public double[] computeOutputs() {
		double[] out = new double[outputLayer.size()];
		for (int i = 0; i < outputLayer.size(); i++) {
			out[i] = outputLayer.get(i).getOutput();
		}
		return out;
	}

	//Do feed forward
	public void activate() {
		for (Neuron n : hiddenLayer) {
			n.computeOutput();
		}
		for (Neuron n : outputLayer) {
			n.computeOutput();
		}

	}
	//·ûºÅº¯Êý
	public double sign(double x){
		if(x<0)
			return -1;
		else if(x>0)
			return 1;
		else
			return 0;
	}
	public void doBackPropagation(double expected[]) {
		int i = 0;
		for (Neuron n : outputLayer) {
			ArrayList<Connection> conns = n.getAllInConnections();
			for (Connection c : conns) {
				double ak = n.getOutput();
				double ai = c.left.getOutput();
				double desiredOutput = expected[i];
				double derivative = ai * sign(desiredOutput - ak)/desiredOutput;
				//                double derivative = -ak * (1 - ak) * ai * (desiredOutput - ak);
				double deltaWeight = eta * -derivative;
				double newWeight = c.getWeight() + deltaWeight;
				c.setDeltaWeight(deltaWeight);
				c.setWeight(newWeight + momentum * c.getPrevDeltaWeight());
			}
			i++;
		}
		for (Neuron n : hiddenLayer) {
			ArrayList<Connection> c = n.getAllInConnections();
			for (Connection cn : c) {
				double aj = n.getOutput();
				double ai = cn.left.getOutput();
				double sumKoutputs = 0;
				int j = 0;
				for (Neuron o : outputLayer) {
					double wjk = o.getConnection(n.id).getWeight();
					double desiredOutput = (double) expected[j];
					double ak = o.getOutput();
					j++;
					sumKoutputs += (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
				}
				double derivative = aj * (1 - aj) * ai * sumKoutputs;
				double deltaWeight = -eta * derivative;
				double newWeight = cn.getWeight() + deltaWeight;
				cn.setDeltaWeight(deltaWeight);
				cn.setWeight(newWeight + momentum * cn.getPrevDeltaWeight());
			}
		}
	}
	void run(int maxSteps, double minError) {
		int i;
		// Train neural network until minError reached or maxSteps exceeded
		double error = 1;
		for (i = 0; i < maxSteps && error > minError; i++) {
			error = 0;
			for (int p = 0; p < inputs.length; p++) {
				initInputs(inputs[p]);
				activate();
				output = computeOutputs();
				resultOutputs[p] = output;
				for (int j = 0; j < expectedOutputs[p].length; j++) {
					//                    double err = Math.pow(output[j] - expectedOutputs[p][j], 2);
					double err = Math.abs(output[j] - expectedOutputs[p][j])/expectedOutputs[p][j];
					error += err;
				}
				doBackPropagation(expectedOutputs[p]);
			}
		}
		double[] in = {0,0.2};
		initInputs(in);
		activate();              
		double out[] = computeOutputs();
		System.out.println("TEST: " +out[0] );
		printResult();
		System.out.println("Sum of squared errors = " + error);
		//System.out.println("##### EPOCH " + i + "\n");
		if (i == maxSteps) {
			System.out.println("!Error training try again");
		} else {
			//   printAllWeights();
			//  printWeightUpdate();
		}
	}
	void printResult()
	{
		System.out.println("NN example with AND training");
		for (int p = 0; p < inputs.length; p++) {
			System.out.print("INPUTS: ");
			for (int x = 0; x < layers[0]; x++) {
				System.out.print(inputs[p][x] + " ");
			}
			System.out.print("EXPECTED: ");
			for (int x = 0; x < layers[2]; x++) {
				System.out.print(expectedOutputs[p][x] + " ");
			}
			System.out.print("ACTUAL: ");
			for (int x = 0; x < layers[2]; x++) {
				System.out.print(resultOutputs[p][x] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	String weightKey(int neuronId, int conId) {
		return "N" + neuronId + "_C" + conId;
	}
	public void updateWeights() {
		// update weights for the output layer
		for (Neuron n : outputLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				String key = weightKey(n.id, con.connID);
				double newWeight = weightUpdate.get(key);
				con.setWeight(newWeight);
			}
		}
		// update weights for the hidden layer
		for (Neuron n : hiddenLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				String key = weightKey(n.id, con.connID);
				double newWeight = weightUpdate.get(key);
				con.setWeight(newWeight);
			}
		}
	}
	void trainedWeights() {
	}
	public void printWeightUpdate() {
		System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
		// weights for the hidden layer
		for (Neuron n : hiddenLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				String w = df.format(con.getWeight());
				System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
						+ con.connID + "), " + w + ");");
			}
		}
		// weights for the output layer
		for (Neuron n : outputLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				String w = df.format(con.getWeight());
				System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
						+ con.connID + "), " + w + ");");
			}
		}
		System.out.println();
	}
	public void printAllWeights() {
		System.out.println("printAllWeights");
		// weights for the hidden layer
		for (Neuron n : hiddenLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				double w = con.getWeight();
				System.out.println("n=" + n.id + " c=" + con.connID + " w=" + w);
			}
		}
		// weights for the output layer
		for (Neuron n : outputLayer) {
			ArrayList<Connection> connections = n.getAllInConnections();
			for (Connection con : connections) {
				double w = con.getWeight();
				System.out.println("n=" + n.id + " c=" + con.connID + " w=" + w);
			}
		}
		System.out.println();
	}
}
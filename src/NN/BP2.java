package NN;
/**
 * BP������
 * @author dog
 * ʵ�ֶ��ǰ�������㷨
 */
public class BP2 {
	/**
	 * input[1..inputCount] : ����ֵ
	 * input[0] = -1 : �����㷧ֵ
	 */
	private double[] input;
	/**
	 * hidden[1..hiddenCount] : ���������ֵ
	 * hidden[0] = -1 : ����㷧ֵ
	 */
	private double[] hidden;
	/**
	 * output[1..outputCount] : ���ֵ
	 * output[0] = 0 : û����
	 */
	private double[] output;
	/**
	 * actOutput[1..actOutputCount] : ʵ�����ֵ
	 * actOutput[0] = 0 : û����
	 */
	private double[] actOutput;
	/**
	 * ���������������Ȩֵ����
	 * iptHidWeight[1..inputCount][j](j=1..hiddenCount) : �������j���ڵ�Ľ���Ȩֵ
	 * input[0] * iptHidWeight[0][j](j=1..hiddenCount) : �������j���ڵ��޸ĺ�ķ�ֵ
	 */
	private double[][] iptHidWeights;
	/**
	 * ���������������Ȩֵ����
	 * hidOptWeight[1..hiddenCount][j](j=1..outputCount) : ������j���ڵ�Ľ���Ȩֵ
	 * hidden[0] * hidOptWeight[0][j](j=1..outputCount) : ������j���ڵ��޸ĺ�ķ�ֵ
	 */
	private double[][] hidOptWeights;
	/**
	 * ���������������Ȩֵ�����޸���
	 */
	private double[][] iptHidWeightsDelta;
	/**
	 * ���������������Ȩֵ�����޸���
	 */
	private double[][] hidOptWeightsDelta;
	/**
	 * optError[i](i=1..outputCount) : ��i�����Ԫ������ź�
	 */
	private double[] optError;
	/**
	 * hidError[i](i=1..hiddenCount) : ��i������Ԫ�������
	 */
	private double[] hidError;
	/**
	 * ѧϰЧ��
	 */
	public double tau;
	/**
	 * ����
	 */
	private double momentum;
	/**
	 * �����
	 */
	public double errorSum;

//	/**
//	 * error[i](i=0..caseCount-1) : ��i��ѵ�������������ֵ
//	 * �ڼ����������ʱ�õ�
//	 */
//	private double[] error;

	/**
	 * ��ʼ��������
	 * @param inputCount ����Ԫ��Ŀ
	 * @param hiddenCount ������ڵ���
	 * @param outputCount ���Ԫ��Ŀ
	 * @param tau ѧϰЧ��
	 * @param momentum ����
	 */
	public BP2(int inputCount, int hiddenCount, int outputCount, double tau, double momentum) {
		this.input = new double[inputCount + 1];
		this.hidden = new double[hiddenCount + 1];
		this.output = new double[outputCount + 1];
		this.actOutput = new double[outputCount + 1];
		this.iptHidWeights = new double[inputCount + 1][hiddenCount + 1];
		this.hidOptWeights = new double[hiddenCount + 1][outputCount + 1];
		this.iptHidWeightsDelta = new double[inputCount + 1][hiddenCount + 1];
		this.hidOptWeightsDelta = new double[hiddenCount + 1][outputCount + 1];
		this.optError = new double[outputCount + 1];
		this.hidError = new double[hiddenCount + 1];
		this.tau = tau;
		this.momentum = momentum;

		input[0] = -1;
		hidden[0] = -1;
		output[0] = 0;
		actOutput[0] = 0;

		errorSum = 0;

		initWeightMatrix();
	}

	/**
	 * ��ʼ��Ȩֵ����
	 */
	private void initWeightMatrix() {
		int inputCount = input.length - 1;
		int hiddenCount = hidden.length - 1;
		int outputCount = output.length - 1;

		//�������ʼ��(����������㸽�����仯�����������򣬿�ʹѧϰ�ٶȽϿ�)
		for(int i = 0; i <= inputCount; i++) {
			for(int j = 1; j <= hiddenCount; j++) {
				iptHidWeights[i][j] = 0;
			}
		}

		//������ʼ��(�����ʼȨֵ̫С��ʹ�������������С)
		for(int i = 0; i <= hiddenCount; i++) {
			for(int j = 1; j <= outputCount; j++) {
				hidOptWeights[i][j] = i%2==0 ? -1 : 1;
			}
		}
	}

	/**
	 * ����һ������������һ��ѵ��
	 */
	public void train(double[] dataInput, double[] dataOutput) {
		System.arraycopy(dataInput, 0, input, 1, dataInput.length);
		System.arraycopy(dataOutput, 0, actOutput, 1, dataOutput.length);

		forward(input, hidden, iptHidWeights);
		forward(hidden, output, hidOptWeights);
		computeError();
		maintainWeights();
	}

	/**
	 * ����������Ĳ���
	 */
	public double[] test(double[] dataInput) {
		System.arraycopy(dataInput, 0, input, 1, dataInput.length);

		forward(input, hidden, iptHidWeights);
		forward(hidden, output, hidOptWeights);
		return output;
	}

	/**
	 * ����ò�����ֵ
	 */
	private void forward(double[] input, double[] output, double[][] weight) {
		int inputCount = input.length - 1;
		int outputCount = output.length - 1;

		for(int j = 1; j <= outputCount; j++) {
			output[j] = 0;
			for(int i = 0; i <= inputCount; i++) {
				output[j] += input[i] * weight[i][j];
			}
			output[j] = sigmoid(output[j]);
		}
	}

	/**
	 * ��������ź�
	 */
	private void computeError() {
		int hiddenCount = hidden.length - 1;
		int outputCount = output.length - 1;
		errorSum = 0;

		//�������������ź�
		for(int i = 1; i <= outputCount; i++) {
			optError[i] = (actOutput[i] - output[i]) * output[i] * (1 - output[i]);
			errorSum += Math.abs(optError[i]);
		}

		//��������������ź�
		for(int i = 1; i <= hiddenCount; i++) {
			hidError[i] = 0;
			for(int j = 1; j <= outputCount; j++) {
				hidError[i] += optError[j] * hidOptWeights[i][j] * hidden[i] * (1 - hidden[i]);
			}
		}
	}

	/**
	 * ����Ȩֵ
	 */
	private void maintainWeights() {
		int inputCount = input.length - 1;
		int hiddenCount = hidden.length - 1;
		int outputCount = output.length - 1;

		//������������������Ȩֵ�����޸���(���Ӷ�����)
		for(int i = 0; i <= hiddenCount; i++) {
			for(int j = 1; j <= outputCount; j++) {
				hidOptWeightsDelta[i][j] = (tau * optError[j] * hidden[i])
					+ (momentum * hidOptWeightsDelta[i][j]);

				hidOptWeights[i][j] += hidOptWeightsDelta[i][j];
			}
		}

		//������������������Ȩֵ�����޸���(���Ӷ�����)
		for(int i = 0; i <= inputCount; i++) {
			for(int j = 1; j <= hiddenCount; j++) {
				iptHidWeightsDelta[i][j] = (tau * hidError[j] * input[i])
					+ (momentum * iptHidWeightsDelta[i][j]);

				iptHidWeights[i][j] += iptHidWeightsDelta[i][j];
			}
		}
	}

	/**
	 * sigmoid����
	 * @param x
	 * @return
	 */
	private double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}
}
package GD;

/**
 * �ݶ��½��㷨����� f(x)=x^4-3x^3+2 ��Сֵ
 * ����Ϊ: f'(x)=4x^3-9x^2
 * @author Zealot
 * @date 2015��12��13��
 */
public class GradientDescent {
//	��������, we expect that the local minimum occurs at x=9/4

	double x_old = 0;
	static double x_new = 6; // �� x=6 ��ʼ����
	double gamma = 0.01; // ÿ�ε����Ĳ���
	double precision = 0.00001;//���
	static int iter = 0;//��������
	//Ŀ�꺯���ĵ���
	private double  derivative(double x) {
		return 4 * Math.pow(x, 3) - 9 *Math.pow(x, 2);
	}
	private void getmin() {
		while (Math.abs(x_new - x_old) > precision){
			iter++;
			x_old = x_new;
		    x_new = x_old - gamma * derivative(x_old);
		}
	}
	public static void main(String[] args) {
		GradientDescent gd = new GradientDescent();
		gd.getmin();
		System.out.println(iter+": "+x_new);
	}
}


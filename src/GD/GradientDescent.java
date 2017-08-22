package GD;

/**
 * 梯度下降算法，求解 f(x)=x^4-3x^3+2 最小值
 * 导数为: f'(x)=4x^3-9x^2
 * @author Zealot
 * @date 2015年12月13日
 */
public class GradientDescent {
//	经过计算, we expect that the local minimum occurs at x=9/4

	double x_old = 0;
	static double x_new = 6; // 从 x=6 开始迭代
	double gamma = 0.01; // 每次迭代的步长
	double precision = 0.00001;//误差
	static int iter = 0;//迭代次数
	//目标函数的导数
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


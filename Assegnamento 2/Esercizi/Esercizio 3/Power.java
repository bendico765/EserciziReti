import java.lang.Thread;
import java.lang.Math;
import java.util.concurrent.*;

public class Power implements Callable<Double>{
	double number;
	double exp; 
	
	public Power(double number, double exp){
		this.number = number;
		this.exp = exp;
	}
	
	public Double call(){
		System.out.printf("Esecuzione {%.2f}^{%.2f} in {%d}\n", number, exp, Thread.currentThread().getId());
		return Math.pow(number, exp);
	}
}

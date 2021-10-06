import java.lang.Thread;

public class Main{
	public static void main(String args[]){
		/*
			DatePrinterThread dp = new DatePrinterThread();
			dp.run();
		
		*/
		Thread t1 = new Thread(new DatePrinterRunnable());
		t1.start();
		return;
	}
}

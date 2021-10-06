import java.lang.Thread;
import java.util.Calendar;

public class DatePrinterThread extends Thread{
	public void run(){
		Calendar cal;
		while(true) {
			cal = Calendar.getInstance();
			System.out.printf("[%s] %s\n", cal.getTime().toString(), Thread.currentThread().getName());
			try{
				Thread.sleep(2000);
			}
			catch(InterruptedException e){
				System.out.println("interrotto");
				return;
			}
		}	
	}
}

import java.lang.Thread;
import java.util.Calendar;

public class DatePrinter{
	public static void main(String args[]) {
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

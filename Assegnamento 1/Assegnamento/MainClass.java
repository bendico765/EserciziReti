import java.lang.Thread;

public class MainClass{
	public static void main(String args[]){
		double accuracy;
		int time;
		// lettura parametri di ingresso
		try{
			accuracy = Double.parseDouble(args[0]);
			time = Integer.parseInt(args[1]);
		}
		catch( Exception e ){
			System.out.printf("Errore con i parametri in ingresso\n");
			return;
		}
		// lancio del thread calcolatore
		Thread t1 = new Thread(new PiCalculator(accuracy));
		t1.start();
		// attesa
		try{
			t1.join(time);
			// mando il segnale di interruzione al thread
			t1.interrupt();
		}
		catch( InterruptedException e){}
		return;
	}
}

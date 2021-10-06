import java.lang.Runnable;
import java.lang.Thread;
import java.util.Random;

public class Viaggiatore implements Runnable{
	private int id; 
	private int upperBound; // limite massimo (in ms) alla possibile durata del task del viaggiatore	
	
	public Viaggiatore(int id, int upperBound){
		this.id = id;
		this.upperBound = upperBound;
	}
	
	public void run(){
		int time;
		Random random;
		
		// generazione durata task
		random = new Random();
		time = random.nextInt(this.upperBound+1);
		
		System.out.printf("Viaggiatore {%d}: sto acquistando un biglietto\n", id);
		try{
			Thread.sleep(time);
		}
		catch( InterruptedException e){}
		System.out.printf("Viaggiatore {%d}: ho acquistato il biglietto\n", id);
	}
}

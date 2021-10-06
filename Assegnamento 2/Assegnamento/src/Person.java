import java.lang.Runnable;
import java.util.Random;

public class Person implements Runnable{
	private int id;
	private int upperBound; // limite massimo (in ms) alla possibile durata del task della persona	
	
	public Person(int id, int upperBound){
		this.id = id;
		this.upperBound = upperBound;
	}
	
	public Person(int upperBound){
		this.id = -1;
		this.upperBound = upperBound;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void run(){
		int time;
		Random random;
		
		// generazione durata task
		random = new Random();
		time = random.nextInt(this.upperBound+1);
		
		System.out.printf("Persona {%d} ha iniziato il suo task\n", this.id);
		try{
			Thread.sleep(time);
		}
		catch( InterruptedException e){}
	}
}

import java.lang.Thread;

public class PiCalculator implements Runnable{
	private double accuracy; // precisione nel calcolo
	
	public PiCalculator(double accuracy){
		this.accuracy = accuracy;
	}
	
	public void run(){
		double tmp = 3;
		double pi = 4;
		boolean plus = false;
		while(true){
			// controllo l'eventuale segnale pendente di terminazione
			if( Thread.interrupted() ){
				System.out.printf("Il thread ha ricevuto il segnale di terminazione\n");
				break;
			}
			else{
				
				if( Math.abs(pi - Math.PI ) <= accuracy ){
					System.out.printf("Pi: %s\n", pi);
					break;
				}
				if(plus){
					pi += 4/tmp;
				}
				else{ 
					pi -= 4/tmp;
				}
				plus = !plus;
				tmp += 2;
			}
		}
	}
}

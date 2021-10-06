import java.lang.Thread;
import java.util.concurrent.*;

public class MainClass{
	static final int EMITTERS = 5; // numero emettitrici
	static final int MAX_TRAVELERS = 10; // massimo numero di viaggiatori che possono trovarsi in coda 
	static final int TRAVELERS = 50; // numero viaggiatori
	static final int MAX_TASK_LEN = 1000; // massima lunghezza task in ms
	static final int TIME = 50; // tempo di attesa tra la generazione di un task e l'altro in ms
	
	public static void main(String args[]){
		BlockingQueue queue = new ArrayBlockingQueue<Viaggiatore>(MAX_TRAVELERS);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(EMITTERS, EMITTERS, 0L, TimeUnit.MILLISECONDS, queue);
		
		// generazione viaggiatori
		for(int i = 0; i < TRAVELERS; i++){
			try{
				// inizializzazione viaggiatore
				pool.execute( new Viaggiatore(i, MAX_TASK_LEN) );
				Thread.sleep(TIME);
			}
			catch( RejectedExecutionException e){
				System.out.printf("Traveler no.  {%d}: sala esaurita\n", i);
			}
			catch( InterruptedException e ){}
		}
		// terminazione thread pool 
		// (senza si blocca il main)
		pool.shutdown();
	}
}

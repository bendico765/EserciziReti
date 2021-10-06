import java.lang.Thread;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass{
	public static final int nReaders = 20;
	public static final int nWriters = 20;
	
	public static void main(String args[]){
		Counter counter = new Counter();
		ExecutorService threadPool = Executors.newCachedThreadPool();
		long startTimestamp, endTimestamp;
		
		// partenza timer
		startTimestamp = System.currentTimeMillis();
		
		// creazione writers
		for(int i = 0; i < nWriters; i++){
			Writer w = new Writer(counter);
			try{
				threadPool.execute(w);
			}
			catch(Exception e){}
		}
		// creazione readers
		for(int i = 0; i < nReaders; i++){
			Reader r = new Reader(counter);
			try{
				threadPool.execute(r);
			}
			catch(Exception e){}
		}
		// attendiamo la terminazione dei thread
		threadPool.shutdown();
		
		// stop timer
		endTimestamp = System.currentTimeMillis();
		
		System.out.printf("Tempo richiesto %d millis\n", endTimestamp-startTimestamp);
				
		return;
	}
}

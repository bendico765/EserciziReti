import java.util.concurrent.ConcurrentMap;

public class Worker	implements Runnable{
	private Account account; // account da elaborare
	private ConcurrentMap<String, ThreadsafeIntCounter> map; // struttura dati con i contatori condivisa
	
	public Worker(Account account, ConcurrentMap<String, ThreadsafeIntCounter> map){
		this.account = account;
		this.map = map;
	}
	
	public void run(){
		// per ogni movimento bancario prendo la causale ed incremento il contatore
		// associato nella struttura dati condivisa
		for(Transfer t: account){
			ThreadsafeIntCounter counter = map.get((Object)t.getPurpose());
			counter.increment();
		}
	}
}

/*
    Creare un file contenente oggetti che rappresentano i conti correnti di una banca. Ogni conto corrente contiene il nome del correntista 
    ed una lista di movimenti. I movimenti registrati per un conto corrente sono relativi agli ultimi 2 anni, quindi possono essere molto
    numerosi.
    
    Per ogni movimento vengono registrati la data e la causale del movimento. L'insieme delle causali possibili è fissato: Bonifico,
    Accredito, Bollettino, F24, PagoBancomat.
    Rileggere il file e trovare, per ogni possibile causale, quanti movimenti hanno quella causale.


    Progettare un'applicazione che attiva un insieme di thread. Uno di essi legge dal file gli oggetti “conto corrente” e li passa, uno per
    volta, ai thread presenti in un thread pool ogni thread calcola il numero di occorrenze di ogni possibile causale all'interno di quel
    conto corrente ed aggiorna un contatore globale.
    Alla fine il programma stampa per ogni possibile causale il numero totale di occorrenze.
    
   	Utilizzare:
    -NIO per creare il file
    -NIO oppure IO classico per rileggere il file
    -JSON per la serializzazione
*/
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.lang.Math;
import java.sql.Timestamp;

public class MainClass{
	public static int N_ACCOUNTS = 50; // numero di conti generati
	public static int TRANSFERS_LOWER_BOUND = 100; // limite inferiore al possibile numero di movimenti generati per ciascun conto
	public static int TRANSFERS_UPPER_BOUND = 400; // limite superiore al possibile numero di movimenti generati per ciascun conto
	public static long AMOUNT_UPPER_BOUND = 5000; // limite al massimo valore di un movimento 
	public static int N_WORKERS = 5; // numero di workers assegnati all'elaborazione delle causali nei conti correnti
	public static String OUTPUT_FILE_NAME = "output.json";
	public static long SHUTDOWN_TIMER = 10; 
	
	/*
	 *	Genera e restituisce un numero arbitrario di conti bancari con movimenti generati casualmente al loro interno
	 *  
	 *  @param numberOfAccounts - numero di conti bancari da generare
	 *  @param transfersUpperBound - limite superiore al numero di possibili movimenti presenti in un conto
	 *  @param transfersLowerBound - limite inferiore al numero di possibili movimenti presenti in un conto
	 *  @param amountUpperBound - limite superiore all'ammontare del movimento bancario
	 *  @return - l'array di conti bancari generati
	 *
	*/
	public static Account[] accountsGenerator(int numberOfAccounts, int transfersUpperBound, int transfersLowerBound, long amountUpperBound){
		Account[] accounts = new Account[numberOfAccounts];
		
		for(int i = 0; i < numberOfAccounts; i++){
			int realTransfers; // movimenti creati per il conto i-esimo
			
			// creazione account
			accounts[i] = new Account(Integer.toString(i), new LinkedList<Transfer>());
			
			// generazione dei movimenti bancari del conto
			realTransfers = (int)(((double) transfersUpperBound - transfersLowerBound)*Math.random()) + transfersLowerBound;
			for( int j = 0; j < realTransfers; j++ ){
				Timestamp datetime; // data e ora del movimento
				long amount; // ammontare del movimento
				String purpose; // causale
				String[] allowedPurposes; // causali permesse dalla classe Account
				
				// reperisco la lista di causali ammesse
				allowedPurposes = Transfer.getAllowedPurposes();
				
				// generazione dati movimento
				datetime = new Timestamp(System.currentTimeMillis());
				amount = (long)(((double)amountUpperBound)*Math.random()) + 1;
				purpose = allowedPurposes[ (int)Math.floor( ((double)allowedPurposes.length) * Math.random()) ];
				
				accounts[i].addTransfer(new Transfer(datetime, amount, purpose));
			}
		}
		
		return accounts;
	}
	
	/* Costruisce una mappa con le causali come chiavi e contatori (inizializzati a zero)
	 * come valori.
	 *
	 * @return - la mappa generata
	*/
	public static ConcurrentMap<String, ThreadsafeIntCounter> getPurposesMap(){
		ConcurrentMap<String, ThreadsafeIntCounter> map;
		
		map = new ConcurrentHashMap<String, ThreadsafeIntCounter>();
		for(String purpose: Transfer.getAllowedPurposes()){
			map.put(purpose, new ThreadsafeIntCounter());
		}
		
		return map;
	}
	
	public static void printBankStats(Map<String, ThreadsafeIntCounter> map){
		for(String purpose: map.keySet()){
			System.out.printf("%s: %d\n", purpose, map.get(purpose).getValue());
		}
	}
	
	public static void main(String args[]){
		Account[] bankAccounts; // array di conti corrente
		ExecutorService threadpool;
		ConcurrentMap<String, ThreadsafeIntCounter> bankStats; // mappa che associa a ciascuna causale il numero di movimenti
		
		// generazione dei conti correnti e dei movimenti
		bankAccounts = accountsGenerator(N_ACCOUNTS, TRANSFERS_UPPER_BOUND, TRANSFERS_LOWER_BOUND, AMOUNT_UPPER_BOUND);
		
		// uso di NIO per creare il file e scriverci gli oggetti serializzati con json
		if( JSONFileHandler.writeToFile(OUTPUT_FILE_NAME, bankAccounts) == false){
			return;
		}
		
		bankAccounts = null;
		
		// rilettura del file tramite NIO e uso di json per deserializzare
		if( (bankAccounts = (Account[])JSONFileHandler.readFromFile(OUTPUT_FILE_NAME) ) == null){
			return;
		}
		
		
		// lettura ed elaborazione delle casuali
		bankStats = getPurposesMap();
		threadpool = Executors.newFixedThreadPool(N_WORKERS);
		// passo ciascun conto corrente ad un worker
		for(Account account: bankAccounts){
			threadpool.execute(new Worker(account, bankStats));
		}
		threadpool.shutdown();
		
		// attendo la terminazione del threadpool
		try{
			while( ! threadpool.awaitTermination(SHUTDOWN_TIMER, TimeUnit.SECONDS) ){}
		}
		catch(InterruptedException e){
			System.out.println("Interruzione ricevuta");
			return;
		}
		
		printBankStats(bankStats);
	}
}

import java.lang.Thread;
import java.util.LinkedList;
import java.util.concurrent.*;

public class Office{
	private int numberOfOffices; // numero di uffici
	private int secondRoomSize; // capienza seconda sala
	private LinkedList<Person> firstRoom;
	private ThreadPoolExecutor pool; // thread pool con gli uffici
	private int counter; // numeratrice
	
	public static final int FULL_ROOM_WAIT_TIME = 200; // nel caso in cui la seconda stanza sia piena, tempo da attendere prima di
													   // riprovare l'inserimento di una persona al suo interno (in ms)
	
	public Office(int numberOfOffices, int secondRoomSize) throws IllegalArgumentException{
		if( numberOfOffices <= 0 || secondRoomSize <= 0 ){	
			throw new IllegalArgumentException();
		}
		
		this.numberOfOffices = numberOfOffices;
		this.secondRoomSize = secondRoomSize;
		this.counter = 0;
		
		// inizializzo la prima sala di attesa
		firstRoom = new LinkedList<Person>();
		
		// inizializzo gli uffici (thread del pool) e la seconda
		// sala di attesa (coda del pool)
		pool = new ThreadPoolExecutor(numberOfOffices, numberOfOffices, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(secondRoomSize));
		// faccio già partire i thread del pool
		pool.prestartAllCoreThreads();
	}
	
	public void insert(Person p){
		// assegno l'id alla persona
		p.setId(counter);
		counter += 1;
		// l'aggiungo nella prima sala
		firstRoom.add(p);
	}
	
	public void start(){
		for(Person p: this.firstRoom){ // una alla volta, provo ad inserire le persone
			boolean rejectedFlag;
			
			do{
				try{ // tento l'inserimento
					this.pool.execute(p);
					rejectedFlag = false;
				}
				catch( RejectedExecutionException e ){
					// la seconda stanza è piena, attendo un pò prima di provare
					// a reinserire la persona
					rejectedFlag = true;
					System.out.printf("Persona {%d} rifiutata\n", p.getId());
					try{
						Thread.sleep(FULL_ROOM_WAIT_TIME);
					}
					catch( InterruptedException e1 ){}
				} 
			}while( rejectedFlag );
			
		}
	}
	
	public void close(){
		pool.shutdown();
	}
}

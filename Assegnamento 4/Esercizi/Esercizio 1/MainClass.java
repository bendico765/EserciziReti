public class MainClass{
	public static void main(String args[]){
		//Dropbox dropbox;
		SyncDropbox dropbox;
		Consumer even_consumer, odd_consumer;
		Producer producer;
		
		// inizializzazione oggetti
		//dropbox = new Dropbox();
		dropbox = new SyncDropbox();
		even_consumer = new Consumer(dropbox, true);
		odd_consumer = new Consumer(dropbox, false);
		producer = new Producer(dropbox);
		
		// creazione thread
		Thread t1 = new Thread(even_consumer);
		Thread t2 = new Thread(odd_consumer);
		Thread t3 = new Thread(producer);
		
		t1.run();
		t2.run();
		t3.run();
		
		return;
	}
}

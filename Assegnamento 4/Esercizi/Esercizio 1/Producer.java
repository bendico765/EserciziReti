public class Producer implements Runnable{
	private Dropbox dropbox;
	
	public Producer(Dropbox dropbox){
		this.dropbox = dropbox;
	}
	
	public void run(){
		int value = (int) (Math.random() * 100);
		this.dropbox.put(value);
	}
}

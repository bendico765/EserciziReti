public class Consumer implements Runnable{
	private Dropbox dropbox;
	private boolean value;
	
	public Consumer(Dropbox dropbox, boolean value){
		this.value = value;
		this.dropbox = dropbox;
	}

	public void run(){
		dropbox.take(this.value);
	}
}

public class Writer implements Runnable{
	Counter c;
	
	public Writer(Counter c){
		this.c = c;
	}
	
	public void run(){
		c.increment();
	}
}

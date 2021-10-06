public class Reader implements Runnable{
	Counter c;
	
	public Reader(Counter c){
		this.c = c;
	}
	
	public void run(){
		System.out.printf("Valore letto: {%d}\n", c.get());
	}
}

public class ThreadsafeIntCounter{
	private int value;
	
	public ThreadsafeIntCounter(){
		this.value = 0;
	}
	
	public synchronized void increment(){
		this.value += 1;
	}
	
	public synchronized int getValue(){
		return this.value;
	}
}

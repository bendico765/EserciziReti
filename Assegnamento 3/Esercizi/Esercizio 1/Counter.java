import java.util.concurrent.locks.ReentrantLock;

public class Counter{
	private int value = 0;
	private ReentrantLock lock = new ReentrantLock();
	
	public Counter(){}
	
	public void increment(){
		try{
			lock.lock();
			value += 1;
		}
		finally{
			lock.unlock();
		}
	}
	
	public int get(){
		int tmp;
		try{
			lock.lock();
			tmp = this.value;
		}
		finally{
			lock.unlock();
		}
		return tmp;
	}
}

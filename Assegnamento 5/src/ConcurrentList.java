import java.util.LinkedList;

// linked list multithread-safe

public class ConcurrentList<T>{
	LinkedList<T> list;
	
	public ConcurrentList(){
		this.list = new LinkedList<T>();
	}
	
	public synchronized void add(T element){
		list.add(element);
		notify();
	}
	
	public synchronized T remove(){
		while( list.size() == 0 )
			try{
				wait();
			}
			catch(InterruptedException e){ return null; }
		return list.remove();
	}
}

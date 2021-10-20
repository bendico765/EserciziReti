public class SyncDropbox extends Dropbox{
	public SyncDropbox(){
		super();
	}
	
	public synchronized int take(boolean e){
		return super.take(e);
	}
	
	public synchronized void put(int n){
		super.put(n);
	}
}

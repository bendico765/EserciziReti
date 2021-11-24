import java.util.List;
import java.util.Iterator;

public class Account implements Iterable<Transfer>{
	private String name;
	private List<Transfer> transfers; // movimenti bancari del cliente
	
	public Account(){
		this.name = "";
		this.transfers = null;
	}
	
	public Account(String name, List<Transfer> transfers){	
		this.name = name;
		this.transfers = transfers;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public List<Transfer> getTransfers(){
		return this.transfers;
	}
	
	public void setTransfers(List<Transfer> transfers){
		this.transfers = transfers;
	}
	
	public void addTransfer(Transfer newTransfer){
		transfers.add(newTransfer);
	}
	
	public Iterator<Transfer> iterator(){
		return this.transfers.iterator();
	}
	
	public String toString(){
		String transfersString;
		Object[] tmpTransfer;
		
		if( this.transfers.size() > 0 ){
			transfersString = "[";
			tmpTransfer = this.transfers.toArray();
			for(int i = 0; i < tmpTransfer.length -1; i++){
				transfersString = transfersString.concat(tmpTransfer[i].toString() + ", ");
			}
			transfersString = transfersString.concat(tmpTransfer[tmpTransfer.length -1].toString() + "]");
		}
		else{
			transfersString = "[]";
		}
		
		return "{\n" + name + ",\n" + transfersString + "\n}";
	}

}

import java.sql.Timestamp;

public class Transfer{
	private Timestamp datetime; // data e ora del movimento
	private long amount; // importo del movimento
	private String purpose; // causale del movimento
	
	private static String[] allowedPurposes = {"Bonifico", "Accredito", "Bollettino", "F24", "PagoBancomat"};
	
	public Transfer(){
		this.datetime = null;
		this.amount = 0;
		this.purpose = null;
	}
	
	public Transfer(Timestamp datetime, long amount, String purpose) throws IllegalArgumentException{
		boolean flag = true;
		
		if( amount <= 0 ) throw new IllegalArgumentException("Amount must be positive");
		// controllo la validità della causale
		for(String s: allowedPurposes){
			if( purpose.equals(s) ){
				flag = false;
				break;
			}
		}
		if( flag ) throw new IllegalArgumentException("Illegal purpose of the transfer");
		
		this.datetime = datetime;
		this.amount = amount;
		this.purpose = purpose;
	}
	
	public Timestamp getDatetime(){
		return this.datetime;
	}
	
	public void setDatetime(Timestamp datetime){
		this.datetime = datetime;
	}
	
	public long getAmount(){
		return this.amount;
	}
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	
	public String getPurpose(){
		return this.purpose;
	}
	
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	
	static public String[] getAllowedPurposes(){
		return allowedPurposes;
	}
	
	public String toString(){
		return this.datetime.toString() + " " + Long.toString(amount) + " " + purpose;
	}
}

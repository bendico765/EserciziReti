public class Day{
	private String[][] session; // tabella di sessioni e speakers della giornata
	private int numberSessions; // numero di sessioni (righe della matrice) in una giornata
	private int numberSpeakers; // numero di interventisti (colonne della matrice) per giornata
	
	public Day(int numberSessions, int numberSpeakers){
		if(numberSessions <= 0) throw new IllegalArgumentException("Numero di sessioni non valido");
		if(numberSpeakers <= 0) throw new IllegalArgumentException("Numero di speakers non valido");
		
		this.numberSessions = numberSessions;
		this.numberSpeakers = numberSpeakers;
		this.session = new String[numberSessions][numberSpeakers];
		
		for(int i = 0; i < numberSessions; i++)
			for(int j = 0; j < numberSpeakers; j++)
				session[i][j] = "-";
	}
	
	public int getNumberSessions(){
		return this.numberSessions;
	}
	
	public int getNumberSpeakers(){
		return this.numberSpeakers;
	}
	
	public String[][] getDayInfo(){
		return this.session;
	}
	
	/*
	 * Inserisce uno speaker nello slot specificato
	 *
	 * @param sessionNumber - indice della sessione desiderata
	 * @param speakerPosition - indice della posizione nella sessione in cui collocare lo speaker
	 * @param speakerName - nome dello speaker
	 * @return - un messaggio contenente l'esito dell'operazione
	 */
	public String addSpeaker(int sessionNumber, int speakerPosition, String speakerName){
		if( sessionNumber < 0 || sessionNumber >= this.numberSessions ) return "Numero di sessione non valido";
		if( speakerPosition < 0 || speakerPosition >= this.numberSpeakers ) return "Posizione dell'intervento non valida";
		
		if( session[sessionNumber][speakerPosition] != "-" ){ // slot già occupato
			return "Slot già occupato";
		}
		else{
			session[sessionNumber][speakerPosition] = speakerName;
			return "Prenotazione avvenuta con successo";
		}
	}
	
}

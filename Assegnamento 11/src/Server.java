/*
	Si progetti un’applicazione Client/Server per la gestione delle registrazioni
	ad un congresso. 
	
	L’organizzazione del congresso fornisce agli speaker delle
	varie sessioni un’interfaccia tramite la quale iscriversi ad una sessione, e la
	possibilità di visionare i programmi delle varie giornate del congresso, con
	gli interventi delle varie sessioni.
	
	Il server mantiene i programmi delle 3 giornate del congresso, ciascuno dei
	quali è memorizzato in una struttura dati come quella mostrata di seguito, in
	cui ad ogni riga corrisponde una sessione (in tutto 12 per ogni giornata). Per
	ciascuna sessione vengono memorizzati i nomi degli speaker che si sono
	registrati (al massimo 5).
	
	 -----------------------------------------------------------------
	|Sessione | Intervento 1   | Intervento 2   | ... | Intervento 5 |
	-----------------------------------------------------------------
    |S1      | Nome Speaker 1 | Nome Speaker 2
    ...
    |S5      | ... 
*/
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class Server implements ServerInterface{
	private Day[] congress; // giorni del congresso
	private int numberSessions; // numero di sessioni in una giornata
	private int numberSpeakers; // numero di speakers per sessione
	
	public Server(int numberDays, int numberSessions, int numberSpeakers) throws RemoteException{
		congress = new Day[numberDays]; 
		this.numberSessions = numberSessions;
		this.numberSpeakers = numberSpeakers;
		
		for(int i = 0; i < numberDays; i++){
			congress[i] = new Day(numberSessions, numberSpeakers);
		}
	}
	
	public int getNumberSessions() throws RemoteException{
		return this.numberSessions;
	}
	
	public int getNumberSpeakers() throws RemoteException{
		return this.numberSpeakers;
	}
	
	/*
	 * Inserisce uno speaker nello slot specificato.
	 *
	 * Per offrire un programma simile a quello della specifica dell'assignment, gli 
	 * indici della giornata, della sessione e della posizione nella sessione partono 
	 * da 1 anzichè da 0.
	 *
	 * @param day - indice della giornata nel congresso
	 * @param sessionNumber - indice della sessione desiderata
	 * @param speakerPosition - indice della posizione nella sessione in cui collocare lo speaker
	 * @param speakerName - nome dello speaker
	 * @return - un messaggio contenente l'esito dell'operazione
	 */
	public String addSpeaker(int day, int sessionNumber, int speakerPosition, String speakerName) throws RemoteException{
		if( day <= 0 || day > congress.length ) 
			return "Errore nella scelta della giornata: scegliere un valore compreso tra 1 e " + Integer.toString(this.congress.length);
		if( sessionNumber <= 0 || sessionNumber > numberSessions ) 
			return "Numero di sessione non valido: scegliere un valore compreso tra 1 e " + Integer.toString(this.numberSessions);
		if( speakerPosition <= 0 || speakerPosition > numberSpeakers ) 
			return "Posizione dello speaker non valida: scegliere un valore compreso tra 1 e " + Integer.toString(this.numberSpeakers);
		
		return congress[day-1].addSpeaker(sessionNumber-1, speakerPosition-1, speakerName);
	}
	
	/*
	 * Restituisce una rappresentazione testuale del programma del congresso
	 *
	 * @return - una stringa contenente il programma del congresso
	 */
	public String getCongressTable() throws RemoteException{
		String result = "";
		String[][] session;
		
		for(int i = 0; i < this.congress.length; i++){ // ciclo dei giorni del congresso
			result = result.concat("DAY " + Integer.toString(i + 1) + "\n");
			
			session = congress[i].getDayInfo();
			for(int j = 0; j < congress[i].getNumberSessions(); j++){ // ciclo delle sessioni per giornata
				result = result.concat("\t" + "SESSION " + Integer.toString(j+1) + ":\n");
				
				for(int k = 0; k < congress[i].getNumberSpeakers(); k++){
					result = result.concat("\t\t" + session[j][k] + "\n");
				}
			}
			
		}
		result = result.concat("\n");
		return result;
	}
}

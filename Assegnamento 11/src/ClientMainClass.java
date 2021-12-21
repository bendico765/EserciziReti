/*
	Il client può richiedere operazioni per
	- registrare uno speaker ad una sessione;
	- ottenere il programma del congresso;
	
	Il client inoltra le richieste al server tramite il meccanismo di RMI.
	
	Prevedere, per ogni possibile operazione una gestione di eventuali condizioni
	anomale (ad esempio la richiesta di registrazione ad una giornata e/o
	sessione inesistente oppure per la quale sono già stati coperti tutti gli spazi
	d’intervento)
	
	Il client è implementato come un processo ciclico che continua a fare
	richieste sincrone fino ad esaurire tutte le esigenze utente. Stabilire una
	opportuna condizione di terminazione del processo di richiesta.
*/
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMainClass{
	public static final String TERMINATOR_STRING = "DISCONNECT"; // comando terminatore del programma
	
	/* Si connette al Registry in esecuzione sull'host locale e restituisce l'oggetto 
	 * remoto richiesto.
	 *
	 * @param name - nome simbolico dell'oggetto remoto
	 * @param port - porta su cui il registro accetta le richieste
	 * @return - l'oggetto remoto richiesto, null in caso di errore
	 */
	public static ServerInterface connect(String name, int port) throws RemoteException, NotBoundException{
		Remote remoteObject;
		Registry reg;
		
		reg = LocateRegistry.getRegistry(port);
		remoteObject = reg.lookup(name);
		return (ServerInterface) remoteObject;
	}
	
	/* La funzione effettua il parsing del comando dato ed esegue la funzione
	 * associata sull'oggetto remoto.
	 *
	 * @param remoteObject - oggetto remoto da utilizzare
	 * @param line - stringa contenente il comando da parsare (con relativi parametri)
	 */
	public static void parseAndExecute(ServerInterface remoteObject, String line) throws RemoteException{
		String[] tokens; 
		String command;
		
		// dividiamo le parole in base allo spazio
		tokens = line.split(" ");
		command = tokens[0]; // comando effettivo
		
		switch(command){
			case "GET-PROGRAM": // GET-PROGRAM 
				System.out.println(remoteObject.getCongressTable());
				break;
			case "REGISTER": // REGISTER giornoDelCongresso numeroDiSessione posizioneSpeakerNellaSessione
				int day;
				int sessionNumber;
				int speakerPosition;
				String speakerName;
				try{
					day = Integer.parseInt(tokens[1]);
					sessionNumber = Integer.parseInt(tokens[2]);
					speakerPosition = Integer.parseInt(tokens[3]);
					speakerName = tokens[4];
				}
				catch(ArrayIndexOutOfBoundsException e){
					System.out.println("Numero di argomenti insufficienti");
					break;
				}
				catch(NumberFormatException e){
					System.out.println("Errore con i parametri");
					System.out.println(e.getMessage());
					break;
				}
				System.out.println(remoteObject.addSpeaker(day, sessionNumber, speakerPosition, speakerName));
				break;
			default:
				System.out.println("Comando non riconosciuto");
				break;
		}
	}
	
	public static void main(String args[]){
		ServerInterface remoteObject; // interfaccia all'oggetto remoto
		BufferedReader reader; // buffer di appoggio per leggere da terminale
		String line, serviceName;
		int port;
		
		// parsing argomenti
		try{
			serviceName = args[0];
			port = Integer.parseInt(args[1]);
			if( port <= 0 ){
				throw new NumberFormatException();
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Numero di argomenti insufficienti");
			System.out.println("java ClientMainClass nomeServizio porta");
			return;
		}
		catch(NumberFormatException e){
			System.out.println("Numero di porta non valido");
			System.out.println("java ClientMainClass nomeServizio porta");
			return;
		}
		
		reader = new BufferedReader( new InputStreamReader(System.in));
		
		try{
			// tentativo di connessione all'oggetto remoto
			remoteObject = connect(serviceName, port);
			
			System.out.println("Connessione al server avvenuta con successo");
			System.out.printf(">");
			while((line = reader.readLine()).equals(TERMINATOR_STRING) == false){
				parseAndExecute(remoteObject, line);
				System.out.printf(">");
			}
		}
		catch(ConnectException e){
			System.out.println("Connessione rifiutata");
		}
		catch(RemoteException e){
			System.out.println("Errore durante l'esecuzione del comando");	
		}
		catch(IOException e){
			System.out.println("Errore di I/O");
		}
		catch(NotBoundException e){
			System.out.println("Non esiste nessun servizio con tale nome associato");
		}
	}
}

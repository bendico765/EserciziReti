import java.util.Scanner;

/*
	Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. 
	Nell'ufficio esiste un'ampia sala d'attesa in cui ogni persona può entrare 
	liberamente. Quando entra, ogni persona prende il numero dalla numeratrice 
	e aspetta il proprio turno in questa sala. una seconda sala, meno ampia, 
	posta davanti agli sportelli, in cui possono essere presenti al massimo k 
	persone (oltre alle persone servite agli sportelli)
    Una persona si mette quindi prima in coda nella prima sala, poi passa 
    nella seconda sala.
    Ogni persona impiega un tempo differente per la propria operazione allo 
    sportello. Una volta terminata l'operazione, la persona esce dall'ufficio

	Scrivere un programma in cui:

    -l'ufficio viene modellato come una classe JAVA, in cui viene attivato un 
    ThreadPool di dimensione uguale al numero degli sportelli 
    -la coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma
    -la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal ThreadPool
    -ogni persona viene modellata come un task, un task che deve essere assegnato 
    ad uno dei thread associati agli sportelli
    
    si preveda di far entrare tutte le persone nell'ufficio postale, all'inizio del programma
*/

public class MainClass{
	public static final int OFFICES = 4; // numero uffici
	public static final int TIME_UPPER_BOUND = 1000; // limite massimo (in ms) alla possibile durata del task della persona

	public static void main(String args[]){
		Scanner scanner; // scanner per operazioni di input
		int numberOfPeople; // numero di persone da servire	
		int maxNumberPeopleSecondRoom; // massimo numero persone nella seconda stanza
		Office office; // ufficio postale
		
		scanner = new Scanner(System.in);
		try{
			numberOfPeople = scanner.nextInt();
			maxNumberPeopleSecondRoom = scanner.nextInt();
			office = new Office(OFFICES, maxNumberPeopleSecondRoom);
		}
		catch(Exception e){
			System.out.println("Errore con i parametri in ingresso");
			return;
		}
	
		// inserimento di tutte persone nella prima sala
		for(int i = 0; i < numberOfPeople; i++){
			office.insert( new Person(TIME_UPPER_BOUND) );
		}
		// inizio del "funzionamento" dell'ufficio
		office.start();
		
		// chiusura ufficio
		office.close();
	}
}

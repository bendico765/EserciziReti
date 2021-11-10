/*
Si scriva un programma JAVA che 

    -riceve in input un filepath che individua una directory D
    -stampa le informazioni del contenuto di quella directory e, ricorsivamente, di tutti i file contenuti nelle sottodirectory di D 

iI programma deve essere strutturato come segue:

     -attiva un thread produttore ed un insieme di k thread consumatori 
     -il produttore comunica con i consumatori mediante una coda 
	 -il produttore visita ricorsivamente la directory data ed, eventualmente tutte le sottodirectory e mette nella coda il nome di ogni directory individuata 
     -i consumatori prelevano dalla coda i nomi delle directories e stampano il loro contenuto  (nomi dei file)
     -la coda deve essere realizzata con una LinkedList. Ricordiamo che una Linked List non è una struttura thread-safe. 
     
Dalle API JAVA      
“Note that the implementation is not synchronized. If multiple threads access a linked list concurrently, and at least one of the threads modifies the list structurally, it must be synchronized externally”

*/

import java.io.*;
import java.util.Scanner;
import java.lang.Thread;

public class MainClass{
	public static final int NUMBER_OF_CONSUMERS = 5; // numero di thread consumatori
	
	public static void main(String args[]){
		Scanner scanner; // scanner per leggere da terminale
		String path; // percorso della directory D 
		ConcurrentList<String> namesList; // coda dei nomi per gli scambi produttore/consumatori
		Producer producer; // produttore
		Consumer consumers[]; // insieme di consumatori
		Thread producerThread;
		Thread consumerThread[]; 
			
		scanner = new Scanner(System.in);
		path = scanner.nextLine(); // inserimento nome file
		namesList = new ConcurrentList<String>(); // inizializzazione coda multi-thread
		producer = new Producer(path, namesList);
		consumers = new Consumer[NUMBER_OF_CONSUMERS];
		for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
			consumers[i] = new Consumer(namesList);
		}
		
		// partenza producer
		producerThread = new Thread(producer);
		producerThread.start();
		
		// partenza consumers
		consumerThread = new Thread[NUMBER_OF_CONSUMERS];
		for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
			consumerThread[i] = new Thread(consumers[i]);
			consumerThread[i].start();
		}

		// attendo almeno la terminazione del produttore
		try{
			producerThread.join();
		}
		catch(InterruptedException e){}
		
		// mando il segnale di terminazione 
		for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
			namesList.add(null);
		}
		
		// attendo la terminazione dei consumatori
		for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
			try{
				consumerThread[i].join();
			}
			catch(InterruptedException e){}
		}
				
		return;
	}
}


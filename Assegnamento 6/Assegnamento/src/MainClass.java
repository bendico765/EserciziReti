/*
Scrivere un programma JAVA che implementa un server HTTP che gestisce richieste di trasferimento di file di diverso tipo (es. immagini jpeg, gif) provenienti da un browser web.
Il server

    -sta in ascolto su una porta nota al client (es. 6789)
    -gestisce richieste HTTP di tipo GET alla Request URL localhost:port/filename

Ulteriori indicazioni

    -le connessioni possono essere non persistenti.
    -usare le classi Socket e ServerSocket per sviluppare il programma server
    -per inviare al server le richieste, utilizzare un qualsiasi browser
    
SINTASSI:

MainClass [port_number]
*/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.concurrent.*;

public class MainClass{
	public static final String MEDIA_FOLDER = "/home/gianluca/Scrivania/Assegnamento 6/Assegnamento/media"; // cartella con i file 
	public static final int NUMBER_WORKERS = 10;
	
	public static void main(String args[]){
		ServerSocket passiveSocket;
		Socket clientSocket;
		ExecutorService pool;
		int port; // numero di porta del server
		
		try{
			port = Integer.parseInt(args[0]);
			if( port < 0 ){
				System.out.println("Il numero di porta deve essere positivo");
				return;
			}
		
		}
		catch(NumberFormatException e){ // stringa non convertibile
			System.out.println("Numero di porta non valido");
			return;
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Specificare un numero di porta");
			System.out.println("MainClass [port_number]");
			return;
		}
		
		// creazione del thread pool
		pool = Executors.newFixedThreadPool(NUMBER_WORKERS);
		
		// apertura della passive socket del server
		try{
			passiveSocket = new ServerSocket(port);
		}
		catch(IOException e){
			System.out.println("Errore nell'apertura del socket");
			System.out.println(e);
			return;
		}
		
		System.out.printf("In ascolto sulla porta %d\n", port);
		
		// attesa di client
		while(true){
			// accettazione del client
			try{
				clientSocket = passiveSocket.accept();
			}
			catch(IOException e){
				System.out.println("Errore nell'accettazione del client");
				continue;
			}
			// assegnazione del client ad un worker
			pool.execute( new Worker(clientSocket, MEDIA_FOLDER) );
		}
	}
}

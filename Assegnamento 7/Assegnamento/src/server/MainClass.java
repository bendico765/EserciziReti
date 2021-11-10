/*
È essenzialmente un echo server: rimanda al mittente qualsiasi dato riceve.

Accetta un argomento da linea di comando: la porta, che è quella su cui è
attivo il server. Se uno qualunque degli argomenti è scorretto, stampa un
messaggio di errore del tipo ERR -arg x, dove x è il numero dell'argomento.

Dopo aver ricevuto un PING, il server determina se ignorare il pacchetto
(simulandone la perdita) o effettuarne l'eco. La probabilità di perdita di
pacchetti di default è del 25%.

Se decide di effettuare l'eco del PING, il server attende un intervallo di tempo
casuale per simulare la latenza di rete

Stampa l'indirizzo IP e la porta del client, il messaggio di PING e l'azione
intrapresa dal server in seguito alla sua ricezione (PING non inviato,oppure
PING ritardato di x ms).

java PingServer 10002
128.82.4.244:44229> PING 0 1360792326564 ACTION: delayed 297 ms
128.82.4.244:44229> PING 1 1360792326863 ACTION: delayed 182 ms
128.82.4.244:44229> PING 2 1360792327046 ACTION: delayed 262 ms
128.82.4.244:44229> PING 3 1360792327309 ACTION: delayed 21 ms
128.82.4.244:44229> PING 4 1360792327331 ACTION: delayed 173 ms
128.82.4.244:44229> PING 5 1360792327505 ACTION: delayed 44 ms
128.82.4.244:44229> PING 6 1360792327550 ACTION: delayed 19 ms
128.82.4.244:44229> PING 7 1360792327570 ACTION: not sent
128.82.4.244:44229> PING 8 1360792328571 ACTION: not sent
128.82.4.244:44229> PING 9 1360792329573 ACTION: delayed 262 ms
*/

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MainClass{
	public static final double LOSS_RATE = 0.25; // rateo di perdita pacchetti
	public static final int DELAY_UPPER_BOUND = 500; // limite superiore (in ms) al ritardo simulato
	public static final int N_WORKERS = 10; // numero di thread workers del thread pool
	
	public static void main(String args[]){
		ExecutorService pool; // pool thread del server
		int port; // numero porta da usare
		
		// parsing argomenti linea di comando
		try{
			port = Integer.parseInt(args[0]);
			if( port < 0 ){
				System.out.println("Numero di porta non valido");
				System.out.println("ERR -arg 0");
				return;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Numero di parametri insufficiente");
			return;
		}
		catch(NumberFormatException e){
			System.out.println("Numero di porta non valido");
			System.out.println("ERR -arg 0");
			return;
		}
		
		// inizializzazione thread pool
		pool = Executors.newFixedThreadPool(N_WORKERS);
		
		// creazione socket
		try(DatagramSocket socket = new DatagramSocket(port);){
			DatagramPacket receivedPacket; // pacchetto ricevuto dal client
			DatagramPacket toSendPacket; // pacchetto da mandare al client
			InetAddress clientAddress; // indirizzo IP client
			int clientPort; // porta usata dal client
			byte[] buffer; // buffer di supporto
			
			while(true){
				buffer = new byte[1024]; 
				// ricezione datagramma da un client
				receivedPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(receivedPacket);
				
				pool.execute(new Worker(socket, receivedPacket, LOSS_RATE, DELAY_UPPER_BOUND));
			}
		}
		catch(SocketException e){
			System.out.println("Errore nella socket");
			System.out.println(e);
			return;
		}
		catch(IOException e){
			System.out.println("Errore nella comunicazione con un client");
			System.out.println(e);
			return;
		}
	}
}

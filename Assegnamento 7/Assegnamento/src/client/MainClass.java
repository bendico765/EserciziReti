/*
Accetta due argomenti da linea di comando: nome e porta del server. 
Se uno o più argomenti risultano scorretti, il client termina, dopo aver stampato
un messaggio di errore del tipo ERR -arg x, dove x è il numero
dell'argomento.

Utilizza una comunicazione UDP per comunicare con il server ed invia
messaggi al server, con il seguente formato: 
PING seq.no timestamp
in cui seq.no è il numero di sequenza del PING (tra 0-9) ed il timestamp (in
millisecondi) indica quando il messaggio è stato inviato

Il client non invia un nuovo PING fino che non ha ricevuto l'eco del PING precedente,
oppure è scaduto un timeout.

Stampa ogni messaggio spedito al server ed il RTT del ping oppure un * se la
risposta non è stata ricevuta entro 2 secondi.

Dopo che ha ricevuto la decima risposta (o dopo il suo timeout), il client
stampa un riassunto simile a quello stampato dal PING UNIX:

---- PING Statistics ----
10 packets transmitted, 7 packets received, 30% packet loss
round-trip (ms) min/avg/max = 63/190.29/290

il RTT medio è stampato con 2 cifre dopo la virgola

java PingClient localhost 10002
PING 0 1360792326564 RTT: 299 ms
PING 1 1360792326863 RTT: 183 ms
PING 2 1360792327046 RTT: 263 ms
PING 3 1360792327309 RTT: 22 ms
PING 4 1360792327331 RTT: 174 ms
PING 5 1360792327505 RTT: 45 ms
PING 6 1360792327550 RTT: 20 ms
PING 7 1360792327570 RTT: *
PING 8 1360792328571 RTT: *
PING 9 1360792329573 RTT: 263 ms
---- PING Statistics ----
10 packets transmitted, 8 packets received, 20% packet loss
round-trip (ms) min/avg/max = 20/158.62/299
*/

import java.net.*;
import java.io.*;
import java.lang.Math;
import java.lang.System;

public class MainClass{
	public static final int TIMEOUT = 3000; // timeout della receive, in millisecondi
	
	/**
	 * Manda un pacchetto UDP al server specificato e restituisce l'RTT
	 *
	 * @param socket - socket da usare
	 * @param seqno - numero di sequenza da mandare
	 * @param address - indirizzo del server
	 * @param port - porta su cui il server è in ascolto
	 * @return - il RTT misurato, -1 in caso di pacchetto perso
	 *
	 * Nota: La funzione ha un problema: potrebbe capitare che un pacchetto i venga considerato perso (per la scadenza del timeout)
	 *       ma magari arrivi semplicemente oltre il timeout, quando il client è già in attesa del pacchetto successivo (i+1-esimo).
	 * 		 Cosa fare in questa situazione? Si potrebbe ignorare il pacchetto i-esimo e riavviare il timeout da capo, ma questo 		  
	 *       sfalserebbe la misurazione in quanto il pacchetto i+1-esimo sarebbe "avvantaggiato" dal fatto di aver avuto
	 *       un timeout più lungo. Non venendomi in mente altre soluzioni, ho pensato che la cosa migliore fosse impostare un timeout
	 *       "ragionevolmente" lungo a sufficienza per poter dire con buona fiducia che il pacchetto è effettivamente andato perso. 
	 *       
	*/
	static int ping(DatagramSocket socket, int seqno, InetAddress address, int port) throws IOException{
		int rtt;
		long startMillis; // timestamp (in ms) all'invio del pacchetto
		long endMillis; // timestamp (in ms) all'eventuale ricezione della risposta
		byte[] buffer; // buffer di supporto
		String msg; // messaggio da mandare al server
		DatagramPacket toSendPacket;
		DatagramPacket receivedPacket;
		
		startMillis = System.currentTimeMillis();
		
		// preparazione contenuto
		msg = "PING " + Integer.toString(seqno) +  " " + Long.toString(startMillis);
		buffer = msg.getBytes();
		
		// ping al server
		toSendPacket = new DatagramPacket(buffer, buffer.length, address, port);
		socket.send(toSendPacket);
		
		// attesa della risposta al ping
		receivedPacket = new DatagramPacket(buffer, buffer.length);
		try{
			socket.receive(receivedPacket);
			endMillis = System.currentTimeMillis();
			rtt = (int)(endMillis - startMillis);
		}
		catch(SocketTimeoutException e){ rtt = -1; } // timeout scaduto
		
		return rtt;
	}
	
	public static void main(String args[]){
		String serverName;
		int serverPort;	
		
		int minRTT, sumRTT, maxRTT, receivedPackets, packetsToSend;
		double avgRTT, packetLoss;
		
		// elaborazione parametri in ingresso
		try{
			serverName = args[0];
			serverPort = Integer.parseInt(args[1]);
			if(serverPort < 0){
				System.out.println("Numero di porta non valido");
				return;
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Numero di parametri insufficiente");
			return;
		}
		catch(NumberFormatException e){
			System.out.println("Numero di porta non valido");
			return;
		}
		
		// connessione al server
		try( DatagramSocket socket = new DatagramSocket();)
		{
			InetAddress serverAddress = InetAddress.getByName(serverName);
			minRTT = TIMEOUT;
			sumRTT = 0;
			maxRTT = 0;
			receivedPackets = 0;
			packetsToSend = 10;
			
			// impostazione timeout
			socket.setSoTimeout(TIMEOUT);
			
			for(int i = 0; i < packetsToSend; i++){
				int packetRTT = ping(socket, i, serverAddress, serverPort); // ping al server
				
				if( packetRTT != -1 ){ // risposta pervenuta
					receivedPackets++;
					minRTT = Math.min(minRTT, packetRTT);
					maxRTT = Math.max(maxRTT, packetRTT);
					sumRTT += packetRTT;
					
					System.out.printf("PING %d %d RTT: %d ms\n", i, System.currentTimeMillis(), packetRTT);
				}
				else{ // ping non ricevuto
					System.out.printf("PING %d %d RTT: *\n", i, System.currentTimeMillis());
				}
			}
		}
		catch(SocketException e){
			System.out.println("Errore nella socket");
			e.printStackTrace();
			return;
		}
		catch(UnknownHostException e){
			System.out.println("Host sconosciuto");
			return;
		}
		catch(IOException e){
			System.out.println("Errore nella comunicazione con il server");
			e.printStackTrace();
			return;
		}
		avgRTT = ((double)sumRTT)/receivedPackets;
		packetLoss = ((float)(packetsToSend - receivedPackets))/((float)(packetsToSend));
		System.out.println("---- PING Statistics ----");
		System.out.printf("%d packets transmitted, %d packets received, %d%% packet loss\n", packetsToSend, receivedPackets, (int)(packetLoss*100));
		System.out.printf("round-trip (ms) min/avg/max = %d/%.2f/%d\n", minRTT, avgRTT, maxRTT);
	}
}

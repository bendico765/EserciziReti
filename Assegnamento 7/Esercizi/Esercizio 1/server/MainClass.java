/*
L'esercizio consiste nella scrittura di un server che offre il servizio di "Ping Pong" e del relativo programma client.

Un client invia al server un messaggio di "Ping".

Il server, se riceve il messaggio, risponde con un messaggio di "Pong".

Il client sta in attesa n secondi di ricevere il messaggio dal server (timeout) e poi termina.

Client e Server usano il protocollo UDP per lo scambio di messaggi.
*/

import java.net.*;
import java.io.IOException;
import java.lang.Thread;

public class MainClass{
	public static final int PORT = 6789;
	
	public static void main(String args[]){
		
		try(DatagramSocket serverSocket =  new DatagramSocket(PORT);)
		{
			DatagramPacket receivedPacket;
			DatagramPacket toSentPacket;
			InetAddress clientIP;
			int clientPort;
			byte buffer[];
			int length;
			
			length = 100;
			buffer = new byte[length];
			
			// il server si mette in ascolto di datagrammi in arrivo
			receivedPacket = new DatagramPacket(buffer, length);
			serverSocket.receive(receivedPacket);
			
			// mando la risposta al client
			buffer = "PONG\n".getBytes();
			clientIP = receivedPacket.getAddress();
			clientPort = receivedPacket.getPort();
			toSentPacket = new DatagramPacket(buffer, buffer.length, clientIP, clientPort);
			
			serverSocket.send(toSentPacket);
		}
		catch(SocketException e){
			System.out.println(e);
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
}

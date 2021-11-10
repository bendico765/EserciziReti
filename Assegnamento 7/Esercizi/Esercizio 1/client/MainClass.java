import java.net.*;
import java.io.IOException;

public class MainClass{
	public static final int PORT = 6789;
	public static final int TIMEOUT = 2000;
	
	public static void main(String args[]){
		try(DatagramSocket socket =  new DatagramSocket();)
		{
			DatagramPacket receivedAnswer; // risposta ricevuta dal server
			DatagramPacket dataToServer; // messaggio da mandare al server
			byte buffer[]; // buffer di appoggio
			int length; // lunghezza messaggio nel buffer
			InetAddress serverIP; // indirizzo IP del server
			
			// imposto il timeout per la receive
			socket.setSoTimeout(TIMEOUT);
				
			serverIP = InetAddress.getByName("127.0.0.1");
			
			// mando il messaggio al server
			buffer = "PING\n".getBytes();
			length = buffer.length;
			dataToServer = new DatagramPacket(buffer, length, serverIP, PORT);
			
			socket.send(dataToServer);
			System.out.println("PING");
			
			// ricevo il messaggio dal server
			buffer = new byte[100];
			receivedAnswer = new DatagramPacket(buffer, buffer.length);
			socket.receive(receivedAnswer);
			System.out.println(new String(receivedAnswer.getData()));
		}
		catch(SocketException e){ // errore creazione socket
			System.out.println(e);
			e.printStackTrace();
		}
		catch(UnknownHostException e){ // errore reperimento indirizzo ip host server
			System.out.println(e);
			e.printStackTrace();
		}
		catch(IOException e){ // errore nell'IO della socket
			System.out.println(e);
			e.printStackTrace();
		}
	}
}

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.net.UnknownHostException;

public class ClientMainClass{
	public static void main(String args[]){
		int port;
		String ipAddress;
		BufferedReader in; // reader per leggere da console
		
		// parsing parametri d'ingresso
		try{
			ipAddress = args[0];
			port = Integer.parseInt(args[1]);
			if( port <= 0 ){
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException e){ 
			System.out.println("Numero di porta non valido");
			return;
		}
		catch(IndexOutOfBoundsException e){ 
			System.out.println("Specificare un numero di porta");
			return;
		}
		
		in = new BufferedReader(new InputStreamReader(System.in));
		
		// connessione al server
		try(
			SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress(ipAddress, port));
		){
			String msgToSend;
			ByteBuffer buffer;
			
			// leggo da console il messaggio da mandare
			msgToSend = in.readLine();
			
			// preparo il buffer a mandare la stringa
			buffer = ByteBuffer.wrap(msgToSend.getBytes());
			
			// mando al server il contenuto del buffer
			clientSocket.write(buffer);
			
			// passo da lettura a scrittura
			buffer.flip();
			
			// leggo la risposta del server
			clientSocket.read(buffer);
			
			System.out.println(new String(buffer.array(), 0, buffer.position()));
		}
		catch(UnknownHostException e){
			System.out.println("Host sconosciuto");
		}
		catch(IOException e){
			System.out.println("Errore durante la comunicazione con il server");
		}
	}
}

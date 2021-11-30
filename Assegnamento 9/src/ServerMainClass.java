import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.Iterator;
/*
Scrivere un programma echo server usando la libreria java NIO e, in particolare, il Selector e canali in modalità non bloccante, e un programma echo client, usando NIO (va bene anche con modalità bloccante).

Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client e li rispedisce (eventualmente aggiungendo "echoed by server" al messaggio ricevuto).

Il client legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto dal server.
*/

public class ServerMainClass{
	public static final int BUFFERSIZE = 71;
	
	public static void main(String args[]){
		ServerSocketChannel serverChannel; // canale del server
		Selector selector;
		int port;
		
		// parsing numero porta
		try{
			port = Integer.parseInt(args[0]);
			if(port <= 0){
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
		
		// creazione server e configurazione selettore
		try{
			serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(port));
			serverChannel.configureBlocking(false);
			
			// ottenimento selettore e registrazione del canale server
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		// avvio del dispatcher
		while(true){
			Set<SelectionKey> readyKeys; // insieme di chiavi con operazioni non bloccanti
			Iterator<SelectionKey> iterator; // iteratore per readyKeys
			// scopro quali keys sono pronte per operazioni non bloccanti
			try{
				selector.select();
			}
			catch(IOException e){
				e.printStackTrace();
				break;
			}
			
			// itero il set di chiavi pronte e non bloccanti
			readyKeys = selector.selectedKeys();
			iterator = readyKeys.iterator();
			while(iterator.hasNext()){
				SocketChannel clientSocketChannel; // canale associato alla key
				ByteBuffer buffer; // buffer associato alla key
				SelectionKey key; // chiave su cui effettuare operazioni di accept/read/write
				
				key = iterator.next();
				iterator.remove(); // rimozione della key dal ready set
				try{
					if( key.isAcceptable() ){ // nuovo client pronto a connettersi
						SelectionKey newKey; // nuova chiave da impostare in lettura
						
						clientSocketChannel = serverChannel.accept();
						clientSocketChannel.configureBlocking(false);
						newKey = clientSocketChannel.register(selector, SelectionKey.OP_READ);
						
						// alloco un buffer dedicato a letture/scritture dal/al client
						newKey.attach(ByteBuffer.allocate(BUFFERSIZE));
					}
					if( key.isReadable() ){ // un client ha mandato un messaggio
						SelectionKey newKey; // nuova chiave per registrare la scrittura
						
						clientSocketChannel = (SocketChannel) key.channel();
						buffer = (ByteBuffer) key.attachment();
						
						// effettuo la lettura (fino a riempire il buffer al caso pessimo)
						clientSocketChannel.read(buffer);
						
						// cambio la chiave da lettura a scrittura
						buffer.flip();
						newKey = key.interestOps(SelectionKey.OP_WRITE);
					}
					if( key.isWritable() ){ // è possibile mandare una risposta al client
						clientSocketChannel = (SocketChannel) key.channel();
						buffer = (ByteBuffer) key.attachment();
						
						// svuoto il buffer
						clientSocketChannel.write(buffer);
					
						// verifico se le scritture sono terminate
						if( buffer.remaining() == 0 ){
							try{
							 clientSocketChannel.socket().close();
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}			
					}
				}
				catch(IOException e){
					e.printStackTrace();
					return;
				}
			}
		}

		return;
	}
}

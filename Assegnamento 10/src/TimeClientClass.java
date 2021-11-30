import java.net.MulticastSocket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;

public class TimeClientClass{
	public static int PACKETS = 10; // numero pacchetti da ricevere
	public static int BUFFERSIZE = 1024;
	public static String INTERFACE = "wLan1"; // interfaccia da usare nel multicast
	
	public static void main(String args[]){
		String addressName; // indirizzo multicast
		int port; // porta del servizio
		
		// parsing argomenti
		try{
			addressName = args[0];
			port = Integer.parseInt(args[1]);
			if(port <= 0){
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException e){ 
			System.out.println("Numero di porta non valido");
			return;
		}
		catch(IndexOutOfBoundsException e){ 
			System.out.println("Errore con i paramtri");
			System.out.println("TimeClientClass indirizzo_multicast numero_porta");
			return;
		}
		
		try(MulticastSocket socket = new MulticastSocket(port);){
			InetSocketAddress group; // gruppo multicast
			NetworkInterface netInf; // interfaccia da usare
			DatagramPacket packetToReceive; // pacchetto UDP da ricevere
			byte[] buffer; // buffer per il pacchetto udp
			
			// configurazione gruppo ed interfaccia
			group = new InetSocketAddress(InetAddress.getByName(addressName), port);
			netInf = NetworkInterface.getByName(INTERFACE);
			// netInf = null;
			
			socket.joinGroup(group, netInf);
			
			// ricevimento dei pacchetti dall'indirizzo multicast
			buffer = new byte[BUFFERSIZE];
			for(int i = 0; i < PACKETS; i++){
				packetToReceive = new DatagramPacket(buffer, BUFFERSIZE);
				socket.receive(packetToReceive);
				System.out.println(new String(packetToReceive.getData()));
			}
			
			// uscita dal gruppo multicast
			socket.leaveGroup(group, netInf);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return;
	}
}

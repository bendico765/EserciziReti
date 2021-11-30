import java.time.LocalDateTime;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Thread;

/*
	Definire un Server TimeServer, che

		invia su un gruppo di multicast  dategroup, ad intervalli regolari, la data e l’ora.  
		attende tra un invio ed il successivo un intervallo di tempo simulata mediante il metodo  sleep().

	L’indirizzo IP di dategroup viene introdotto  da linea di comando.

	Definire quindi un client TimeClient che si unisce a dategroup e riceve, per dieci volte consecutive, data ed ora, le visualizza, quindi termina.
*/

public class TimeServerClass{
	public static final int DELAY = 300; // ritardo da un pacchetto mandato in multicast e l'altro, in ms
	
	/*
		Restituisce l'orario corrente, in formato HHHH-MM-DDThh:mm:ss
		
		@return - un array di bytes contenente l'orario in formato testuale
	*/
	private static byte[] getTime(){
		return LocalDateTime.now().toString().getBytes();
	}
	 
	public static void main(String args[]){
		InetAddress multicastAddress; 
		DatagramPacket packetToSend; // pacchetto per spedire l'orario
		String addressName; // indirizzo multicast da usare
		int port; // porta da usare per il servizio di timeserver
		byte[] buffer; // buffer di supporto
		
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
			System.out.println("TimeServerClass indirizzo_multicast numero_porta");
			return;
		}
		
		// avvio del server e invio continuo dei messaggi multicast
		try( DatagramSocket socket = new DatagramSocket();)
		{
			multicastAddress = InetAddress.getByName(addressName);
			while(true){
				buffer = getTime();
				packetToSend = new DatagramPacket(buffer, buffer.length, multicastAddress, port);
				socket.send(packetToSend);
				Thread.sleep(DELAY);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return;
	}
}

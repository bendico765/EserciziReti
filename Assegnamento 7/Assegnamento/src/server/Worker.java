import java.net.*;
import java.lang.Math;
import java.lang.Thread;
import java.io.IOException;

public class Worker implements Runnable{
	private DatagramSocket socket; // socket del server
	private DatagramPacket receivedPacket; // datagramma di pertinenza del thread
	private double lossRate; // rateo di perdita pacchetti, espresso tra 0.0 e 1.0
	private int delayUpperBound; // limite superiore al ritardo simulato (in ms)
	
	public Worker(DatagramSocket socket, DatagramPacket receivedPacket, double lossRate, int delayUpperBound){
		this.socket = socket;
		this.receivedPacket = receivedPacket;	
		this.lossRate = lossRate;
		this.delayUpperBound = delayUpperBound;
	}
	
	public void run(){
		DatagramPacket toSendPacket; // pacchetto da mandare al client
		InetAddress clientAddress; // indirizzo IP client
		int clientPort; // porta usata dal client
		
		// reperimento informazioni client
		clientAddress = receivedPacket.getAddress();
		clientPort = receivedPacket.getPort();
		
		// controllo se rispondere o simulare la perdita
		if( Math.random() >= this.lossRate ){ 
			int delay; // ritardo simulato
			
			// simulazione del ritardo
			delay = (int)(Math.random() * this.delayUpperBound);
			try{
				Thread.sleep(delay);
			}
			catch(InterruptedException e){}
			
			// invio risposta al client
			toSendPacket = new DatagramPacket(receivedPacket.getData(), receivedPacket.getLength(), clientAddress, clientPort);
			try{
				this.socket.send(toSendPacket);
			}
			catch(IOException e){
				System.out.printf("%s:%d> %s ACTION: IO error\n", clientAddress.getHostAddress(), clientPort, new String(receivedPacket.getData()));
			}
			
			System.out.printf("%s:%d> %s ACTION: delayed %d ms\n", clientAddress.getHostAddress(), clientPort, new String(receivedPacket.getData()), delay);
		}
		else{
			System.out.printf("%s:%d> %s ACTION: not sent\n", clientAddress.getHostAddress(), clientPort, new String(receivedPacket.getData()));
		}
	}
}

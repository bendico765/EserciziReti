/*
Scrivere un programma JAVA che implementi un server che apre una serversocket su una porta e sta in attesa di richieste di connessione.

Quando arriva una richiesta di connessione, il server accetta la connessione, trasferisce al client un file e poi chiude la connessione.

Ulteriori dettagli:

    Il server gestisce una richiesta per volta 

    Il server invia sempre lo stesso file, usate un file di testo

Per il client potete usare telnet. Qui sotto un esempio di utilizzo:

*/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class MainClass{
	public static final int PORT = 8081;
	public static final String FILEPATH = "lorem.txt";
	
	public static String readFileContent(String path){
		String result = "";
		byte[] buffer = new byte[1024];
		
		try(FileInputStream in = new FileInputStream(path);){
			while(in.read(buffer) != -1){
				result = result.concat(new String(buffer));
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return result;
	}
	
	public static void main(String args[]){
		ServerSocket passiveSocket;
		String message;
		
		// ottenimento del contenuto del file
		message = readFileContent(FILEPATH);
		
		// apertura della passive socket del server
		try{
			passiveSocket = new ServerSocket(PORT);
		}
		catch(Exception e){
			System.out.println(e);
			return;
		}
		
		// attesa di client
		while(true){
			System.out.println("Waiting for clients");
			try(Socket clientSocket = passiveSocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));)
			{
				// il clint è connesso
				writer.write(message);
				writer.flush();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Client Served");
		}
	}
}

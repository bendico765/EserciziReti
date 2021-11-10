import java.net.Socket;
import java.io.*;

public class Worker implements Runnable{
	private Socket clientSocket; // socket con cui comunicare con il client
	private String mediaFolderPath;	// percorso della cartella con i file multimediali
	
	public Worker(Socket clientSocket, String mediaFolderPath){
		this.clientSocket = clientSocket;
		this.mediaFolderPath = mediaFolderPath;
	}
	
	/**
	 * Legge il file indicato dal percorso e ne restituisce il contenuto
	 * 
	 * @param path - Percorso locale al file su disco
	 * @return - contenuto del file, o null in caso di errore
	*/
	private byte[] getFileContent(String path){
		byte[] fileContent;
		File requestedFile;
		FileInputStream fileInput;
		
		// apro il file
		requestedFile = new File(path);
		
		if( requestedFile.exists() && requestedFile.isFile() ){	
			try{
				int fileSize = (int)requestedFile.length();
				fileInput = new FileInputStream(path);
				fileContent = new byte[fileSize];
				fileInput.read(fileContent, 0, fileSize);
			}
			catch(IOException e){
				fileContent = null;
			}
		}
		else{
			fileContent = null;
		}
		
		return fileContent;
	}
	
	/**
	 * Legge da stream la richiesta HTTP fatta dall'utente e la restituisce
	 * 
	 * @param stream - Stream da cui leggere
	 * @return - contenuto letto, o null in caso di errore
	*/
	private String getClientRequest(BufferedReader stream){
		byte[] buffer; // buffer di supporto per leggere da stream
		int byteRead;
		String result;

		buffer = new byte[1024];
		result = "";
		try{
			result = stream.readLine();
		}
		catch(IOException e){
			result = null;
		}
		return result;
	}
	
	/**
	 * Fa il parsing della richiesta HTTP restituendo il nome della 
	 * risorsa richiesta.
	 *
	 * @param request - Richiesta http da parsare
	 * @return - la risorsa richiesta
	*/
	private String parseHttpRequest(String request){
		String resource;
		
		try{
			resource = request.split("\\s+")[1];
		}
		catch(Exception e){
			resource = null;
		}
		
		return resource;
	}
	
	private String getBadRequestResponse(){
		return "HTTP/1.1 400 Bad request\r\nContent-Type: text/html\r\n\r\n<h1>ERROR 400</h1><p>Bad request</p>\n";
	}
	
	private String getMethodNotAllowedResponse(){
		return "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n<h1>ERROR 405</h1><p>Method not allowed</p>\n";
	}
	
	private String getNotFoundResponse(){
		return "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n<h1>ERROR 404</h1><p>Not Found</p>\n";
	}
	
	private String getInternalServerErrorResponse(){
		return "HTTP/1.1 500 Internal Server Error\r\nContent-Type: text/html\r\n\r\n<h1>ERROR 500</h1><p>Internal Server Error</p>\n";
	}
	
	/**
	 * Restituisce il valore del campo Content-Type da mandare nella
	 * risposta http in base al nome della risorsa.
	 *
	 * @params resourceName - il nome della risorsa da mandare come risposta http
	 * @return - il valore del campo content-type
	*/
	private String getContentType(String resourceName){
		String extension;
		
		try{
			extension = resourceName.split("\\.")[1];
		}
		catch(Exception e){ return null; }
		
		switch( extension ){
			case "jpeg":
			case "jpg":
				return "image/jpeg";
			case "mp4":
				return "video/mp4";
			case "png":
				return "image/png";
			default:
				return null;
		}
	}
	
	public void run(){
		try(
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream())); // input stream
			DataOutputStream outToClient = new DataOutputStream(this.clientSocket.getOutputStream()); // output stream
		)
		{
			String httpRequest; // richiesta http del client
			
			// lettura della richiesta HTTP del client
			httpRequest = getClientRequest(inFromClient);
			
			if( httpRequest == null ){ // errore nel reperire richiesta
				outToClient.writeBytes(getBadRequestResponse());
			}
			else{
				if( httpRequest.startsWith("GET") ){ // servo richiesta get
					int contentLength; 
					String contentType;
					String requiredResource; // risorsa richiesta dal client
					String filepath; // percorso alla risorsa richiesta dal client
					byte[] content;
					
					// faccio il parsing della richiesta per sapere la risorsa richiesta dal client
					requiredResource = parseHttpRequest(httpRequest);
					if( requiredResource == null ){ // errore nel parsing
						outToClient.writeBytes(getBadRequestResponse());
					}
					else{
						filepath = this.mediaFolderPath + requiredResource;
						
						// reperisco il file da mandare
						content = getFileContent(filepath);
						if( content == null ){ // risorsa inesistente
							outToClient.writeBytes(getNotFoundResponse());
						}
						else{ // mando il file al client
							contentLength = content.length;
							contentType = getContentType(requiredResource);
							// invio header
							outToClient.writeBytes("HTTP/1.1 200 OK\n");
							outToClient.writeBytes(String.format("Content-Length: %d\n", contentLength));
							outToClient.writeBytes(String.format("Content-Type: %s\n", contentType));
							outToClient.writeBytes("\n");
							// invio content
							outToClient.write(content, 0, contentLength);
						}
					}
				}
				else{ // richiesta non è di tipo get
					outToClient.writeBytes(getMethodNotAllowedResponse());
				}
			}
			outToClient.flush();
		}
		catch(Exception e){ // eccezione non gestita
			try{
				this.clientSocket.getOutputStream().write(getInternalServerErrorResponse().getBytes());
			}
			catch(IOException e2){}
			e.printStackTrace();
		}
		finally{
			// chiusura socket
			try{
				this.clientSocket.close();
			}
			catch(IOException e){
				System.out.println("Errore nella chiusura della socket");
			}
		}
	}
}

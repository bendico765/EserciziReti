import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class JSONFileHandler{
	private static int BUFFER_SIZE = 1024;
	
	/*
	 *	Serializza l'oggetto in formato JSON e lo scrive sul file specificato.
	 *  Se il file non esiste viene creato.
	 *
	 *  @param filepath - percorso al file da creare
	 *  @param obj - oggetto da serializzare
	 *  @return - true se l'operazione è andata a buon fine, falso altrimenti
	*/
	public static boolean writeToFile(String filepath, Object obj){
		ObjectMapper objectMapper; // mapper di jackson
		FileChannel channel; // canale del file su cui scrivere
		ByteBuffer buf; // buffer di supporto 
		
		// inizializzo il mapper
		objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		// tento la scrittura su file
		try(
			FileOutputStream out = new FileOutputStream(filepath);
		)
		{
			buf = ByteBuffer.wrap( objectMapper.writeValueAsString(obj).getBytes() );
			channel = out.getChannel();
			
			// scrivo sul file fino ad esaurire il buffer
			while( buf.hasRemaining() )
				channel.write(buf);
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	 *	Deserializza il contenuto del file json passato come argomento e lo restituisce.
	 *
	 *  @param filepath - percorso al file json da parsare
	 *  @return - true se l'operazione è andata a buon fine, falso altrimenti
	*/
	public static Object readFromFile(String filepath){
		ObjectMapper objectMapper;
		
		objectMapper = new ObjectMapper();
		
		try(
			FileInputStream in = new FileInputStream(filepath);
		)
		{
			return objectMapper.readValue(in, Account[].class);
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
}

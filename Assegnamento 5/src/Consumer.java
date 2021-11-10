import java.io.*;

public class Consumer implements Runnable{
	private ConcurrentList<String> namesList; // coda concorrente per i nomi
	
	public Consumer(ConcurrentList<String> namesList){
		this.namesList = namesList;
	}
	
	public void run(){		
		String directoryPath; // percorso assoluto della cartella
		
		while( (directoryPath = namesList.remove()) != null ){
			File dir = new File(directoryPath);
			String files[]; // nomi dei files contenuti nella cartella
			String directoryName; // nome della cartella
			String concatFiles;
			
			// concateno in una sola stringa i nomi dei files nella cartella
			files = dir.list();
			concatFiles = "";
			for(String s: files)
				concatFiles = concatFiles.concat(s).concat(";");
				
			// estrapolo il nome della cartella dal percorso assoluto
			directoryName = directoryPath.substring(directoryPath.lastIndexOf("/") + 1);
			
			System.out.printf("Directory [%s], Files [%s]\n", directoryName, concatFiles);
		}
	}
}

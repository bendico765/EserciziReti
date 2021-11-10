import java.io.*;

public class Producer implements Runnable{
	String path; // percorso della cartella da esplorare
	private ConcurrentList<String> namesList; // coda concorrente per i nomi
	
	public Producer(String path, ConcurrentList<String> namesList){
		this.path = path;
		this.namesList = namesList;
	}
	
	// metodo per visitare ricorsivamente cartelle e sotto cartelle, dato 
	// un percorso assoluto
	private void visitDirectory(String path){
		File dir = new File(path);
		if( dir.isDirectory() ){
			File[] elements = dir.listFiles(); // elementi nella directory
			for(File element: elements){
				if( element.isDirectory() ){ // se è una cartella, metto in coda e visito ricorsivamente
					this.namesList.add(element.getAbsolutePath());
					visitDirectory(element.getAbsolutePath());
				}
			}
		}
	}
	
	public void run(){
		File f = new File(this.path);
		if( f.exists() && f.isDirectory()){
			this.namesList.add(this.path);
			visitDirectory(this.path);
		}
		else{
			System.out.println("Il path non corrisponde ad alcuna directory valida.");
		}
		return;
	}
}

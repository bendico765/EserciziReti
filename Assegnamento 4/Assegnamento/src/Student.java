import java.util.Random;
import java.lang.Thread;

public class Student implements Runnable{
	private Tutor tutor;
	private int numberOfAccesses; // numero di accessi che l'utente farà in lab.
	private int workTimeUpperBound; // limite superiore al tempo di attesa possibile (in millisecondi)
									// in cui il computer viene detenuto
	private int waitTimeUpperBound; // limite superiore al tempo di attesa possibile (in millisecondi)
									// tra l'uso di un computer ed il successivo
	
	public Student(Tutor tutor, int numberOfAccesses, int workTimeUpperBound, int waitTimeUpperBound){
		this.tutor = tutor;
		this.numberOfAccesses = numberOfAccesses;
		this.workTimeUpperBound = workTimeUpperBound; 
		this.waitTimeUpperBound = waitTimeUpperBound;
	}
	
	public int getWorkTimeUpperBound(){
		return this.workTimeUpperBound;
	}
	
	public int getWaitTimeUpperBound(){
		return this.waitTimeUpperBound;
	}
	
	public int getNumberOfAccesses(){
		return this.numberOfAccesses;
	}
	
	public Tutor getTutor(){
		return this.tutor;
	}
	
	public void run(){
		Random r = new Random();
		for(int i = 0; i < numberOfAccesses; i++){
			int computerIndex = r.nextInt(tutor.getNumberComputers()) + 1;
			int workTime = r.nextInt(workTimeUpperBound);
			int waitTime = r.nextInt(waitTimeUpperBound);
			
			// chiedo al tutor l'uso del computer
			tutor.studentUsesComputer(computerIndex); 
			try{
				Thread.sleep(workTime);
			}
			catch(Exception e){}
			tutor.studentLeavesComputer(computerIndex);
			
			// attendo prima di fare un nuovo accesso in aula
			try{
				Thread.sleep(waitTime);
			
			}
			catch(Exception e){}
		}
	}
}

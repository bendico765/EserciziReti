import java.util.Random;
import java.lang.Thread;

public class GraduateStudent extends Student implements Runnable{
	private int computerIndex; // indice del computer che il tesista deve usare
	
	public GraduateStudent(Tutor tutor, int numberOfAccesses, int workTimeUpperBound, int waitTimeUpperBound, int computerIndex){
		super(tutor, numberOfAccesses, workTimeUpperBound, waitTimeUpperBound);
		this.computerIndex = computerIndex;
	}
	
	public void run(){
		Random r = new Random();
		Tutor tutor = super.getTutor();
		for(int i = 0; i < super.getNumberOfAccesses(); i++){
			int workTime = r.nextInt(super.getWorkTimeUpperBound());
			int waitTime = r.nextInt(super.getWaitTimeUpperBound());
			
			// chiedo al tutor l'uso del computer
			tutor.graduateUsesComputer(this.computerIndex); 
			try{
				Thread.sleep(workTime);
			}
			catch(Exception e){}
			tutor.graduateLeavesComputer(this.computerIndex);
			
			// attendo prima di fare un nuovo accesso in aula
			try{
				Thread.sleep(waitTime);
			
			}
			catch(Exception e){}
		}
	}
}

import java.util.Random;
import java.lang.Thread;

public class Teacher extends Student implements Runnable{
	
	public Teacher(Tutor tutor, int numberOfAccesses, int workTimeUpperBound, int waitTimeUpperBound){
		super(tutor, numberOfAccesses, workTimeUpperBound, waitTimeUpperBound);
	}
	
	public void run(){
		Random r = new Random();
		Tutor tutor = super.getTutor();
		for(int i = 0; i < super.getNumberOfAccesses(); i++){
			int workTime = r.nextInt(super.getWorkTimeUpperBound());
			int waitTime = r.nextInt(super.getWaitTimeUpperBound());
			
			// chiedo al tutor l'uso di tutti i computer
			tutor.useAllComputers(); 
			try{
				Thread.sleep(workTime);
			}
			catch(Exception e){}
			tutor.leaveAllComputers();
			
			// attendo prima di fare un nuovo accesso in aula
			try{
				Thread.sleep(waitTime);
			
			}
			catch(Exception e){}
		}
	}
}

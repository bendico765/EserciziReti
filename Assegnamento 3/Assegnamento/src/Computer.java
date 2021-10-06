import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Computer{
	private ReentrantLock computerLock = new ReentrantLock();
	
	private Condition studentGo = computerLock.newCondition(); // var. di cond. su cui attendono gli studenti
	private Condition graduateGo = computerLock.newCondition(); // var. di cond. su cui attendono i tesisti
	private Condition teacherGo = computerLock.newCondition(); // var. di cond. su cui attendono i docenti
	
	// numero di studenti/tesisti/insegnanti in attesa
	private int awaitingStudents = 0;
	private int awaitingGraduates = 0;
	private int awaitingTeachers = 0;
	
	// numero di studenti/tesisti/insegnanti attivi sul computer
	private int activeStudents = 0;
	private int activeGraduates = 0;
	private int activeTeachers = 0;
	
	public Computer(){}
	
	public void studentUsesComputer(){
		computerLock.lock();
		awaitingStudents++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0 || awaitingGraduates > 0 || awaitingTeachers > 0){
			try{
				studentGo.await();
			}
			catch(InterruptedException e){}
		}
		// lo studente ha acquisito il computer
		awaitingStudents--;
		activeStudents++;
		computerLock.unlock();
	}
	
	public void studentLeavesComputer(){
		computerLock.lock();
		activeStudents--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		if( awaitingTeachers > 0 ){
			teacherGo.signal();
		}
		else{
			if( awaitingGraduates > 0 ){
				graduateGo.signal();
			}
			else{
				if( awaitingStudents > 0 ){
					studentGo.signal();
				}
			}
		}
		computerLock.unlock();
	}
	
	public void graduateUsesComputer(){
		computerLock.lock();
		awaitingGraduates++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0 || awaitingTeachers > 0){
			try{
				graduateGo.await();
			}
			catch(InterruptedException e){}
		}
		// il tesista ha acquisito il computer
		awaitingGraduates--;
		activeGraduates++;
		computerLock.unlock();
	}
	
	public void graduateLeavesComputer(){
		computerLock.lock();
		activeGraduates--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		if( awaitingTeachers > 0 ){
			teacherGo.signal();
		}
		else{
			if( awaitingGraduates > 0 ){
				graduateGo.signal();
			}
			else{
				if( awaitingStudents > 0 ){
					studentGo.signal();
				}
			}
		}
		computerLock.unlock();
	}
	
	public void teacherUsesComputer(){
		computerLock.lock();
		awaitingTeachers++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0){
			try{
				teacherGo.await();
			}
			catch(InterruptedException e){}
		}
		// il professore ha acquisito il computer
		awaitingTeachers--;
		activeTeachers++;
		computerLock.unlock();
	}
	
	public void teacherLeavesComputer(){
		computerLock.lock();
		activeTeachers--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		if( awaitingTeachers > 0 ){
			teacherGo.signal();
		}
		else{
			if( awaitingGraduates > 0 ){
				graduateGo.signal();
			}
			else{
				if( awaitingStudents > 0 ){
					studentGo.signal();
				}
			}
		}
		computerLock.unlock();
	}
}

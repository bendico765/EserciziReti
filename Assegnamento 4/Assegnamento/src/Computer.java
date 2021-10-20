import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.LinkedList;

public class Computer{
	/*
	private ReentrantLock computerLock = new ReentrantLock();
		
	private Condition studentGo = computerLock.newCondition(); // var. di cond. su cui attendono gli studenti
	private Condition graduateGo = computerLock.newCondition(); // var. di cond. su cui attendono i tesisti
	private Condition teacherGo = computerLock.newCondition(); // var. di cond. su cui attendono i docenti
	*/
	// numero di studenti/tesisti/insegnanti in attesa
	private int awaitingStudents = 0;
	private int awaitingGraduates = 0;
	private int awaitingTeachers = 0;
	
	// numero di studenti/tesisti/insegnanti attivi sul computer
	private int activeStudents = 0;
	private int activeGraduates = 0;
	private int activeTeachers = 0;
	
	public Computer(){}
	
	public synchronized void studentUsesComputer(){
		awaitingStudents++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0 || awaitingGraduates > 0 || awaitingTeachers > 0){
			try{
				wait();
			}
			catch(InterruptedException e){}
		}
		// lo studente ha acquisito il computer
		awaitingStudents--;
		activeStudents++;
	}
	
	public synchronized void studentLeavesComputer(){
		activeStudents--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		notifyAll();
	}
	
	public synchronized void graduateUsesComputer(){
		awaitingGraduates++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0 || awaitingTeachers > 0){
			try{
				wait();
			}
			catch(InterruptedException e){}
		}
		// il tesista ha acquisito il computer
		awaitingGraduates--;
		activeGraduates++;
	}
	
	public synchronized void graduateLeavesComputer(){
		activeGraduates--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		notifyAll();
	}
	
	public synchronized void teacherUsesComputer(){
		awaitingTeachers++;
		while( activeStudents > 0 || activeGraduates > 0 || activeTeachers > 0){
			try{
				wait();
			}
			catch(InterruptedException e){}
		}
		// il professore ha acquisito il computer
		awaitingTeachers--;
		activeTeachers++;
	}
	
	public synchronized void teacherLeavesComputer(){
		activeTeachers--;
		// "risveglio" eventuali insegnanti, tesisti o studenti
		notifyAll();
	}
}

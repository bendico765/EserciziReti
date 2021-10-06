/*
Il laboratorio di Informatica del Polo Marzotto è utilizzato da tre tipi di utenti, studenti, tesisti e professori ed ogni utente deve fare una richiesta al tutor per accedere al laboratorio. I computers del laboratorio sono numerati da 1 a 20. Le richieste di accesso sono diverse a seconda del tipo dell'utente:

I professori accedono in modo esclusivo a tutto il laboratorio, poichè hanno necessità di utilizzare tutti i computers per effettuare prove in rete.
I tesisti richiedono l'uso esclusivo di un solo computer, identificato dall'indice i, poichè su quel computer è istallato un particolare software necessario per lo sviluppo della tesi.
Gli studenti richiedono l'uso esclusivo di un qualsiasi computer.

I professori hanno priorità su tutti nell'accesso al laboratorio, i tesisti hanno priorità sugli studenti.

Nessuno può essere interrotto mentre sta usando un computer. Scrivere un programma JAVA che simuli il comportamento degli utenti e del tutor. Il programma riceve in ingresso il numero di studenti, tesisti e professori che utilizzano il laboratorio ed attiva un thread per ogni utente. Ogni utente accede k volte al laboratorio, con k generato casualmente. Simulare l'intervallo di tempo che intercorre tra un accesso ed il successivo e l'intervallo di permanenza in laboratorio mediante il metodo sleep. Il tutor deve coordinare gli accessi al laboratorio. Il programma deve terminare quando tutti gli utenti hanno completato i loro accessi al laboratorio.
*/

import java.util.Random;
import java.util.Scanner;
import java.lang.Thread;
import java.util.ArrayList;

public class MainClass{
	public static final int NUMBER_OF_COMPUTERS = 20;
	public static final int NUMBER_OF_ACCESSES_UPPER_BOUND = 5; // limite superiore al numero di accessi
	public static final int WORK_TIME_UPPER_BOUND = 1000;
	public static final int AWAIT_TIME_UPPER_BOUND = 1000;
	
	public static void main(String args[]){
		Tutor tutor;
		Random r;
		Scanner scanner;
		int numberStudents;
		int numberGraduateStudents;
		int numberTeachers;
		int numberOfAccesses;
		ArrayList<Thread> threadList; // lista  di thread generati
		
		tutor = new Tutor(NUMBER_OF_COMPUTERS);
		r = new Random();
		scanner = new Scanner(System.in);
		// inserimento del numero di studenti, tesisti e insegnanti
		numberStudents = scanner.nextInt();
		numberGraduateStudents = scanner.nextInt();
		numberTeachers = scanner.nextInt();
		numberOfAccesses = r.nextInt(NUMBER_OF_ACCESSES_UPPER_BOUND);
		threadList = new ArrayList<Thread>();
		
		if(numberStudents < 0 || numberGraduateStudents < 0 || numberTeachers < 0){
			System.out.println("Si è verificato un errore con i parametri di ingresso");
			return;
		}
		
		// generazione studenti
		for(int i = 0; i < numberStudents; i++){
			Thread t = new Thread(new Student(tutor, numberOfAccesses, WORK_TIME_UPPER_BOUND, AWAIT_TIME_UPPER_BOUND));
			threadList.add(t);
			t.start();
		}
		
		// generazione tesisti
		for(int i = 0; i < numberGraduateStudents; i++){
			int graduateStudentIndex = r.nextInt(NUMBER_OF_COMPUTERS) + 1;
			Thread t = new Thread(new GraduateStudent(tutor, numberOfAccesses, WORK_TIME_UPPER_BOUND, AWAIT_TIME_UPPER_BOUND, graduateStudentIndex));
			threadList.add(t);
			t.start();
		}
		
		// generazione professori
		for(int i = 0; i < numberTeachers; i++){
			Thread t = new Thread(new Teacher(tutor, numberOfAccesses, WORK_TIME_UPPER_BOUND, AWAIT_TIME_UPPER_BOUND));
			threadList.add(t);
			t.start();
		}
		
		// attesa della terminazione dei thread
		for(Thread t: threadList){
			try{
				t.join();
			}
			catch(InterruptedException e){} 
		}
		System.out.println("OK");
		return;
	}
}

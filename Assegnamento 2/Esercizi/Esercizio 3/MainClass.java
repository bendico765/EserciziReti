import java.util.concurrent.*;
import java.util.*;

public class MainClass{
	public static final int EXP_UPPER_BOUND = 51; // limite superiore esponente
	public static final int EXP_LOWER_BOUND = 2; // limite inferiore esponente
	
	public static void main(String args[]){
		Scanner scanner; // scanner per operazioni di input
		ThreadPoolExecutor pool; 
		ArrayList<Future<Double>> futuresList; // lista per i risultati parziali
		double result; // risultato finale
		int base; // numero n
		
		scanner = new Scanner(System.in);
		pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		futuresList = new ArrayList<Future<Double>>();
		result = 0;
		
		// prendo la base in input
		base = scanner.nextInt();
		
		// assegno i calcoli delle singole potenze al pool
		for(int i = EXP_LOWER_BOUND; i < EXP_UPPER_BOUND; i++){
			Future<Double> powerResult = pool.submit( new Power(base,i));
			futuresList.add(powerResult);
		}
		// sommatoria finale 
		for(int i = EXP_LOWER_BOUND; i < EXP_UPPER_BOUND; i++){
			Future<Double> powerResult = futuresList.get(i - EXP_LOWER_BOUND);
			try{
				result += powerResult.get();
			}
			catch (Exception e){
				System.out.println("ERROR\n");
				pool.shutdownNow();
				return;
			}
		}
		System.out.printf("%.2f\n", result);
		pool.shutdown();
		return;
	}
}

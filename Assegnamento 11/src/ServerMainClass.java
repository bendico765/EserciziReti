import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class ServerMainClass{
	public static final int N_DAYS = 3;
	public static final int N_SESSIONS = 12;
	public static final int N_SPEAKERS = 5;
	public static final int PORT = 6789;
	public static final String SYM_NAME = "CONGRESS-SERVER";
	
	public static void main(String args[]){
		try{
			Server server;
			ServerInterface stub;
			Registry reg;
			
			server = new Server(N_DAYS, N_SESSIONS, N_SPEAKERS);
			
			stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);
			
			LocateRegistry.createRegistry(PORT);
			reg = LocateRegistry.getRegistry(PORT);
			
			reg.rebind(SYM_NAME, stub);
			
			System.out.println("Server pronto");
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
	}
}

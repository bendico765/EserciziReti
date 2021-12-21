import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	public int getNumberSessions() throws RemoteException;
	
	public int getNumberSpeakers() throws RemoteException;
	
	public String addSpeaker(int day, int sessionNumber, int speakerPosition, String speakerName) throws RemoteException;
	
	public String getCongressTable() throws RemoteException;
}

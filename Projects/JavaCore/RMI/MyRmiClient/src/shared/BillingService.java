package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BillingService extends Remote {
	public void addNewCard(Card card) throws RemoteException;
	
	public void addMoney(Card card, double money) throws RemoteException;
	
	public void subMoney(Card card, double money) throws RemoteException;
	
	public double getCardBalance(Card card) throws RemoteException;
}

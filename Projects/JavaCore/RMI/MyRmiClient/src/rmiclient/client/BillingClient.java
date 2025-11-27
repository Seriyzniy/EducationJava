package rmiclient.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import shared.BillingService;
import shared.Card;


public class BillingClient {
	private String localhost    = "127.0.0.1";
	private String RMI_HOSTNAME = "java.rmi.server.hostname";
	private String SERVICE_PATH = "rmi://localhost/BillingService";
	
	private String[][] CARDS    = {{"Ivanov", "1213-456-7890"}, {"Petrov", "987-654-3210"}};

	private double[]   MONEYS   = {135790.0, 24680.0};
	
	private Card createCard (final int idx)
	{
		return new Card(CARDS[idx][0], CARDS[idx][1], 0);
	}
	
	private void registerCards(BillingService bs)
	{
		for (int i = 0; i < CARDS.length; i++) {
			Card card = createCard (i);
			try {
				bs.addNewCard(card);
			} catch (RemoteException e) {
				System.err.println("RemoteException : " + e.getMessage());
			}
		}
	}
	private void addMoney(BillingService bs)
	{
		for (int i = 0; i < CARDS.length; i++) {
			Card card = createCard (i);
			try {
				bs.addMoney(card, MONEYS[i]);
			} catch (RemoteException e) {
				System.err.println("RemoteException : " + e.getMessage());
			}
		}
	}
	private void getBalance(BillingService bs)
	{
		for (int i = 0; i < CARDS.length; i++) {
			Card card = createCard (i);
			try {
				System.out.println("card : " + card.getNumber() + 
						           ", balance = " + bs.getCardBalance(card));
			} catch (RemoteException e) {
				System.err.println("RemoteException : " + e.getMessage());
			}
		}
	}
	public BillingClient()
	{
		try {
			System.setProperty(RMI_HOSTNAME, localhost);

			String objectName = SERVICE_PATH;
			
			
			BillingService bs = (BillingService) Naming.lookup(objectName);
			
			
			System.out.println("\nRegister cards ...");
			registerCards(bs);
			System.out.println("Add moneys ...");
			addMoney(bs);
			System.out.println("Get balance ...\n");
			getBalance(bs);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
			System.err.println("NotBoundException : " + e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		new BillingClient();
		System.exit(0);
	}
}

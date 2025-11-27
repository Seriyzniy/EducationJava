package ThreadCollectionsClasses;

import java.util.concurrent.Exchanger;

public class MyExchanger {
	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<>();

		Thread t1 = new Thread(() -> {
		    try {
		        String response = exchanger.exchange("Привет от T1");
		        System.out.println("T1 получил: " + response);
		    } catch (InterruptedException e) {}
		});

		Thread t2 = new Thread(() -> {
		    try {
		        String response = exchanger.exchange("Привет от T2");
		        System.out.println("T2 получил: " + response);
		    } catch (InterruptedException e) {}
		});

		t1.start();
		t2.start();
	}
}

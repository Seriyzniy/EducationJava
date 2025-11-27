package ThreadCollectionsClasses;

import java.util.concurrent.CyclicBarrier;

public class MyBarier {
	public static void main(String[] args) {
		CyclicBarrier barrier = new CyclicBarrier(3, () ->
	    	System.out.println("Все прибыли, начинаем выполнение!")
		);
	
		for (int i = 1; i <= 3; i++) {
		    int id = i;
		    new Thread(() -> {
		        System.out.println("Поток " + id + " ждёт у барьера");
		        try {
		            Thread.sleep(id * 1000);
		            barrier.await(); // ждет других
		            System.out.println("Поток " + id + " прошёл барьер");
		        } catch (Exception e) { e.printStackTrace(); }
		    }).start();
		}
	}
}

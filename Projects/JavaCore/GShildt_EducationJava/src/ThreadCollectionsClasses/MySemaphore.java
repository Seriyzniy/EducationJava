package ThreadCollectionsClasses;

import java.util.concurrent.Semaphore;

public class MySemaphore {
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(3); // максимум 3 потока

		for (int i = 1; i <= 10; i++) {
		    int id = i;
		    new Thread(() -> {
		        try {
		            semaphore.acquire(); // получить разрешение
		            System.out.println("Поток " + id + " начал работу");
		            Thread.sleep(1000);
		            System.out.println("Поток " + id + " завершил работу");
		        } catch (InterruptedException ignored) {
		        } finally {
		            semaphore.release(); // вернуть разрешение
		        }
		    }).start();
		}
	}
}

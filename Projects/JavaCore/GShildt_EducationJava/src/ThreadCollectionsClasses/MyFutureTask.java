package ThreadCollectionsClasses;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MyFutureTask {
	public static void main(String[] args) {
		FutureTask<String> futureTask = new FutureTask<>(() -> {
		    Thread.sleep(2000);
		    return "Готовый результат!";
		});

		Thread t = new Thread(futureTask);
		t.start();

		System.out.println("Жду результат...");
		String result;
		try {
			result = futureTask.get(); // блокируется
			System.out.println("Результат: " + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 
	}
}

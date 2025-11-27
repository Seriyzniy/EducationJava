package OtherConcurrentClasses;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyCallable {
	 public static void main(String[] args) throws Exception {
        Callable<String> task = () -> {
            Thread.sleep(1000);
            return "Результат вычисления";
	    };
	
	    ExecutorService executor = Executors.newSingleThreadExecutor();
	    Future<String> future = executor.submit(task);
	
	    System.out.println("Ждём результат...");
	    String result = future.get(); // блокирующий вызов
	    System.out.println("Результат: " + result);
	
	    executor.shutdown();
	}
}

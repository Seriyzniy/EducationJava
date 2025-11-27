package OtherConcurrentClasses;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyCopletionService {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		CompletionService<String> cs = new ExecutorCompletionService<String>(executor);
		

		for (int i = 1; i <= 3; i++) {
		    int id = i;
		    cs.submit(() -> {
		        Thread.sleep(5000 - (id*1000)); // разные задержки
		        return "Task " + id + Thread.currentThread().getName();
		    });
		}
		Future<String> res;
		// Обрабатываем результаты в порядке запуска
		while ((res = cs.take()) != null) {
		    System.out.println(res.get()); // блокируемся на каждом
		}
		executor.shutdown();
    }
}

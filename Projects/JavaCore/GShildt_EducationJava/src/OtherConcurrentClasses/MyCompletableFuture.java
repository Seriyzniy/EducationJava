package OtherConcurrentClasses;

import java.util.concurrent.CompletableFuture;

public class MyCompletableFuture {
	public static void main(String[] args) {
		CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return "Данные";
        }).thenApply(data -> {
            System.out.println("Трансформация: " + data);
            return data.toUpperCase();
        }).thenAccept(result -> {
            System.out.println("Финальный результат: " + result);
        });

        System.out.println("Основной поток продолжает работу...");
        try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

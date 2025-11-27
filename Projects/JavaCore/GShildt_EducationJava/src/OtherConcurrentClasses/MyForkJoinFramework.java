package OtherConcurrentClasses;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MyForkJoinFramework {
	public static void main(String[] args) {
//		ForkJoinPool pool = new ForkJoinPool();
//        pool.invoke(new PrintTask(1, 20));
//        pool.shutdown();
        
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) array[i] = i + 1;

        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SumTask(array, 0, array.length));
        System.out.println("Sum = " + result);
        pool.shutdown();
	}
}


class SumTask extends RecursiveTask<Long> {
    private final int[] arr;
    private final int start, end;

    public SumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= 10) { // базовый случай
            long sum = 0;
            for (int i = start; i < end; i++) sum += arr[i];
            return sum;
        } else {
            int mid = (start + end) / 2;
            SumTask left = new SumTask(arr, start, mid);
            SumTask right = new SumTask(arr, mid, end);
            left.fork();
            long rightResult = right.compute(); // оптимизация: выполняем одну задачу сами
            long leftResult = left.join();
            return leftResult + rightResult;
        }
    }
}


class PrintTask extends RecursiveAction {
    private final int start, end;

    public PrintTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start <= 5) { // базовый случай
            for (int i = start; i <= end; i++) {
                System.out.println(Thread.currentThread().getName() + " prints " + i);
            }
        } else {
            int mid = (start + end) / 2;
            invokeAll(new PrintTask(start, mid), new PrintTask(mid + 1, end));
        }
    }
}
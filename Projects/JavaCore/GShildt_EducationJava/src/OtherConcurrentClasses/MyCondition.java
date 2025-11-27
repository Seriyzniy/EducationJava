package OtherConcurrentClasses;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCondition {
	public static void main(String[] args) {
		BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>(5);
		
		new Thread(()->{
			for(int i = 0; i < 10; i++) {
				try {
					boundedBuffer.put(i);
					System.out.println("Отправлено в буфер i = " + i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(()->{
			int x = 0;
			for(int i = 0; i < 10; i++) {
				try {
					x = boundedBuffer.take();
					System.out.println("Получено из буфера i = " + x);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

class BoundedBuffer<T> {
    private final T[] buffer;
    private int count = 0, putPtr = 0, takePtr = 0;
    
    private final Lock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition(); //Создание через Lock
    private final Condition notEmpty = lock.newCondition();

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int size) {
        buffer = (T[]) new Object[size];
    }

    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length)
                notFull.await(); // ждём, пока появится место
            buffer[putPtr] = x;
            putPtr = (putPtr + 1) % buffer.length;
            count++;
            notEmpty.signal(); // сигнал потребителю
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await(); // ждём, пока появятся данные
            T x = buffer[takePtr];
            takePtr = (takePtr + 1) % buffer.length;
            count--;
            notFull.signal(); // сигнал производителю
            return x;
        } finally {
            lock.unlock();
        }
    }
}
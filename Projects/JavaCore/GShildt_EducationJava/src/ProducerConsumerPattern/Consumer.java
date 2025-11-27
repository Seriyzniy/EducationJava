package ProducerConsumerPattern;

public class Consumer implements Runnable {
	Q q;
	Thread t;
	
	public Consumer(Q q) {
		this.q = q;
		t = new Thread(this, "Потребитель");
	}

	@Override
	public void run() {
		int n = 0;
		while(n < 9) {
			n = q.get();
		}
	}
}

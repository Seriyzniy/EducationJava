package ProducerConsumerPattern;

public class Producer implements Runnable{
	Q q;
	Thread t;
	
	public Producer(Q q) {
		this.q = q;
		t = new Thread(this, "Производитель");
	}

	@Override
	public void run() {
		int i = 0;
		while(i < 10) {
			q.put(i++);
		}
	}
}

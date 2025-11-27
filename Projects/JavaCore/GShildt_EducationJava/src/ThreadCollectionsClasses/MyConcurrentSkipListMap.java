package ThreadCollectionsClasses;

import java.util.concurrent.ConcurrentSkipListMap;

public class MyConcurrentSkipListMap {
	public static void main(String[] args) {
		try {
			ConcurrentSkipListMap<String, Integer> collection = new ConcurrentSkipListMap<String, Integer>();
			
			WriteResultTask wrt1 = new WriteResultTask(collection, "c");
			WriteResultTask wrt2 = new WriteResultTask(collection, "b");
			
			Thread t1 = new Thread (wrt1);
			Thread t2 = new Thread (wrt2);
			
			t1.start();
			t2.start();
			
			t1.join();
			t2.join();
			 
			 System.out.println(collection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class WriteResultTask implements Runnable {
	private final ConcurrentSkipListMap<String, Integer> collection;
	private final String symb;
	
	public WriteResultTask(ConcurrentSkipListMap<String, Integer> collection, String symb) {
		this.collection = collection;
		this.symb = symb;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 3; i++) {
			collection.put(symb + Integer.toString(i), i);
		}
	}
}
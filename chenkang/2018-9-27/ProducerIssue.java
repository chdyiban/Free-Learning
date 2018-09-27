package mainconsole;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerIssue {


	public static void main(String[] args) {
		Sys sysObj = new Sys();
		new Thread(new Producer(sysObj)).start();
		new Thread(new Consumer(sysObj)).start();
		
		new Thread(new Producer(sysObj)).start();
		new Thread(new Consumer(sysObj)).start();
		
		new Thread(new Producer(sysObj)).start();
		new Thread(new Consumer(sysObj)).start();
		
		new Thread(new Producer(sysObj)).start();
		new Thread(new Consumer(sysObj)).start();
	}
	
}

class Producer implements Runnable {
	private Integer nextp;
	private Sys sysObj;
	
	public Producer(Sys sysObj) {
		this.sysObj = sysObj;
	}
	
	@Override
	public void run() {
		while(true) {
			// Producing.
			Random rand = new Random(System.nanoTime());
			nextp = rand.nextInt(2333);
			System.out.println(nextp + " was put in!");
			sysObj.P(sysObj.empty);
			//sysObj.P(sysObj.mutex);
			
			synchronized (sysObj.buf) {
				sysObj.buf[sysObj.in] = nextp;
//				try 							{Thread.sleep(3000);}
//				catch (InterruptedException e) 	{e.printStackTrace();}
				sysObj.in = (sysObj.in + 1) % Sys.N;
			}
			
			//sysObj.V(sysObj.mutex);
			sysObj.V(sysObj.full);
		}
	}
	
}


class Consumer implements Runnable {
	private Integer nextc;
	private Sys sysObj;
	
	public Consumer(Sys sysObj) {
		this.sysObj = sysObj;
	}
	
	@Override
	public void run() {
		while(true) {
			// Consuming.
			sysObj.P(sysObj.full);
			//sysObj.P(sysObj.mutex);
			
			synchronized (sysObj.buf) {
				nextc = sysObj.buf[sysObj.out];
//				try 							{Thread.sleep(3000);}
//				catch (InterruptedException e) 	{e.printStackTrace();}
				sysObj.out = (sysObj.out + 1) % Sys.N;
			}
			
			System.out.println(nextc + " was taken out!");
			//sysObj.V(sysObj.mutex);
			sysObj.V(sysObj.empty);
		
		}
	}
}

class Sys {
	public static final Integer N = 3;

	public Integer in = 0, out = 0;
	public AtomicInteger empty = new AtomicInteger(N), full = new AtomicInteger(0);
	public AtomicInteger mutex = new AtomicInteger(1);
	public Integer[] buf = new Integer[N];

	public LinkedList<Thread> objOnWaitQ = new LinkedList<Thread>();

	public synchronized void P(AtomicInteger s) {
		if(s.decrementAndGet() < 0) {
			Thread t = Thread.currentThread();
			objOnWaitQ.add(t);
			System.out.println("   #" + Thread.currentThread().getName() + " -> blocked");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void V(AtomicInteger s) {
		if(s.addAndGet(1) <= 0) {
			System.out.println("   #" + Thread.currentThread().getName() + " -> running");
			notify();
		}
	}
}
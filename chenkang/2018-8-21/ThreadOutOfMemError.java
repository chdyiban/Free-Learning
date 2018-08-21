/**
 * Args: -Xss2m
 * 参数-Xss用于设置虚拟机栈的容量（而非深度？）
 */
public class ThreadOutOfMemError {

	public static void main(String[] args) {
		while(true) {
			new Thread(new IndefinitedThread()).start();
		}
	}

}

class IndefinitedThread implements Runnable {
	
	public void neverSleep() { while(true); }
	
	@Override
	public void run() {
		neverSleep();
	}
	
}

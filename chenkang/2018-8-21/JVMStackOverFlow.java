/**
 * Args: -Xms20m -Xmx20m
 * 参数-Xms、-Xmx指定了虚拟机的堆区内存的最小值、最大值。
 */
public class JVMStackOverFlow {
	
	private int counter;

	public void overFlow() {
		System.out.println(counter++);
		overFlow();
	}

	public static void main(String[] args) {
		new JVMStackOverFlow().overFlow();
	}
	
}

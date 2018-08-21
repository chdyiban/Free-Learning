import java.util.*;

/**
 * Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * 参数-XX:+HeapDumpOnOutOfMemoryError指定了当发生HeapDumpOnOutOfMemoryError时
 * 进行转储
 */
public class HeapOOM{
	
	static class OOMObject {}
	
	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();

		while(true) {
			list.add(new OOMObject());
		}
	}

}
